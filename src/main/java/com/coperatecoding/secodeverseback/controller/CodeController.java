package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.dto.BoardAndImageDTO;
import com.coperatecoding.secodeverseback.dto.BoardImgDTO;
import com.coperatecoding.secodeverseback.dto.CodeDTO;
import com.coperatecoding.secodeverseback.service.CodeService;
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

    @PostMapping("/{questionPk}")
    public ResponseEntity makeCode(@AuthenticationPrincipal User user,@PathVariable Long questionPk, @RequestBody @Valid CodeDTO.addCodeRequest addCodeRequest) {
     codeService.makeCode(user,questionPk,addCodeRequest);
        System.out.println(addCodeRequest.getCodeStatus());
        System.out.println(addCodeRequest.getCompileTime());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
