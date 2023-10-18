package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.TestCase;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.dto.*;
import com.coperatecoding.secodeverseback.exception.CategoryNotFoundException;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.service.BoardService;
import com.coperatecoding.secodeverseback.service.LevelService;
import com.coperatecoding.secodeverseback.service.QuestionService;
import com.coperatecoding.secodeverseback.service.TestCaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "문제", description = "문제 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/question")

public class QuestionController {
    private final QuestionService questionService;
    private final TestCaseService testCaseService;
    private final LevelService levelService;
    @PostMapping("/post")
    public ResponseEntity makeQuestion(@AuthenticationPrincipal User user, @RequestBody QuestionandTestCaseDTO.AddQuestionAndTestRequest addQuestionRequest) {
        Question question = questionService.makeQuestion(user, addQuestionRequest.getQuestion());

        for (TestCaseDTO.AddtestCaseRequest testCase : addQuestionRequest.getTestCase()) {
            testCaseService.makeTestCase(question.getPk(), testCase);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("")
    public ResponseEntity getQuestions(){
        List<QuestionDTO.SearchQuestionListResponse>  questions = questionService.getQuestion();
        List<Map<String, Object>> questionsAndLevels = new ArrayList<>();
        for(QuestionDTO.SearchQuestionListResponse  question: questions){
            Level level = levelService.getLevel(question.getLevelPk());

            Map<String, Object> questionAndLevelMap = new HashMap<>();
            questionAndLevelMap.put("question", question);
            questionAndLevelMap.put("level", level);

            questionsAndLevels.add(questionAndLevelMap);
        }
        return ResponseEntity.ok(questionsAndLevels);
    }
    @GetMapping("/category = {categoryPk}")
    public ResponseEntity getCategoryQuestions(Long categoryPk){
        List<QuestionDTO.SearchQuestionListResponse>  questions = questionService.getCategoryQuestion(categoryPk);
        List<Map<String, Object>> questionsAndLevels = new ArrayList<>();
        for(QuestionDTO.SearchQuestionListResponse  question: questions){
            Level level = levelService.getLevel(question.getLevelPk());

            Map<String, Object> questionAndLevelMap = new HashMap<>();
            questionAndLevelMap.put("question", question);
            questionAndLevelMap.put("level", level);

            questionsAndLevels.add(questionAndLevelMap);
        }
        return ResponseEntity.ok(questionsAndLevels);
    }

}
