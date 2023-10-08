package com.coperatecoding.secodeverseback.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "jwt_token_blacklist")
public class JwtTokenBlackList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    private String token;

    @NotNull
    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public static JwtTokenBlackList makeEntity(String token, LocalDateTime expiredAt) {
        return new JwtTokenBlackList(null, token, expiredAt);
    }
}
