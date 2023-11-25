package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.CommentDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.service.BoardService;
import com.coperatecoding.secodeverseback.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글", description = "댓글 관련 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = """
    [로그인 필요]<br>
    200: 성공<br>
    403: 권한없음
    """)
    @PostMapping("")
    public ResponseEntity makeComment(@AuthenticationPrincipal User user, @RequestBody @Valid CommentDTO.AddCommentRequest addCommentRequest){
        commentService.makeComment(user,addCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "댓글 삭제", description = """
    [로그인 필요] 댓글 작성자, 관리자만 삭제가 가능합니다<br>
    200: 성공<br>
    403: 권한없음
    """)
    @DeleteMapping("/{commentPk}")
    public ResponseEntity deleteComment(@AuthenticationPrincipal User user, @PathVariable Long commentPk){
        try {
            commentService.deleteComment(user, commentPk);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "댓글 수정", description = """
    [로그인 필요] 댓글 작성자만 삭제가 가능합니다<br>
    200: 성공<br>
    403: 권한없음
    """)
    @PatchMapping("/{commentPk}")
    public ResponseEntity modifyComment(@AuthenticationPrincipal User user, @PathVariable Long commentPk, @RequestBody CommentDTO.modifyRequest modifyRequest){
        commentService.modifyComment(user, commentPk, modifyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "댓글 조회")
    @GetMapping("/{boardPk}")
    public ResponseEntity <List<CommentDTO.SearchResponse>> getComments(@PathVariable Long boardPk){
        List<CommentDTO.SearchResponse> comments = commentService.getComments(boardPk);
        //
//        if (comments.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글이 없음");
//        }

        return ResponseEntity.ok(comments);
    }

}
