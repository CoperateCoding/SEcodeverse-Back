package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import com.coperatecoding.secodeverseback.domain.board.BoardImage;
import com.coperatecoding.secodeverseback.dto.BoardDTO;
import com.coperatecoding.secodeverseback.dto.SortType;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.BoardCategoryRepository;
import com.coperatecoding.secodeverseback.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {
    private final BoardRepository boardRepository;

    private final BoardCategoryRepository boardCategoryRepository;

    public Board makeBoard(User user, BoardDTO.AddBoardRequest addBoardRequest) throws RuntimeException {

        // 카테고리 올바른지 확인
        BoardCategory category = boardCategoryRepository.findById(addBoardRequest.getCategoryPk())
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));


        Board board = Board.makeBoard(user, category, addBoardRequest.getTitle(), addBoardRequest.getContent());

//        List<BoardImage> boardImageList = getBoardImage(board, addBoardRequest.getImageList());
//
//        board.setBoardImage(boardImageList);

        return boardRepository.save(board);

    }

    public void makeLike(Long boardPk) throws RuntimeException{

        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));
       board.addLikeCnt();

    }

    public void deleteLike(Long boardPk) throws RuntimeException{
        Board board =boardRepository.findById(boardPk)
                        .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));
        board.deleteLikeCnt();
    }

//    private List<BoardImage> getBoardImage(Board board, List<BoardImage> imageList) {
//
//
//
//    }

//    public BoardDTO.SearchListResponse getBoardList(Long categoryPk, String q, int page, int pageSize, SortType sort) {
//    }
}
