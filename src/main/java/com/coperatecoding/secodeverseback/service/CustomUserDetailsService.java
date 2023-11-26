package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

//    @Autowired
//    private UserRepository userRepository;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User loadUserByUsername(final String username) {

        // User 엔티티를 데이터베이스에서 로드
        User user = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user;
    }

}
