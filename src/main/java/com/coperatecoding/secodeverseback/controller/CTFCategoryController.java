package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.CTFCategory;
import com.coperatecoding.secodeverseback.dto.ctf.CTFCategoryDTO;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.service.CTFCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "CTF 카테고리", description = "CTF 카테고리 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/ctf/category")
public class CTFCategoryController {

    private final CTFCategoryService ctfCategoryService;

    @Operation(summary = "ctf 카테고리 조회")
    @GetMapping("/all")
    public ResponseEntity<List<CTFCategoryDTO>> getCTFCategoryAll(@AuthenticationPrincipal User user
            , @RequestParam Long leaguePk
    ) throws ForbiddenException {

        List<CTFCategoryDTO> ctfCategoryList = ctfCategoryService.getCTFCategoryAll(user, leaguePk);

        return ResponseEntity.ok(ctfCategoryList);
    }

    @Operation(summary = "ctf 카테고리별로 문제 풀이 완료 여부")
    @GetMapping("/all/isSolved")
    public ResponseEntity<Map<Long, Boolean>> allQuestionsSolvedByCategory(@AuthenticationPrincipal User user
            , @RequestParam Long leaguePk
    ) throws ForbiddenException {
        // 각 카테고리별로 모든 문제가 풀렸는지 검사합니다.
        Map<Long, Boolean> categorySolvedMap = ctfCategoryService.allQuestionsSolvedByCategory(user, leaguePk);

        return ResponseEntity.ok(categorySolvedMap);
    }

    @Operation(summary = "ctf 문제 카테고리 이름으로 카테고리pk 얻기")
    @GetMapping("/{categoryName}")
    public ResponseEntity<Long> getCategoryPk(@AuthenticationPrincipal User user
            , @PathVariable String categoryName
    ) throws ForbiddenException {

        Long categoryPk = ctfCategoryService.getCategoryPk(categoryName);

        return ResponseEntity.ok(categoryPk);
    }
}
