package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.Likes;
import com.coperatecoding.secodeverseback.dto.LikesDTO;
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

    public Likes makeLikes(User user, Long boardPk) throws RuntimeException{

        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NotFoundException("해당하는 게시물이 존재하지 않음"));

        Likes likes = Likes.makeLikes(user,board);

        return likeRepository.save(likes);
    }

    public void deleteLikes(Long likePk) throws RuntimeException{
        Likes likes = likeRepository.findById(likePk)
                .orElseThrow(() -> new NotFoundException("해당 좋아요가 존재하지 않음"));
        likeRepository.delete(likes);
    }
}
