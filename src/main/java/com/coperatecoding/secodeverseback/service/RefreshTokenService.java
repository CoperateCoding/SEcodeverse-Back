package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.RefreshToken;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.UserDTO;
import com.coperatecoding.secodeverseback.dto.UserDTO.LoginResponse;
import com.coperatecoding.secodeverseback.exception.TokenInValidException;
import com.coperatecoding.secodeverseback.repository.RefreshTokenRepository;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserDTO.LoginResponse reissue(String bearerRefreshToken, HttpServletRequest request) {
        if (bearerRefreshToken == null || !bearerRefreshToken.startsWith("Bearer ")) {
            throw new TokenInValidException("토큰 값이 이상함");
        }

        String token = bearerRefreshToken.substring(7);
        String id = jwtService.extractUsername(token);

        User user = userRepository.findById(id)
                .orElseGet(() -> null);

        //if (user != null) {
        List<RefreshToken> refreshTokenList = refreshTokenRepository.findByUser(user);

        RefreshToken refreshToken = null;

        for (RefreshToken r: refreshTokenList) {
            if (r.getRefreshToken().equals(token)) {
                refreshToken = r;
                break;
            }
        }

        if (refreshToken == null)
            throw new TokenInValidException("리프레쉬 토큰이 존재하지 않음");

        if (jwtService.isTokenExpired(token)) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenInValidException("리프레쉬 토큰이 만료됨");
        }

        if (!refreshToken.getIp().equals(UserService.getClientIp(request))) {
            //@TODO 다른 ip에서 로그인되었다는 알림을 보냄
        }

        String accessToken = jwtService.generateAccessToken(user);
        refreshTokenRepository.delete(refreshToken);
        String refreshTokenString = jwtService.generateRefreshToken(user);

        refreshTokenRepository.save(RefreshToken.makeRefreshToken(refreshTokenString, UserService.getClientIp(request), user, jwtService.extractExpiration(refreshTokenString)));

        return LoginResponse.makeResponse(accessToken, refreshTokenString);

    }

}
