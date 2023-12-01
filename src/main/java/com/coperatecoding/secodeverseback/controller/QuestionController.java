package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.question.Question;
import com.coperatecoding.secodeverseback.dto.*;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public ResponseEntity makeQuestion(@AuthenticationPrincipal User user, @RequestBody @Valid QuestionAndTestAndImageDTO.AddQuestionAndTestAndImageRequest addQuestionAndTestAndImageRequest) {
        Question question = questionService.makeQuestion(user, addQuestionAndTestAndImageRequest.getQuestion());


        for (TestCaseDTO.AddtestCaseRequest testCase : addQuestionAndTestAndImageRequest.getTestCase()) {
            testCaseService.makeTestCase(question.getPk(), testCase);
        }


        for(QuestionImgDTO.AddQuestionImgRequest questionImg: addQuestionAndTestAndImageRequest.getImg()){
            if(questionImg != null)
                questionImgService.makeQuestionImg(question.getPk(),questionImg);
        }


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{questionPk}")
    public ResponseEntity modifyQuestion(@AuthenticationPrincipal User user, @PathVariable Long questionPk, @RequestBody QuestionAndTestAndImageDTO.AddQuestionAndTestAndImageRequest addQuestionAndTestRequest) {
        questionService.modifyQuestion(questionPk, addQuestionAndTestRequest.getQuestion());
        List<TestCaseDTO.SearchResponse> testCaseDTOS = testCaseService.getTestCaseList(questionPk);
        List<QuestionImgDTO.SearchQuestionImgResponse> questionImgDTOS = questionImgService.getQuestionImg(questionPk);

        // 수정할 테스트케이스
        int i = 0;
        for (TestCaseDTO.SearchResponse testCase : testCaseDTOS) {
            if (i < addQuestionAndTestRequest.getTestCase().size()) {
                testCaseService.modifyTestCase(testCase.getPk(), addQuestionAndTestRequest.getTestCase().get(i));
            } else {
                // 새로운 테스트케이스의 크기를 벗어나면 삭제
                testCaseService.delete(testCase.getPk());
            }
            i++;
        }

        // 수정할 이미지
        int j = 0;
        for (QuestionImgDTO.SearchQuestionImgResponse questionImg : questionImgDTOS) {
            if (j < addQuestionAndTestRequest.getImg().size()) {
                questionImgService.modifyQuestionImg(questionImg.getPk(), addQuestionAndTestRequest.getImg().get(j));
            } else {
                // 새로운 이미지의 크기를 벗어나면 삭제
                questionImgService.delete(questionImg.getPk());
            }
            j++;
        }

        // 나머지 새 이미지 추가
        for (; j < addQuestionAndTestRequest.getImg().size(); j++) {
            questionImgService.makeQuestionImg(questionPk, addQuestionAndTestRequest.getImg().get(j));
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/delete/{questionPk}")
    public ResponseEntity deleteQuestion(@AuthenticationPrincipal User user, @PathVariable Long questionPk){
        try{
            List<QuestionImgDTO.SearchQuestionImgResponse> imgDTOS=questionImgService.getQuestionImg(questionPk);
            for (QuestionImgDTO.SearchQuestionImgResponse img : imgDTOS) {
                questionImgService.delete(img.getPk());
            }
            List<TestCaseDTO.SearchResponse>testCaseDTOS = testCaseService.getTestCaseList(questionPk);
            for (TestCaseDTO.SearchResponse testCase : testCaseDTOS) {
                testCaseService.delete(testCase.getPk());
            }
            questionService.deleteQuestion(user,questionPk);
            return ResponseEntity.noContent().build();
        }
        catch(NotFoundException e){
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/solve/user={userPk}")
    public ResponseEntity<List<QuestionDTO.questionPagingResponse>> getUserQuestion(@AuthenticationPrincipal User user,
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

    @GetMapping("/wrong")
    public ResponseEntity<List<QuestionDTO.questionPagingResponse>> getWrongQuestion(@AuthenticationPrincipal User user,
                                                                                     @RequestParam(defaultValue = "1") int page,
                                                                                     @RequestParam(defaultValue = "10") int pageSize){

        List<CodeDTO.PageableCodeListResponse>codes=codeService.getWrongCodes(user,page,pageSize);
        List<QuestionDTO.SearchQuestionResponse> questions=new ArrayList<>();
        for(CodeDTO.PageableCodeListResponse code: codes){
            Question question = questionService.findByPk(code.getQuestionPk());
            QuestionDTO.SearchQuestionResponse questionDTO=questionService.getByPk(question);
            questions.add(questionDTO);

        }List<QuestionDTO.questionPagingResponse> pagingQuestion = new ArrayList<>();
        if(questionService.userPagingQuestion(codes.get(0).getCnt(),questions).size()>0){
            pagingQuestion = questionService.userPagingQuestion(codes.get(0).getCnt(),questions);}

//        List<QuestionDTO.questionPagingResponse> pagingQuestion = questionService.userPagingQuestion(codes.get(0).getCnt(),questions);

        return ResponseEntity.ok(pagingQuestion);
    }



    @GetMapping("/detail/{questionPk}")
    public ResponseEntity<QuestionAndTestAndImageDTO.QuestionAndTest> detailQuestion(@PathVariable Long questionPk) {

        QuestionDTO.AddQuestionResponse question = questionService.getDetailQuestion(questionPk);
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

    @GetMapping("/search/keyword={keyword}")
    public ResponseEntity<List<QuestionDTO.SearchQuestionResponse>> getKeywordQuestion(@PathVariable String keyword){
        List<QuestionDTO.SearchQuestionResponse> question= questionService.getKeywordQuestion(keyword);
        return ResponseEntity.ok(question);
    }

    @GetMapping("/search/recent")
    public ResponseEntity<List<QuestionDTO.SearchQuestionResponse>> getRecentQuestion(){
        List<QuestionDTO.SearchQuestionResponse> questions = questionService.getRecentQuestion();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/search/")
    public ResponseEntity<QuestionDTO.SearchListResponse> getQuestions(
            @RequestParam(required = false, defaultValue = "10") @Min(value = 2, message = "page 크기는 1보다 커야합니다") int pageSize,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page는 0보다 커야합니다") int page,
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "sort", required = false) QuestionSortType sort,
            @RequestParam(value = "categoryPk", required = false) List<Long> categoryPks,
            @RequestParam(value = "levelPk", required = false) List<Long> levelPks
    ) {

        Page<QuestionDTO.SearchQuestionResponse> questions = questionService.getQuestionList(page, pageSize, q, sort, categoryPks, levelPks);
        System.out.println(sort);
        System.out.println(categoryPks);
        System.out.println(levelPks);

        QuestionDTO.SearchListResponse response = QuestionDTO.SearchListResponse.builder()
                .cnt((int) questions.getTotalElements())
                .list(questions.getContent())
                .build();

        return ResponseEntity.ok(response);
    }

    //python print('hello')\nprint('hello')
    // -> /n으로 해도 될거같음
    //자바같은 경우 String code = "public class Main{\n    public static void main(String[] args){\n    System.out.println(1);\n}\n}"; 이런식으로 전처리가 필요함 (main문 미리 줄 필요 있어보임)
    //자바같은 경우 String code = "public class Main{\n    public static void main(String[] args){\n    System.out.println(1);\n}\n}"; 이런식으로 전처리가 필요함 (main문 미리 줄 필요 있어보임)
    ///그리고 String형 같은 경우 String code = \\\"Hello World\\\" 큰따움표 사이에 \\\이렇게 세개 넣어주기
    //c++,c 마찬가지로 String code = "#include <stdio.h>\\nint main(void)\\n{\\n    printf(\\\"hello, world\\\");\\n    return 0;\\n}"; #include<studio.h>은 줘야함
    //1번 -> 파이썬 ,2번 -> 자바, 3번 ->c ,4번 ->c++
    //{"stdout":"hello World\n","time":"0.052","memory":26928,"stderr":null,"token":"42f06f39-2574-4d3e-9e90-35d33059ab14","compile_output":null,"message":null,"status":{"id":3,"description":"Accepted"}}
    //{"stdout":null,"time":"0.112","memory":33472,"stderr":"  File \"/box/script.py\", line 1\n    cHJpbnQoJ2hlbGxvIFdvcmxkJyk=\n                                ^\nSyntaxError: invalid syntax\n","token":"92e5da6a-a84d-4d83-9bc6-836f56a3258d","compile_output":null,"message":"Exited with error status 1","status":{"id":11,"description":"Runtime Error (NZEC)"}}
    //문제 풀기
    @GetMapping("/solveQuestion")
    public ResponseEntity<String> solveQuestion(
            @RequestParam(required = true) String userCode,
            @RequestParam(required = true) int languageNum,
            @RequestParam(required=true) String testcase
    ) throws IOException, InterruptedException {
        System.out.println("userCode" + userCode);
        System.out.println("languageNum" + languageNum);
        String JUDGE0_API_URL = "https://judge0-extra-ce.p.rapidapi.com";
        String RAPIDAPI_HOST = "judge0-extra-ce.p.rapidapi.com";
        String RAPIDAPI_KEY = "ce1aa40351mshd1b8e179b49a600p12e79djsn3c18e4473ece"; // -> 언니 이거이거
        String python3LanguageId = "28";
        String javaLanguageId = "4";
        String cPlusLanguageId = "2";
        String cLanguageId = "1";
        String languageNumber = "";
        if (languageNum == 1)
            languageNumber = cLanguageId;
        else if (languageNum == 2)
            languageNumber = javaLanguageId;
        else if (languageNum == 3)
            languageNumber = cPlusLanguageId;
        else if (languageNum == 4)
            languageNumber = python3LanguageId;


        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("language_id", languageNumber);
        requestBody.put("source_code", userCode);
        ArrayNode inputArrayNode = objectMapper.createArrayNode();
//        for (String input : inputs) {
//            inputArrayNode.add(input);
//        }

//        List<TestCaseDTO.SearchResponse> testCaseList= testCaseService.getTestCaseList(questionPk);
//        for(TestCaseDTO.SearchResponse testcase : testCaseList){
//            System.out.println(testcase.getInput());
//            inputs.add(testcase.getInput());
//        }
        List<String> inputs = new ArrayList<>();
        String str = testcase;

        List<String> splitList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '!' || c == '@' || c == '#' || c == '*' || c == '(' || c == ')') {
                if (sb.length() > 0) {
                    splitList.add(sb.toString());
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }

        // 마지막 구분자 이후의 문자열도 추가
        if (sb.length() > 0) {
            splitList.add(sb.toString());
        }
        System.out.println("size는"+splitList.size());
        // 결과 출력
        for (String substring : splitList) {
            System.out.println(substring);
            inputs.add(substring);
        }

        for(String input : inputs){
            System.out.println(input);
        }

        requestBody.put("stdin", String.join("\n", inputs));

        String requestBodyString = requestBody.toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(JUDGE0_API_URL + "/submissions"))
                .header("X-RapidAPI-Key", RAPIDAPI_KEY)
                .header("X-RapidAPI-Host", RAPIDAPI_HOST)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyString))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("responseBody" + response.body());
        String responseBody = response.body();
        System.out.println("responseBody" + responseBody);
        String token = "";
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            token = jsonNode.get("token").asText();
            System.out.println(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread.sleep(2000);

        HttpRequest resultRequest = HttpRequest.newBuilder()
                .uri(URI.create(JUDGE0_API_URL + "/submissions/" + token))
                .header("X-RapidAPI-Key", RAPIDAPI_KEY)
                .header("X-RapidAPI-Host", RAPIDAPI_HOST)
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> resultResponse = client.send(resultRequest, HttpResponse.BodyHandlers.ofString());
        String resultBody = resultResponse.body();
        System.out.println(resultBody);
        return ResponseEntity.ok(resultBody);
    }

    @Operation(summary = "사용자의 경험치 증가", description = """
           사용자가 문제를 맞췄을때, 문제의 레벨에 따라 경험치를 증가 시킴. 
           """)
    @PostMapping("/corret/exp")
    public ResponseEntity increaseExp(@AuthenticationPrincipal User user, @RequestParam Long questionPk) {
        questionService.increaseExp(user, questionPk);
        return ResponseEntity.ok().build();
    }


}

