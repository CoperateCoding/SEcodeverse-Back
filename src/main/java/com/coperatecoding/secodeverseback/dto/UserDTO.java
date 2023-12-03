package com.coperatecoding.secodeverseback.dto;

import com.coperatecoding.secodeverseback.domain.CodingBadge;
import com.coperatecoding.secodeverseback.domain.RoleType;
import com.coperatecoding.secodeverseback.domain.ctf.CTFTeam;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

public class UserDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RegisterRequest {

        @NotNull
        @Size(min = 4, max = 12, message ="아이디는 4에서 12자 사이 입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9]*[a-zA-Z]+[a-zA-Z0-9]*$", message = "아이디 형식이 일치하지 않습니다.")
        private String id;

        @NotNull
        @Size(min = 12, max = 20,message ="비밀번호는 12에서 20자 사이 입니다.")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[~!@#$%^&*()_=+|{};:<>/?])[a-zA-Z0-9~!@#$%^&*()_=+|{};:,.<>/?]*$"
                , message = "비밀번호 형식이 일치하지 않습니다.")
        private String pw;

        @NotNull
        @Size(min = 2, max = 10, message = "이름은 10자 이하이어야 합니다.")
        @Pattern(regexp = "^[가-힣]*$", message = "이름은 한글이어야 합니다.")
        private String name;

        @NotNull
        @Size(min = 2, max = 8, message = "닉네임은 2에서 8자 사이 입니다.")
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
        private RoleType roleType;
        private String nickName;
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
    @Builder
    public static class UserInfoResponse {
        private String nickName;
        private String badgeName;
        private Integer exp;
        private String imgUrl;
    }


}
