package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CTFCategoryRepository extends JpaRepository<CTFCategory, Long> {
    Optional<CTFCategory> findByName(String categoryName);
}
