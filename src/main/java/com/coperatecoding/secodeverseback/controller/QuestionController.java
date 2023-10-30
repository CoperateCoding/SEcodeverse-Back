package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.TestCase;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Level;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.domain.question.QuestionImage;
import com.coperatecoding.secodeverseback.dto.*;
import com.coperatecoding.secodeverseback.exception.CategoryNotFoundException;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
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
    private final QuestionImgService questionImgService;

    @PostMapping("/post")
    public ResponseEntity makeQuestion(@AuthenticationPrincipal User user, @RequestBody QuestionAndTestAndImageDTO.AddQuestionAndTestAndImageRequest addQuestionAndTestAndImageRequest) {
        Question question = questionService.makeQuestion(user, addQuestionAndTestAndImageRequest.getQuestion());

        for (TestCaseDTO.AddtestCaseRequest testCase : addQuestionAndTestAndImageRequest.getTestCase()) {
            testCaseService.makeTestCase(question.getPk(), testCase);
        }

        for(QuestionImgDTO.AddQuestionImgRequest questionImg: addQuestionAndTestAndImageRequest.getImg()){
            questionImgService.makeQuestionImg(question.getPk(),questionImg);
        }


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{questionPk}")
    public ResponseEntity modifyQuestion(@PathVariable Long questionPk, @RequestBody QuestionAndTestAndImageDTO.AddQuestionAndTestAndImageRequest addQuestionAndTestRequest) {
        questionService.modifyQuestion(questionPk, addQuestionAndTestRequest.getQuestion());
        List<TestCaseDTO.SearchResponse>testCaseDTOS = testCaseService.getTestCaseList(questionPk);
        List<QuestionImgDTO.SearchQuestionImgListResponse>questionImgDTOS = questionImgService.getQuestionImg(questionPk);
        System.out.println("이미지 크기는"+questionImgDTOS.size());
        int i=0;
        for (TestCaseDTO.SearchResponse testCase : testCaseDTOS) {
            testCaseService.modifyTestCase(testCase.getPk(),addQuestionAndTestRequest.getTestCase().get(i));
            i++;
        }
        int j=0;
        System.out.println(addQuestionAndTestRequest.getImg().size());
        for(QuestionImgDTO.SearchQuestionImgListResponse questionImg: questionImgDTOS){
            System.out.println(addQuestionAndTestRequest.getImg().get(j).getImgUrl());
            questionImgService.modifyQuestionImg(questionImg.getPk(),addQuestionAndTestRequest.getImg().get(j));
            j++;
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{questionPk}")
    public ResponseEntity deleteQuestion(@PathVariable Long questionPk){
        try{
            List<QuestionImgDTO.SearchQuestionImgListResponse> imgDTOS=questionImgService.getQuestionImg(questionPk);
            for (QuestionImgDTO.SearchQuestionImgListResponse img : imgDTOS) {
                questionImgService.deleteImg(img.getPk());
            }
            List<TestCaseDTO.SearchResponse>testCaseDTOS = testCaseService.getTestCaseList(questionPk);
            for (TestCaseDTO.SearchResponse testCase : testCaseDTOS) {
                testCaseService.deleteTestCase(testCase.getPk());
            }
            questionService.deleteQuestion(questionPk);
            return ResponseEntity.noContent().build();
        }
            catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }

    }
    @GetMapping("/{questionPk}")
    public ResponseEntity< Map<String, Object>>detailQuestion(@PathVariable Long questionPk){
        Map<String, Object> response = new HashMap<>();

        Question question = questionService.getDetailQuestion(questionPk);
        List<TestCaseDTO.SearchResponse> testCases = testCaseService.getTestCaseList(questionPk);
        List<QuestionImgDTO.SearchQuestionImgListResponse> imgs = questionImgService.getQuestionImg(questionPk);
        Map<String,Object>questionMap = new HashMap<>();

        questionMap.put("title",question.getTitle());
        questionMap.put("intro",question.getIntro());
        questionMap.put("content",question.getContent());
        questionMap.put("limitation",question.getLimitations());
        questionMap.put("source",question.getSource());
        questionMap.put("language",question.getLanguage());
        questionMap.put("testcaseDescription",question.getTestcaseDescription());
        List<Map<String, Object>> questionList = (List<Map<String, Object>>) response.get("question");
        if (questionList == null) {
            questionList = new ArrayList<>();
        }
        questionList.add(questionMap);
        response.put("question",questionList);
        for(TestCaseDTO.SearchResponse testCase : testCases){
            Map<String, Object> testCaseMap = new HashMap<>();
            testCaseMap.put("input",testCase.getInput());
            testCaseMap.put("output",testCase.getOutput());
            List<Map<String, Object>> testCaseList = (List<Map<String, Object>>) response.get("testcase");
            if (testCaseList == null) {
                testCaseList = new ArrayList<>();
            }
            testCaseList.add(testCaseMap);

            response.put("testcase", testCaseList);

        }
        if(imgs.size()>0){

            for(QuestionImgDTO.SearchQuestionImgListResponse img : imgs){
                Map<String,Object>imgMap = new HashMap<>();
                imgMap.put("imgUrl",img.getImgUrl());
                List<Map<String, Object>> imgList = (List<Map<String, Object>>) response.get("img");
                if (imgList == null) {
                    imgList = new ArrayList<>();
                }
                imgList.add(imgMap);

                response.put("img", imgList);

            }
        }


        return ResponseEntity.ok(response);

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



