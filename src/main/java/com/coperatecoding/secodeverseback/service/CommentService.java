package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.Comment;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.dto.CommentDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.BoardRepository;
import com.coperatecoding.secodeverseback.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public void modifyComment(Long commentPk, String content) throws RuntimeException{
        Comment comment = commentRepository.findById(commentPk).orElseThrow(() -> new NotFoundException("해당하는 댓글이 존재하지 않음"));
        comment.modifyComment(content);

    }

    public List<Comment> getComments(Long boardPk){
        Board board = boardRepository.findById(boardPk).orElseThrow(() -> new NotFoundException("해당하는 게시글이 존재하지 않음"));;
        List<Comment>comments=commentRepository.findAllById(Collections.singleton(board.getPk()));
        return comments;

    }

}
