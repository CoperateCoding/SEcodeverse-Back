package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CTFQuestionRepository extends JpaRepository<CTFQuestion, Long> {
}
