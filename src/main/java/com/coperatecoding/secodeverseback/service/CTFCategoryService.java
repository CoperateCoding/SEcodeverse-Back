package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.ctf.CTFCategory;
import com.coperatecoding.secodeverseback.repository.CTFCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return categories;
    }

}
