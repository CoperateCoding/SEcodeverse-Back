package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.CodingBadge;
import com.coperatecoding.secodeverseback.domain.RoleType;
import com.coperatecoding.secodeverseback.domain.board.Likes;
import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RegisterRequest {
        @NotNull
        @Size(min = 6, max = 12, message ="아이디는 6에서 12자 사이 입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]*[a-zA-Z]+[a-zA-Z0-9]*$", message = "아이디 형식이 일치하지 않습니다.")
        private String id;

        @NotNull
        @Size(min = 12, max = 20,message ="비밀번호는 12에서 20자 사이 입니다.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*()_=+|{};:<>/?])[a-zA-Z0-9~!@#$%^&*()_=+|{};:,.<>/?]*$"
                , message = "비밀번호 형식이 일치하지 않습니다.")
        private String pw;

        @NotNull
        @Size(min = 2, max = 10, message = "이름은 10자 이하이어야 합니다.")
        @Pattern(regexp = "^[가-힣]*$", message = "이름은 한글이어야 합니다.")
        private String name;

        @NotNull
        @Size(min = 2, max = 10, message = "닉네임은 2에서 10자 사이 입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]*$", message = "닉네임은 한글 또는 영문이 필수이며, 숫자는 선택입니다.")
        private String nickname;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class RegisterResponse {
        private String id;
        private String nickname;
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRequest {
        @NotNull
        private String id;
        @NotNull
        private String pw;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
        public static LoginResponse makeResponse(String accessToken, String refreshToken) {
            return new LoginResponse(accessToken, refreshToken);
        }


    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginAllResponse {
        private Long pk;
        private CTFTeam team;
        private CodingBadge codingBadge;
        private RoleType roleType;
        private String id;
        private String name;
        private String nickname;
        private Integer exp;
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfoResponse {

        private String nickName;
        private String badgeName;
        private String imgUrl;
        public static UserInfoResponse makeResponse(String nickName, String badgeName, String imgUrl) {
            return new UserInfoResponse(nickName, badgeName, imgUrl);
        }

    }

//    @Getter
//    @Builder
//    @AllArgsConstructor(access = AccessLevel.PROTECTED)
//    @NoArgsConstructor(access = AccessLevel.PROTECTED)
//    public static class LoginResponse {
//        private String token;
//        private UserInfo user;
//
//        public LoginResponse(String token, String id, String username, String role) {
//            this.token = token;
//            this.user = new UserInfo(id, username, role);
//        }
//
//        @Data
//        @AllArgsConstructor
//        private class UserInfo {
//            private String id;
//            private String username;
//            private String role;
//
//        }
//
//        public String getId() {
//            return this.user.id;
//        }
//    }


}
