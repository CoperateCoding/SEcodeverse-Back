package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long>  {
}
