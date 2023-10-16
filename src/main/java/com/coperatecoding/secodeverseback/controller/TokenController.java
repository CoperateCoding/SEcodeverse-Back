//package com.coperatecoding.secodeverseback.controller;
//
//import com.coperatecoding.secodeverseback.domain.User;
//import com.coperatecoding.secodeverseback.dto.UserDTO;
//import com.coperatecoding.secodeverseback.repository.RefreshTokenRepository;
//import com.coperatecoding.secodeverseback.repository.UserRepository;
////import com.coperatecoding.secodeverseback.service.JwtService;
//import com.coperatecoding.secodeverseback.service.RefreshTokenService;
//import com.coperatecoding.secodeverseback.service.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RequiredArgsConstructor
//@RequestMapping("/api/v1")
//@RestController
//public class TokenController {
//
////    private final JwtService jwtService;
//    private final UserRepository userRepository;
//
//    private final RefreshTokenService refreshTokenService;
//    private final RefreshTokenRepository refreshTokenRepository;
//
//
//    @GetMapping("/token/validate")
//    public ResponseEntity<Map<String, Object>> validateJwtToken(@AuthenticationPrincipal User user) {
//        Map<String, Object> response = new HashMap<>();
//
////        boolean isValid = jwtService.isTokenValidByUsers(user);
//
//
//        Map<String, Object> userResponse = new HashMap<>();
//        if (isValid) {
//            userResponse.put("id", user.getId());
//            userResponse.put("username", user.getNickname());
//            userResponse.put("role", user.getRoleType().name());
//        }
//
//        response.put("isValid", isValid);
//        response.put("user", userResponse);
//
//        return ResponseEntity.ok(response);
//    }
//
////    @Operation(summary = "엑세스 토큰 재발급(리프레쉬 토큰 이용)", description = """
////    refresh token을 <X-REFRESH-TOKEN 헤더>에 넣어 보내면 새로운 access token을 반환<br>
////    ★★★주의★★★[Bearer asdf~~ ] 이런식으로 꼭!!! 넣어줘야함. Bearer 필수<br>
////    401: 리프레쉬 토큰이 유효하지 않음<br>
////    """)
////    @GetMapping("/token/reissue")
////    public ResponseEntity reissueAccessToken(@RequestHeader(value = "X-REFRESH-TOKEN") String bearerRefreshToken, HttpServletRequest request) {
////
////        UserDTO.LoginResponse response = refreshTokenService.reissue(bearerRefreshToken, request);
////
////        return ResponseEntity.ok(response);
////    }
//
//
//
//}
