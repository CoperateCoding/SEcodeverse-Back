package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.CodingBadge;
import com.coperatecoding.secodeverseback.domain.RoleType;
import com.coperatecoding.secodeverseback.domain.board.Likes;
import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class UserDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegisterRequest {
        @NotNull
        private String id;
        @NotNull
        private String pw;
        @NotNull
        private String name;
        @NotNull
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
    public static class LoginResponse {
        private String token;
        private String accessToken;
        private String refreshToken;
        public static LoginResponse makeResponse(String accessToken, String refreshToken) {
            return new LoginResponse(accessToken, accessToken, refreshToken);
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
