package com.coperatecoding.secodeverseback.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

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


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login {
        @NotNull
        private String id;
        @NotNull
        private String password;

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

}
