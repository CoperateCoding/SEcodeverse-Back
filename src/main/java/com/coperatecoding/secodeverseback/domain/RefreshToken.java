//package com.coperatecoding.secodeverseback.domain;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.Getter;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//
//@Getter
//@Entity
//public class RefreshToken {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long pk;
//
//    @NotNull
//    private String ip;
//
//    @NotNull
//    private String refreshToken;
//
//    @NotNull
//    @ManyToOne
//    @JoinColumn(name = "user_pk")
//    private User user;
//
//    @NotNull
//    @Column(name = "expired_at")
//    private LocalDateTime expiredAt;
//
//    public static RefreshToken makeRefreshToken(String refreshToken, String ip, User user, Date expiredAt) {
//        RefreshToken newRefresh = new RefreshToken();
//        newRefresh.refreshToken = refreshToken;
//        newRefresh.ip = ip;
//        newRefresh.user = user;
//        newRefresh.expiredAt = expiredAt.toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDateTime();
//        return newRefresh;
//    }
//}
