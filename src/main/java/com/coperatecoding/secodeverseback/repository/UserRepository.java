package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String id);

    Optional<User> findByNickname(String nickname);

}
