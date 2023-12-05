package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import com.coperatecoding.secodeverseback.dto.question.CategoryDTO;
import com.coperatecoding.secodeverseback.dto.question.QuestionDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.QuestionCategoryRepository;
import com.coperatecoding.secodeverseback.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    public List<CategoryDTO.SearchQuestionCategoryResponse> getAllCategorys(){
        List<QuestionCategory> allCategorys = questionCategoryRepository.findAll();
        List<CategoryDTO.SearchQuestionCategoryResponse> resultCategorys = new ArrayList<>();
        List<CategoryDTO.SearchQuestionCategoryResponse> categoryDTOS = new ArrayList<>();
        for(QuestionCategory category : allCategorys){
            CategoryDTO.CategoryRequest request = CategoryDTO.CategoryRequest.category(
                    category.getPk(),
                    category.getName()
            );
            CategoryDTO.SearchQuestionCategoryResponse response = getCategory(request);

            CategoryDTO.SearchQuestionCategoryResponse categoryDTO = CategoryDTO.SearchQuestionCategoryResponse.builder()
                    .pk(response.getPk())
                    .categoryName(response.getCategoryName())
                    .build();
            categoryDTOS.add(categoryDTO);
        }
        return categoryDTOS;
    }

    public CategoryDTO.SearchQuestionCategoryResponse getCategory(CategoryDTO.CategoryRequest request) {
        CategoryDTO.SearchQuestionCategoryResponse response = CategoryDTO.SearchQuestionCategoryResponse.builder()
                .pk(request.getPk())
                .categoryName(request.getCategoryName())
                .build();

        return response;
    }

}
