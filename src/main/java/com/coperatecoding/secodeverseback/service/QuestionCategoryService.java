package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.QuestionCategoryRepository;
import com.coperatecoding.secodeverseback.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class QuestionCategoryService {

    private final QuestionCategoryRepository questionCategoryRepository;

    public String getName(Long categoryPk){
        QuestionCategory category = questionCategoryRepository.findById(categoryPk)
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));
        return category.getName();
    }

}
