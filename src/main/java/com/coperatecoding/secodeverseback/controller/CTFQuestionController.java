package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.ctf.CTFQuestionDTO;
import com.coperatecoding.secodeverseback.service.CTFQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CTF문제", description = "CTF 문제 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1")
public class CTFQuestionController {
    private final CTFQuestionService ctfQuestionService;

    @Operation(summary = "ctf 문제 등록")
    @PostMapping("/admin/ctf/question")
    public ResponseEntity makeCTFQuestion(@AuthenticationPrincipal User user, @RequestBody @Valid CTFQuestionDTO.PostRequest request) throws RuntimeException {
        System.out.println("leaguePk:"  + request.getLeaguePk());
        System.out.println("categoryPk: " + request.getCategoryPk());
        ctfQuestionService.makeQuestion(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
