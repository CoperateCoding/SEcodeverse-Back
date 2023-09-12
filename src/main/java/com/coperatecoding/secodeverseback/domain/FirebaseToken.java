package com.coperatecoding.secodeverseback.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "firebase_token")
public class FirebaseToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_pk", referencedColumnName = "pk")
    private Users user;

    @NotNull
    @Column(length = 999999999)
    private String token; // 사용자 기기 토큰

    public static FirebaseToken makeFirebaseToken(Users user, String token) {
        FirebaseToken firebaseToken = new FirebaseToken();
        firebaseToken.user = user;
        firebaseToken.token = token;

        return firebaseToken;
    }

}
