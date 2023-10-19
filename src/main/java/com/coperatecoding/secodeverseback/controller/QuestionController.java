package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.TestCase;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.dto.*;
import com.coperatecoding.secodeverseback.exception.CategoryNotFoundException;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.service.*;
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
import org.springframework.web.util.UriComponentsBuilder;

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
    private final QuestionCategoryService questionCategoryService;

    @PostMapping("/post")
    public ResponseEntity makeQuestion(@AuthenticationPrincipal User user, @RequestBody QuestionandTestCaseDTO.AddQuestionAndTestRequest addQuestionRequest) {
        Question question = questionService.makeQuestion(user, addQuestionRequest.getQuestion());

        for (TestCaseDTO.AddtestCaseRequest testCase : addQuestionRequest.getTestCase()) {
            testCaseService.makeTestCase(question.getPk(), testCase);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    public ResponseEntity<List<QuestionDTO.SearchQuestionListResponse>> getQuestions(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sort", required = false) SortType sort,
            @RequestParam(value = "categoryPk", required = false) List<Long> categoryPks,
            @RequestParam(value = "levelPk", required = false) List<Long> levelPks
    ) {

        List<QuestionDTO.SearchQuestionListResponse> questions = new ArrayList<>();


            if (sort != null) {
            if (categoryPks != null && !categoryPks.isEmpty() && levelPks != null && !levelPks.isEmpty()) {
                for (Long categoryPk : categoryPks) {
                    for (Long levelPk : levelPks) {
                        List<QuestionDTO.SearchQuestionListResponse> matchingQuestions = questionService.getMatchingQuestions(true, categoryPk, levelPk);
                        questions.addAll(matchingQuestions);
                    }
                }
            } else if (categoryPks != null && !categoryPks.isEmpty()) {
                for (Long categoryPk : categoryPks) {
                    List<QuestionDTO.SearchQuestionListResponse> categoryQuestions = questionService.getCategoryQuestion(true, categoryPk);
                    questions.addAll(categoryQuestions);
                }
            } else if (levelPks != null && !levelPks.isEmpty()) {
                for (Long levelPk : levelPks) {
                    List<QuestionDTO.SearchQuestionListResponse> levelQuestions = questionService.getLevelQuestionList(true, levelPk);
                    questions.addAll(levelQuestions);
                }
            }
        } else {
            if (categoryPks != null && !categoryPks.isEmpty() && levelPks != null && !levelPks.isEmpty()) {
                for (Long categoryPk : categoryPks) {
                    for (Long levelPk : levelPks) {
                        List<QuestionDTO.SearchQuestionListResponse> matchingQuestions = questionService.getMatchingQuestions(false, categoryPk, levelPk);
                        questions.addAll(matchingQuestions);
                    }
                }
            } else if (categoryPks != null && !categoryPks.isEmpty()) {
                for (Long categoryPk : categoryPks) {
                    List<QuestionDTO.SearchQuestionListResponse> categoryQuestions = questionService.getCategoryQuestion(false, categoryPk);
                    questions.addAll(categoryQuestions);
                }
            } else if (levelPks != null && !levelPks.isEmpty()) {
                for (Long levelPk : levelPks) {
                    List<QuestionDTO.SearchQuestionListResponse> levelQuestions = questionService.getLevelQuestionList(false, levelPk);
                    questions.addAll(levelQuestions);
                }
            }
            if (questions.isEmpty()) {
                questions = questionService.getQuestion();

            }
        }

            return ResponseEntity.ok(questions);
        }

    }



