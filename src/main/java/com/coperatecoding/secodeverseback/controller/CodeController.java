package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.question.CodeDTO;
import com.coperatecoding.secodeverseback.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "코드", description = "코드 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/code")
public class CodeController {
    private final CodeService codeService;

    @Operation(summary = "코드 제출")
    @PostMapping("/{questionPk}")
    public ResponseEntity makeCode(@AuthenticationPrincipal User user, @PathVariable Long questionPk,
                                   @RequestBody @Valid CodeDTO.AddCodeRequest addCodeRequest) {
        codeService.makeCode(user,questionPk,addCodeRequest);
        System.out.println(addCodeRequest.getCodeStatus());
        System.out.println(addCodeRequest.getCompileTime());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @Operation(summary = "달력 정보 가져오기", description = """
    [로그인 필요]<br>
    200: 성공
    403: 로그인 필요
    /<br>
    현재 년도, 월 입력받으면 맞춘 개수 보내줌.
    """)
    @GetMapping("/calendar")
    public ResponseEntity<CodeDTO.MyTrueQuestionResponseList> getCalendar(
            @AuthenticationPrincipal User user,
            @RequestParam int year,
            @RequestParam int month
    ) throws RuntimeException {
        CodeDTO.MyTrueQuestionResponseList response = codeService.getCalendar(user, year, month);
        return ResponseEntity.ok(response);
    }
}
