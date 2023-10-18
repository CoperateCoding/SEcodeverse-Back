package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.TestCase;
import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestCaseRepository  extends JpaRepository<TestCase, Long> {
}
