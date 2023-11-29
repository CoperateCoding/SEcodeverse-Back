package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.CodingBadge;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.UserDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.exception.UserLockedException;
import com.coperatecoding.secodeverseback.repository.CodingBadgeRepository;
import com.coperatecoding.secodeverseback.repository.RedisRepository;
import com.coperatecoding.secodeverseback.repository.RefreshTokenRepository;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.coperatecoding.secodeverseback.exception.UserInfoDuplicatedException;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CodingBadgeRepository codingBadgeRepository;
    private final RedisRepository redisRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public void signUp(@Validated UserDTO.RegisterRequest dto) throws UserInfoDuplicatedException {

        if (isExistId(dto.getId())) {
            throw new UserInfoDuplicatedException("해당하는 id가 존재합니다.");
        }

        if (isExistNickname(dto.getNickname())) {
            throw new UserInfoDuplicatedException("해당하는 닉네임이 존재합니다.");
        }

        // 기본 Badge 가져오기
        CodingBadge defaultBadge = codingBadgeRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Default badge with pk 1 not found."));

        // User 생성 및 Badge 설정
        User newUser = User.makeUsers(dto.getId(), passwordEncoder.encode(dto.getPw()), dto.getName(), dto.getNickname());
        newUser.setBadge(defaultBadge);

         userRepository.save(newUser);

   }

    //중복 아이디 확인
    @Transactional(readOnly = true)
    public boolean isExistId(String id) {
        User user = userRepository.findById(id)
                .orElseGet(() -> null);

        return (user == null)? false : true;
    }

    //중복 닉네임 확인
    @Transactional(readOnly = true)
    public boolean isExistNickname(String nickName) {
        User user = userRepository.findByNickname(nickName)
                .orElseGet(() -> null);

        return (user == null)? false : true;
    }

    public static String getClientIp(HttpServletRequest request) {
        String clientIp = null;
        boolean isIpInHeader = false;

        List<String> headerList = new ArrayList<>();
        headerList.add("X-Forwarded-For");
        headerList.add("HTTP_CLIENT_IP");
        headerList.add("HTTP_X_FORWARDED_FOR");
        headerList.add("HTTP_X_FORWARDED");
        headerList.add("HTTP_FORWARDED_FOR");
        headerList.add("HTTP_FORWARDED");
        headerList.add("Proxy-Client-IP");
        headerList.add("WL-Proxy-Client-IP");
        headerList.add("HTTP_VIA");
        headerList.add("IPV6_ADR");

        for (String header : headerList) {
            clientIp = request.getHeader(header);
            if (StringUtils.hasText(clientIp) && !clientIp.equals("unknown")) {
                isIpInHeader = true;
                break;
            }
        }

        if (!isIpInHeader) {
            clientIp = request.getRemoteAddr();
        }

        return clientIp;
    }

    public UserDTO.LoginResponse login(UserDTO.LoginRequest loginRequest, HttpServletRequest request) {
        User user = userRepository.findById(loginRequest.getId())
                .orElseGet(() -> null);

        if (!user.isAccountNonLocked()) {
            throw new UserLockedException();
        }

//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getId(),
//                        loginRequest.getPw()
//                )
//        );
        // authenticate 메소드가 인증된 Authentication 객체를 반환
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getId(),
                        loginRequest.getPw()
                )
        );

        // SecurityContextHolder에 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);


        //토큰 생성
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        redisRepository.saveRefreshToken(loginRequest.getId(), refreshToken);
        return UserDTO.LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .roleType(user.getRoleType())
                .build();

    }

    public void logout(String userId) {
        redisRepository.deleteRefreshToken(userId);
    }

    private CodingBadge findBadgeByExp(Integer exp) {
        Long badgeId;
        if (exp >= 5000) {
            badgeId = 6L;
        } else if (exp >= 1200) {
            badgeId = 5L;
        } else if (exp >= 800) {
            badgeId = 4L;
        } else if (exp >= 500) {
            badgeId = 3L;
        } else if (exp >= 100) {
            badgeId = 2L;
        } else {
            badgeId = 1L;
        }
        return codingBadgeRepository.findById(badgeId)
                .orElseThrow(() -> new NotFoundException("해당하는 코딩뱃지가 존재하지 않습니다."));
    }


    public UserDTO.UserInfoResponse getUserInfo(User user) {
        Integer userExp = user.getExp();
        if (userExp != null) {
            CodingBadge newBadge = findBadgeByExp(userExp);
            user.setBadge(newBadge);
            userRepository.save(user);
        }

        UserDTO.UserInfoResponse userInfoResponse = UserDTO.UserInfoResponse.builder()
                .nickName(user.getNickname())
                .exp(userExp)
                .badgeName(user.getBadge().getName())
                .imgUrl(user.getBadge().getImgUrl())
                .build();

        return userInfoResponse;
    }


}
