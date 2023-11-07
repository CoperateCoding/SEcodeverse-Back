package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.dto.*;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.service.*;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        List<TestCaseDTO.SearchResponse> testCaseDTOS = testCaseService.getTestCaseList(questionPk);
        List<QuestionImgDTO.SearchQuestionImgResponse> questionImgDTOS = questionImgService.getQuestionImg(questionPk);
        System.out.println("이미지 크기는"+questionImgDTOS.size());
        int i=0;
        for (TestCaseDTO.SearchResponse testCase : testCaseDTOS) {
            testCaseService.modifyTestCase(testCase.getPk(),addQuestionAndTestRequest.getTestCase().get(i));
            i++;
        }
        int j=0;
        System.out.println(addQuestionAndTestRequest.getImg().size());
        for(QuestionImgDTO.SearchQuestionImgResponse questionImg: questionImgDTOS){
            System.out.println(addQuestionAndTestRequest.getImg().get(j).getImgUrl());
            questionImgService.modifyQuestionImg(questionImg.getPk(),addQuestionAndTestRequest.getImg().get(j));
            j++;
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{questionPk}")
    public ResponseEntity deleteQuestion(@PathVariable Long questionPk){
        try{
            List<QuestionImgDTO.SearchQuestionImgResponse> imgDTOS=questionImgService.getQuestionImg(questionPk);
            for (QuestionImgDTO.SearchQuestionImgResponse img : imgDTOS) {
                questionImgService.delete(img.getPk());
            }
            List<TestCaseDTO.SearchResponse>testCaseDTOS = testCaseService.getTestCaseList(questionPk);
            for (TestCaseDTO.SearchResponse testCase : testCaseDTOS) {
                testCaseService.delete(testCase.getPk());
            }
            questionService.deleteQuestion(questionPk);
            return ResponseEntity.noContent().build();
        }
        catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }

    }
    @GetMapping("/solve/user={userPk}")
    public ResponseEntity<List<QuestionDTO.questionPagingResponse>>getUserQuestion(@AuthenticationPrincipal User user,
                                                                                   @RequestParam(defaultValue = "1") int page,
                                                                                   @RequestParam(defaultValue = "10") int pageSize){

        List<CodeDTO.PageableCodeListResponse>codes=codeService.getUserCodes(user,page,pageSize);
        List<QuestionDTO.SearchQuestionResponse> questions=new ArrayList<>();
        for(CodeDTO.PageableCodeListResponse code: codes){
            Question question = questionService.findByPk(code.getQuestionPk());
            QuestionDTO.SearchQuestionResponse questionDTO=questionService.getByPk(question);
            questions.add(questionDTO);

        }
        List<QuestionDTO.questionPagingResponse> pagingQuestion = questionService.userPagingQuestion(codes.get(0).getCnt(),questions);

        return ResponseEntity.ok(pagingQuestion);
    }

    @GetMapping("/wrong/user={userPk}")
    public ResponseEntity<List<QuestionDTO.questionPagingResponse>> getWrongQuestion(@AuthenticationPrincipal User user,
                                                                                     @RequestParam(defaultValue = "1") int page,
                                                                                     @RequestParam(defaultValue = "10") int pageSize){

        List<CodeDTO.PageableCodeListResponse>codes=codeService.getWrongCodes(user,page,pageSize);
        List<QuestionDTO.SearchQuestionResponse> questions=new ArrayList<>();
        for(CodeDTO.PageableCodeListResponse code: codes){
            Question question = questionService.findByPk(code.getQuestionPk());
            QuestionDTO.SearchQuestionResponse questionDTO=questionService.getByPk(question);
            questions.add(questionDTO);

        }
        List<QuestionDTO.questionPagingResponse> pagingQuestion = questionService.userPagingQuestion(codes.get(0).getCnt(),questions);


        return ResponseEntity.ok(pagingQuestion);
    }



    @GetMapping("/{questionPk}")
    public ResponseEntity<QuestionAndTestAndImageDTO.QuestionAndTest> detailQuestion(@PathVariable Long questionPk) {

        Question question = questionService.getDetailQuestion(questionPk);
        List<TestCaseDTO.SearchResponse> testCases = testCaseService.getTestCaseList(questionPk);
        List<QuestionImgDTO.SearchQuestionImgResponse> imgs = questionImgService.getQuestionImg(questionPk);
        QuestionAndTestAndImageDTO.QuestionAndTest response = new QuestionAndTestAndImageDTO.QuestionAndTest();
        response.setQuestion(question);
        response.setTestCase(testCases);
        response.setImg(imgs);

        return ResponseEntity.ok(response);

    }

    @GetMapping("post/user={userPk}")
    public ResponseEntity<List<QuestionDTO.questionPagingResponse>> userPostQuestion(@AuthenticationPrincipal User user,
                                                                                     @RequestParam(defaultValue = "1") int page,
                                                                                     @RequestParam(defaultValue = "10") int pageSize){

        List<QuestionDTO.questionPagingResponse> question= questionService.userPostQuestion(user,page,pageSize);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/keyword={keyword}")
    public ResponseEntity<List<QuestionDTO.SearchQuestionResponse>> getKeywordQuestion(@PathVariable String keyword){
        List<QuestionDTO.SearchQuestionResponse> question= questionService.getKeywordQuestion(keyword);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<QuestionDTO.SearchQuestionResponse>> getRecentQuestion(){
        List<QuestionDTO.SearchQuestionResponse> questions = questionService.getRecentQuestion();
        return ResponseEntity.ok(questions);
    }
    @GetMapping("")
    public ResponseEntity<List<QuestionDTO.SearchQuestionResponse>> getQuestions(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sort", required = false) QuestionSortType sort,
            @RequestParam(value = "categoryPk", required = false) List<Long> categoryPks,
            @RequestParam(value = "levelPk", required = false) List<Long> levelPks
    ) {

        List<QuestionDTO.SearchQuestionResponse> questions = new ArrayList<>();


        if (sort != null) {
            if (categoryPks != null && !categoryPks.isEmpty() && levelPks != null && !levelPks.isEmpty()) {
                for (Long categoryPk : categoryPks) {
                    for (Long levelPk : levelPks) {
                        List<QuestionDTO.SearchQuestionResponse> matchingQuestions = questionService.getMatchingQuestions(true, categoryPk, levelPk);
                        questions.addAll(matchingQuestions);
                    }
                }
            } else if (categoryPks != null && !categoryPks.isEmpty()) {
                for (Long categoryPk : categoryPks) {
                    List<QuestionDTO.SearchQuestionResponse> categoryQuestions = questionService.getCategoryQuestion(true, categoryPk);
                    questions.addAll(categoryQuestions);
                }
            } else if (levelPks != null && !levelPks.isEmpty()) {
                for (Long levelPk : levelPks) {
                    List<QuestionDTO.SearchQuestionResponse> levelQuestions = questionService.getLevelQuestionList(true, levelPk);
                    questions.addAll(levelQuestions);
                }
            }
        } else {
            if (categoryPks != null && !categoryPks.isEmpty() && levelPks != null && !levelPks.isEmpty()) {
                for (Long categoryPk : categoryPks) {
                    for (Long levelPk : levelPks) {
                        List<QuestionDTO.SearchQuestionResponse> matchingQuestions = questionService.getMatchingQuestions(false, categoryPk, levelPk);
                        questions.addAll(matchingQuestions);
                    }
                }
            } else if (categoryPks != null && !categoryPks.isEmpty()) {
                for (Long categoryPk : categoryPks) {
                    List<QuestionDTO.SearchQuestionResponse> categoryQuestions = questionService.getCategoryQuestion(false, categoryPk);
                    questions.addAll(categoryQuestions);
                }
            } else if (levelPks != null && !levelPks.isEmpty()) {
                for (Long levelPk : levelPks) {
                    List<QuestionDTO.SearchQuestionResponse> levelQuestions = questionService.getLevelQuestionList(false, levelPk);
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


