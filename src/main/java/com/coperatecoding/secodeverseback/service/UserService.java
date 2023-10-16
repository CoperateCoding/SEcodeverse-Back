package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.CodingBadge;
import com.coperatecoding.secodeverseback.domain.RefreshToken;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.UserDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.exception.UserLockedException;
import com.coperatecoding.secodeverseback.repository.CodingBadgeRepository;
import com.coperatecoding.secodeverseback.repository.RefreshTokenRepository;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.coperatecoding.secodeverseback.exception.UserInfoDuplicatedException;
import org.springframework.util.StringUtils;

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
//    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public User signUp(UserDTO.RegisterRequest dto) throws RuntimeException {

        if (isExistId(dto.getId())) {
            throw new UserInfoDuplicatedException("해당하는 id가 존재합니다.");
        }

        if (isExistNickname(dto.getNickname())) {
            throw new UserInfoDuplicatedException("해당하는 닉네임이 존재합니다.");
        }

        // 기본 Badge를 가져오기
        CodingBadge defaultBadge = codingBadgeRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("Default badge with pk 1 not found."));

        // User 생성 및 Badge 설정
        User newUser = User.makeUsers(dto.getId(), dto.getPw(), dto.getName(), dto.getNickname());
        newUser.setBadge(defaultBadge);

        userRepository.save(newUser);

        return newUser;
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

//    public UserDTO.LoginResponse authenticate(UserDTO.Login dto) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        dto.getId(),
//                        dto.getPassword()
//                )
//        );
//
//        User user = userRepository.findById(dto.getId())
//                .orElseThrow(() -> new NotFoundException());
//
//        String jwtToken = jwtService.generateToken(user);
//
//        return new UserDTO.LoginResponse(jwtToken, user.getId(), user.getName(), user.getNickname(), user.getRoleType().name());
//
//    }

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

//    public UserDTO.LoginResponse login(UserDTO.LoginRequest loginRequest, HttpServletRequest request) {
//        User user = userRepository.findById(loginRequest.getId())
//                .orElseGet(() -> null);
//
//        if (!user.isAccountNonLocked()) {
//            throw new UserLockedException();
//        }
//
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getId(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        //토큰 생성
//        String accessToken = jwtService.generateAccessToken(user);
//        String refreshToken = jwtService.generateRefreshToken(user);
//
//        refreshTokenRepository.save(
//                RefreshToken.makeRefreshToken(refreshToken, getClientIp(request), user, jwtService.extractExpiration(refreshToken))
//        );
//
//        return UserDTO.LoginResponse.makeResponse(accessToken, refreshToken);
//    }

//    public UserDTO.LoginResponse authenticate(UserDTO.LoginRequest loginRequest) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getId(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        User user = userRepository.findById(loginRequest.getId())
//                .orElseThrow(() -> new NotFoundException());
//
//        String jwtToken = jwtService.generateToken(user);
//
//        return new UserDTO.LoginResponse(jwtToken, user.getId(), user.getNickname(), user.getRoleType().name());
//
//    }
}
