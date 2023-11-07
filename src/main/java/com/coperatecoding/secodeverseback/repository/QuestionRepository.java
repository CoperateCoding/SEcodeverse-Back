package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
        List<Question> findByCategory(QuestionCategory questionCategory);
        List<Question> findByLevel(Level level);
        List<Question>findByCategoryAndLevel(QuestionCategory questionCategory, Level level);
        List<Question> findByTitleContaining(String title);
        Page<Question> findByUser(User user, Pageable pageable);
        List<Question> findByUser(User user);}


