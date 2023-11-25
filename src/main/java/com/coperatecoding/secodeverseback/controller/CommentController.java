package com.coperatecoding.secodeverseback.controller;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.dto.CommentDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.service.CommentService;
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

    @PostMapping("")
    public ResponseEntity makeComment(@AuthenticationPrincipal User user, @RequestBody @Valid CommentDTO.AddCommentRequest addCommentRequest){
        commentService.makeComment(user,addCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{commentPk}")
    public ResponseEntity deleteComment(@PathVariable Long commentPk){
        try {
            commentService.deleteComment(commentPk);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{commentPk}")
    public ResponseEntity modifyComment(@PathVariable Long commentPk, @RequestBody CommentDTO.modifyRequest modifyRequest){
        commentService.modifyComment(commentPk,modifyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{boardPk}")
    public ResponseEntity <List<CommentDTO.SearchResponse>>getComments(@PathVariable Long boardPk){
        System.out.println("댓글 등록 시작함");
        List<CommentDTO.SearchResponse> comments = commentService.getComments(boardPk);
        System.out.println(comments.size());
        //
//        if (comments.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글이 없음");
//        }

        return ResponseEntity.ok(comments);
    }

}
