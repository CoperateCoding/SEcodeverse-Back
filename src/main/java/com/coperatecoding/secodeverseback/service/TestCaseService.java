package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.TestCase;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.dto.TestCaseDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.QuestionRepository;
import com.coperatecoding.secodeverseback.repository.TestCaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;
    private final QuestionRepository questionRepository;


    public TestCase makeTestCase(Long questionPk,TestCaseDTO.AddtestCaseRequest addTestCaseRequest) throws RuntimeException{
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));

        TestCase testCase = TestCase.makeTestCase(question,addTestCaseRequest.getInput(), addTestCaseRequest.getOutput());
        return testCaseRepository.save(testCase);
    }
}
