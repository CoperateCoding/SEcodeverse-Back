package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.dto.LevelDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.LevelRepository;
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
public class LevelService {

    private final LevelRepository levelRepository;

    public Level getLevel(Long levelPk){
        Level level = levelRepository.findById(levelPk).orElseThrow(() -> new NotFoundException("해달하는 레벨이 존재하지 않음"));
        return level;

    }
}
