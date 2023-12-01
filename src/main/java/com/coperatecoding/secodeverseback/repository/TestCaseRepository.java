package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.question.TestCase;

import com.coperatecoding.secodeverseback.domain.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCaseRepository  extends JpaRepository<TestCase, Long> {
    List<TestCase> findByQuestion(Question question);
}
