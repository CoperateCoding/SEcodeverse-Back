package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.ctf.CTFCategory;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.CTFCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CTFCategoryService {

    private final CTFCategoryRepository ctfCategoryRepository;

    public List<CTFCategory> getCTFCategoryAll() {
        List<CTFCategory> categories = ctfCategoryRepository.findAll();
        //다 풀었는지 - 그 카테고리 문제 다 풀었는지 정보
        return categories;
    }

    public Long getCategoryPk(String categoryName
    ) throws RuntimeException {
        CTFCategory ctfCategory = ctfCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NotFoundException("해당 ctf 카테고리가 존재하지 않습니다."));

        return ctfCategory.getPk();
    }
}
