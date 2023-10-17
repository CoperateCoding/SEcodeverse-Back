package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.LikesDTO;
import com.coperatecoding.secodeverseback.service.BoardService;
import com.coperatecoding.secodeverseback.service.LikeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "좋아요", description = "좋아요 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/likes")
public class LikesController {
    private final LikeService likeService;
    private final BoardService boardService;
    @PostMapping("/{boardPk}")
    public ResponseEntity makeLikes(@AuthenticationPrincipal User user, @PathVariable Long boardPk){
        likeService.makeLikes(user,boardPk);
        boardService.makeLike(boardPk);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }
    @DeleteMapping("/{boardPk}")
    public ResponseEntity deleteLikes(@PathVariable Long boardPk){
        likeService.deleteLikes(boardPk);
        boardService.deleteLike(boardPk);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
