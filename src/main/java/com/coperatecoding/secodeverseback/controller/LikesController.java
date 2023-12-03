package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "좋아요", description = """
    [로그인 필요] 좋아요<br>
    200: 성공<br>
    403: 권한없음
    500: 이미 좋아요 등록함
    """)
    @PostMapping("/{boardPk}")
    public ResponseEntity makeLikes(@AuthenticationPrincipal User user, @PathVariable Long boardPk){

        likeService.makeLikes(user, boardPk);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "좋아요 취소", description = """
    [로그인 필요] 좋아요 취소 <br>
    200: 성공<br>
    403: 권한없음
    500: 좋아요 등록이 안되어있음.
    """)
    @DeleteMapping("/{boardPk}")
    public ResponseEntity deleteLikes(@AuthenticationPrincipal User user, @PathVariable  Long boardPk){

        likeService.deleteLikes(user, boardPk);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "좋아요 여부 확인", description = """
    [로그인 필요] 좋아요 여부 확인 <br>
    200: 성공<br>
    403: 권한없음
    true: 좋아요 등록되어 있음.
    false: 좋아요 미등록
    """)
    @GetMapping("/{boardPk}")
    public ResponseEntity checkLikes(@AuthenticationPrincipal User user, @PathVariable  Long boardPk){

        boolean check = likeService.checkLikes(user, boardPk);

        return ResponseEntity.ok(check);
    }

}
