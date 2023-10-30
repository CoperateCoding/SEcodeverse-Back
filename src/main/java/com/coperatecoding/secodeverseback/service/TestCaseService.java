package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.TestCase;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.domain.question.QuestionCategory;
import com.coperatecoding.secodeverseback.dto.QuestionDTO;
import com.coperatecoding.secodeverseback.dto.TestCaseDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.QuestionRepository;
import com.coperatecoding.secodeverseback.repository.TestCaseRepository;

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
public class TestCaseService {
    private final TestCaseRepository testCaseRepository;
    private final QuestionRepository questionRepository;


    public TestCase makeTestCase(Long questionPk,TestCaseDTO.AddtestCaseRequest addTestCaseRequest) throws RuntimeException{
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));

        TestCase testCase = TestCase.makeTestCase(question,addTestCaseRequest.getInput(), addTestCaseRequest.getOutput());
        return testCaseRepository.save(testCase);
    }

    public List<TestCaseDTO.SearchResponse> getTestCaseList(Long questionPk){
        Question question = questionRepository.findById(questionPk)
                .orElseThrow(() -> new NotFoundException("해당하는 문제가 존재하지 않음"));

        List<TestCase>testCaseList = testCaseRepository.findByQuestion(question);
        List<TestCaseDTO.SearchResponse> testCaseDTOS= new ArrayList<>();
        for(TestCase testCase:testCaseList){
            TestCaseDTO.SearchListRequest request =TestCaseDTO.SearchListRequest.makeRequest(
                    testCase.getPk(),
                    testCase.getInput(),
                    testCase.getOutput()

            );

            TestCaseDTO.SearchResponse response = getTestCase(request);

            TestCaseDTO.SearchResponse testCaseDTO = TestCaseDTO.SearchResponse.builder()
                    .pk(response.getPk())
                    .input(response.getInput())
                    .output(response.getOutput())
                   .build();
            testCaseDTOS.add(testCaseDTO);

        }
        return testCaseDTOS;
    }

    public TestCaseDTO.SearchResponse getTestCase(TestCaseDTO.SearchListRequest request){
        TestCaseDTO.SearchResponse response =  TestCaseDTO.SearchResponse.builder()
                .pk(request.getPk())
                .input(request.getInput())
                .output(request.getOutput())
                .build();

        return response;
    }

    public TestCase modifyTestCase(Long testCasePK, TestCaseDTO.AddtestCaseRequest addQuestionRequest) throws RuntimeException{
       TestCase testCase= testCaseRepository.findById(testCasePK).orElseThrow(() -> new NotFoundException("해당하는 테스트케이스가 존재하지 않음"));

        testCase.updateTestCase(addQuestionRequest.getInput(), addQuestionRequest.getOutput());
        return testCase;
    }

    public void deleteTestCase(Long testCasePK) throws RuntimeException{
        TestCase testCase = testCaseRepository.findById(testCasePK).orElseThrow(() -> new NotFoundException("해당하는 테스트케이스가 존재하지 않음"));
        testCaseRepository.delete(testCase);
    }

}
