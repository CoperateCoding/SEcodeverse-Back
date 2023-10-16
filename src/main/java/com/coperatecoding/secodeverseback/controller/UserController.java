package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.dto.UserDTO;
//import com.coperatecoding.secodeverseback.service.JwtService;
import com.coperatecoding.secodeverseback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "UserController", description = "User 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final UserService userService;
//    private final JwtService jwtService;


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Operation(summary = "회원가입", description = """
    [모두 접근가능] 회원가입을 할 수 있습니다.<br>
    201: 성공<br>
    409: 같은 uid가 이미 존재함
    """)
    @PostMapping("/signup")
    public ResponseEntity<UserDTO.RegisterResponse> signUp (@RequestBody @Valid UserDTO.RegisterRequest registerRequest) throws RuntimeException {
        userService.signUp(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                UserDTO.RegisterResponse
                        .builder()
                        .id(registerRequest.getId())
                        .nickname(registerRequest.getNickname())
                        .build()
        );

    }


//    @PostMapping("/login") //로그인
//    public ResponseEntity<UserDTO.LoginResponse> authenticate(
//            @RequestBody @Valid UserDTO.LoginRequest loginRequest
//    ) {
//        return ResponseEntity.ok(userService.authenticate(loginRequest));
//    }

//    @Operation(summary = "로그아웃", description = """
//    [로그인 필요] 로그아웃을 합니다.<br>
//    jwt토큰이 만료됩니다<br>
//    로그아웃 리다이랙트 url 꼭 넣기. &logout_redirect_uri={url} <br>
//    200: 성공<br>
//    """)
//    @PostMapping("/logout")
//    public ResponseEntity<Map> logout(@AuthenticationPrincipal User user, @RequestHeader(value="Authorization") String authHeader) {
//        Map<String, String> map = new HashMap<>();
//
//        String token = authHeader.substring(7);
//
//        //jwt토큰만료처리 -> 블랙리스트
//        jwtService.addBlackList(token);
//
//        return ResponseEntity.ok(map);
//    }

    @GetMapping("/id/{id}/exists") //중복 아이디 확인
    public ResponseEntity<Map> isExistsId(@PathVariable("id") String id) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", userService.isExistId(id));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/username/{username}/exists") //중복 닉네임 확인
    public ResponseEntity<Map> isExistsNickname(@PathVariable("username") String nickName) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", userService.isExistNickname(nickName));

        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


}
