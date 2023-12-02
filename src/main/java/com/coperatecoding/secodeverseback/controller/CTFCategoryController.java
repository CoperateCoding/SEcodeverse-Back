package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.ctf.CTFCategory;
import com.coperatecoding.secodeverseback.service.CTFCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "CTF 카테고리", description = "CTF 카테고리 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/ctf/category")
public class CTFCategoryController {

    private final CTFCategoryService ctfCategoryService;

    @Operation(summary = "ctf 카테고리 조회")
    @GetMapping("/all")
    public ResponseEntity<List<CTFCategory>> getCTFCategoryAll(@AuthenticationPrincipal User user)
            throws RuntimeException {

        List<CTFCategory> ctfCategoryList = ctfCategoryService.getCTFCategoryAll();

        return ResponseEntity.ok(ctfCategoryList);

    }

    @Operation(summary = "ctf 문제 카테고리 조회")
    @GetMapping("/{categoryName}")
    public ResponseEntity<Long> getCategoryPk(@AuthenticationPrincipal User user
            , @PathVariable String categoryName)
        throws RuntimeException {

        Long categoryPk = ctfCategoryService.getCategoryPk(categoryName);

        return ResponseEntity.ok(categoryPk);
    }
}
