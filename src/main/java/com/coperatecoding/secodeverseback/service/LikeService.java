package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.Likes;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.BoardRepository;
import com.coperatecoding.secodeverseback.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LikeService {
    private final LikeRepository likeRepository;

    private final BoardRepository boardRepository;

    public void makeLikes(User user, Long boardPk) throws RuntimeException {

        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NotFoundException("해당하는 게시물이 존재하지 않음"));

        // 이전에 좋아요 눌렀는지 확인
        boolean isClicked = likeRepository.existsByUserAndBoard(user, board);
        if(!isClicked) {
            Likes likes = Likes.makeLikes(user, board);
            likeRepository.save(likes);
            board.addLikeCnt();
        }
        else {
            throw new RuntimeException();
        }

    }

    public void deleteLikes(User user, Long boardPk) throws RuntimeException{

        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NotFoundException("해당하는 게시물이 존재하지 않음"));

        Likes likes = likeRepository.findByUserAndBoard(user, board)
                .orElseThrow(() -> null);

        //좋아요가 존재하지 않는다면 무시
        if (likes == null)
            throw new RuntimeException();

        //이전에 좋아요를 눌렀다면 삭제
        likeRepository.delete(likes);
        board.deleteLikeCnt();

    }

    public boolean checkLikes(User user, Long boardPk)  throws RuntimeException
    {
        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NotFoundException("해당하는 게시물이 존재하지 않음"));

        boolean isClicked = likeRepository.existsByUserAndBoard(user, board);
        if(isClicked) {
            return true;
        }
        else {
            return false;
        }

    }

}
