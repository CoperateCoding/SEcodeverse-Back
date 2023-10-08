package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.User;
import com.nimbusds.oauth2.sdk.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findByUser(User user);
}
