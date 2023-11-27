package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CTFCategoryRepository extends JpaRepository<CTFCategory, Long> {
}
