package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.dto.*;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    private final CodeService codeService;
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
    @GetMapping("/solve/user={userPk}")
    public ResponseEntity<List<QuestionDTO.SearchQuestionListResponse>>getUserQuestion(@AuthenticationPrincipal User user){

        List<CodeDTO.SearchCodeListResponse>codes=codeService.getUserCodes(user);
        List<QuestionDTO.SearchQuestionListResponse> questions=new ArrayList<>();
        for(CodeDTO.SearchCodeListResponse code: codes){
            Question question = questionService.findByPk(code.getQuestionPk());
            QuestionDTO.SearchQuestionListResponse questionDTO=questionService.getByPk(question);
            questions.add(questionDTO);

        }

        return ResponseEntity.ok(questions);
    }

@GetMapping("/wrong/user={userPk}")
public ResponseEntity<List<QuestionDTO.SearchQuestionListResponse>>getWrongQuestion(@AuthenticationPrincipal User user){

        List<CodeDTO.SearchCodeListResponse>codes=codeService.getWrongCodes(user);
        List<QuestionDTO.SearchQuestionListResponse> questions=new ArrayList<>();
        for(CodeDTO.SearchCodeListResponse code: codes){
            Question question = questionService.findByPk(code.getQuestionPk());
            QuestionDTO.SearchQuestionListResponse questionDTO=questionService.getByPk(question);
            questions.add(questionDTO);

        }

    return ResponseEntity.ok(questions);
    }

    @GetMapping("/{questionPk}")
    public  ResponseEntity <QuestionAndTestAndImageDTO.QuestionAndTest >detailQuestion(@PathVariable Long questionPk) {

        Question question = questionService.getDetailQuestion(questionPk);
        List<TestCaseDTO.SearchResponse> testCases = testCaseService.getTestCaseList(questionPk);
        List<QuestionImgDTO.SearchQuestionImgListResponse> imgs = questionImgService.getQuestionImg(questionPk);
        QuestionAndTestAndImageDTO.QuestionAndTest response = new QuestionAndTestAndImageDTO.QuestionAndTest();
        response.setQuestion(question);
        response.setTestCase(testCases);
        response.setImg(imgs);

        return ResponseEntity.ok(response);

    }

    @GetMapping("post/user={userPk}")
    public  ResponseEntity<List<QuestionDTO.SearchQuestionListResponse>> userPostQuestion(@AuthenticationPrincipal User user){
        List<QuestionDTO.SearchQuestionListResponse> question= questionService.userPostQuestion(user);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/keyword={keyword}")
    public  ResponseEntity<List<QuestionDTO.SearchQuestionListResponse>> getKeywordQuestion(@PathVariable String keyword){
            List<QuestionDTO.SearchQuestionListResponse> question= questionService.getKeywordQuestion(keyword);
        return ResponseEntity.ok(question);
    }

    @GetMapping("")
    public ResponseEntity<List<QuestionDTO.SearchQuestionListResponse>> getQuestions(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sort", required = false) SortType sort,
            @RequestParam(value = "categoryPk", required = false) List<Long> categoryPks,
            @RequestParam(value = "levelPk", required = false) List<Long> levelPks

    ) {

        List<QuestionDTO.SearchQuestionListResponse> questions = new ArrayList<>();
        if(categoryPks == null && levelPks == null){
            questions=questionService.getQuestion();
        }else {
            if (sort == SortType.RECENT) {
                List<QuestionDTO.SearchQuestionListResponse> lastQuestion = new ArrayList<>();
                if (categoryPks != null && !categoryPks.isEmpty() && levelPks != null && !levelPks.isEmpty()) {
                    for (Long categoryPk : categoryPks) {
                        for (Long levelPk : levelPks) {
                            List<QuestionDTO.SearchQuestionListResponse> matchingQuestions = questionService.getMatchingQuestions(true, categoryPk, levelPk);
                            lastQuestion.addAll(matchingQuestions);
                        }
                    }
                } else if (categoryPks != null && !categoryPks.isEmpty()) {
                    for (Long categoryPk : categoryPks) {
                        List<QuestionDTO.SearchQuestionListResponse> categoryQuestions = questionService.getCategoryQuestion(true, categoryPk);
                        lastQuestion.addAll(categoryQuestions);
                    }
                } else if (levelPks != null && !levelPks.isEmpty()) {
                    for (Long levelPk : levelPks) {
                        List<QuestionDTO.SearchQuestionListResponse> levelQuestions = questionService.getLevelQuestionList(true, levelPk);
                        lastQuestion.addAll(levelQuestions);
                    }

                }

                for (int i = lastQuestion.size() - 1; i > -1; i--) {
                    questions.add(lastQuestion.get(i));
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

            }
        }

            return ResponseEntity.ok(questions);
        }


    }



