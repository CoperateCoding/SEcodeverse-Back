package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.CTFQuestionType;
import com.coperatecoding.secodeverseback.dto.ctf.CTFLeagueDTO;
import com.coperatecoding.secodeverseback.dto.ctf.CTFQuestionDTO;
import com.coperatecoding.secodeverseback.dto.ctf.CTFQuestionSortType;
import com.coperatecoding.secodeverseback.service.CTFQuestionService;
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

import java.util.HashMap;
import java.util.Map;

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

    @Operation(summary = "ctf 문제 리스트 조회")
    @GetMapping("/ctf/question/")
    public ResponseEntity<CTFQuestionDTO.AllListResponse> getCTFQuestionList(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) Long categoryPk,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 2, message = "page 크기는 1보다 커야합니다") int pageSize,
            @RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "page는 0보다 커야합니다") int page,
            @RequestParam(required = false, defaultValue = "HIGH") CTFQuestionSortType sort
    ) throws RuntimeException {

        Page<CTFQuestionDTO.BriefResponse> briefResponses = ctfQuestionService.getCTFQuestionAll(user, categoryPk, page, pageSize, sort);

        CTFQuestionDTO.AllListResponse response = CTFQuestionDTO.AllListResponse.builder()
                .cnt((int) briefResponses.getTotalElements())
                .list(briefResponses.getContent())
                .build();

        return ResponseEntity.ok(response);

    }

    @Operation(summary = "ctf 문제 상세 조회")
    @GetMapping("/ctf/question/{ctfQuestionPk}")
    public ResponseEntity<CTFQuestionDTO.DetailResponse> getCTFQuestion(@PathVariable Long ctfQuestionPk) throws RuntimeException {
        CTFQuestionDTO.DetailResponse response = ctfQuestionService.getDetailCTFQuestion(ctfQuestionPk);

        return ResponseEntity.ok(response);

    }

    @Operation(summary = "ctf 문제 풀기")
    @PostMapping("/ctf/question/{ctfQuestionPk}/solve")
    public ResponseEntity<Map> solveCTFQuestion(@AuthenticationPrincipal User user,
                                                @PathVariable Long ctfQuestionPk,
                                                @RequestBody CTFQuestionDTO.SolveRequest request) throws RuntimeException {
        Map<String, Boolean> map = new HashMap<>();
        map.put("isTrue", ctfQuestionService.solveCTFQuestion(user, ctfQuestionPk, request));
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Operation(summary = "ctf 문제 수정")
    @PatchMapping("/admin/ctf/question/{ctfQuestionPk}")
    public ResponseEntity editCTFQuestion(@AuthenticationPrincipal User user, @PathVariable Long ctfQuestionPk,
                                           @RequestBody @Valid CTFQuestionDTO.EditRequest request) throws RuntimeException {
        ctfQuestionService.editRequest(ctfQuestionPk, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "ctf 문제 삭제")
    @DeleteMapping("/admin/ctf/question/{ctfQuestionPk}")
    public ResponseEntity deleteCTFQuestion(@AuthenticationPrincipal User user, @PathVariable Long ctfQuestionPk) throws RuntimeException {
        ctfQuestionService.deleteCTFQouestion(ctfQuestionPk);
        return ResponseEntity.ok().build();
    }

}

