package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.Comment;
import com.coperatecoding.secodeverseback.domain.RoleType;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.dto.CommentDTO;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.BoardRepository;
import com.coperatecoding.secodeverseback.repository.CommentRepository;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;


    public Comment makeComment(User user, CommentDTO.AddCommentRequest addCommentRequest) throws RuntimeException{

        Board board =  boardRepository.findById(addCommentRequest.getBoardPK())
                .orElseThrow(() -> new NotFoundException("해당하는 게시글이 존재하지 않음"));

        Comment comment = Comment.makeComment(board, user, addCommentRequest.getContent());
        board.addCommentCnt();

        return commentRepository.save(comment);

    }

    public void deleteComment(User user, Long commentID) throws RuntimeException{

        Comment comment = commentRepository.findById(commentID).orElseThrow(() -> new NotFoundException("해당하는 댓글이 존재하지 않음"));
        Board board = comment.getBoard();
        board.deleteCommentCnt();

        // Admin은 통과, 회원이면 댓글 확인
        if(!user.getRoleType().equals(RoleType.ADMIN) && comment.getUser().getPk() != user.getPk()) {
            throw new ForbiddenException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);

    }

    public void modifyComment(User user, Long commentPk, CommentDTO.modifyRequest modifyRequest) throws RuntimeException{
        Comment comment = commentRepository.findById(commentPk).orElseThrow(() -> new NotFoundException("해당하는 댓글이 존재하지 않음"));

        // Admin은 통과, 회원이면 댓글 확인
        if(!user.getRoleType().equals(RoleType.ADMIN) && comment.getUser().getPk() != user.getPk()) {
            throw new ForbiddenException("작성자만 댓글을 삭제할 수 있습니다.");
        }

        comment.modifyComment(modifyRequest.getContent());

    }

    public List<CommentDTO.SearchResponse> getComments(Long boardPk){
        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NotFoundException("해당하는 게시글이 존재하지 않음"));

        List<Comment> comments = commentRepository.findByBoardOrderByCreateAtDesc(board);

        List<CommentDTO.SearchResponse> commentResponses = comments.stream().map(comment -> {
            User user = comment.getUser();

            return CommentDTO.SearchResponse.builder()
                    .pk(comment.getPk())
                    .createAt(comment.getCreateAt())
                    .content(comment.getContent())
                    .nickname(user.getNickname())
                    .build();
        }).collect(Collectors.toList());


        return commentResponses;

    }


}
