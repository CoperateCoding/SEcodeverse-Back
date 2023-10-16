package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.Comment;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.dto.CommentDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.BoardRepository;
import com.coperatecoding.secodeverseback.repository.CommentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    public Comment makeComment(User user,  CommentDTO.AddCommentRequest addCommentRequest) throws RuntimeException{

        Board board =  boardRepository.findById(addCommentRequest.getBoardPK()).orElseThrow(() -> new NotFoundException("해당하는 게시글이 존재하지 않음"));

        Comment comment = Comment.makeComment(board,user,addCommentRequest.getContent());
                return commentRepository.save(comment);

    }

    public void deleteComment(Long commentID) throws RuntimeException{

        Comment comment = commentRepository.findById(commentID).orElseThrow(() -> new NotFoundException("해당하는 댓글이 존재하지 않음"));
        commentRepository.delete(comment);

    }


}
