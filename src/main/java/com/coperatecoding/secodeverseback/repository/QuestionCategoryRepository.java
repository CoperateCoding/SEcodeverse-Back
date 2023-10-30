package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Locale;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {

}
