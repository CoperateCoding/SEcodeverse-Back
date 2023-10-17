package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.RoleType;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import com.coperatecoding.secodeverseback.domain.board.BoardImage;
import com.coperatecoding.secodeverseback.dto.BoardDTO;
import com.coperatecoding.secodeverseback.dto.SortType;
import com.coperatecoding.secodeverseback.exception.CategoryNotFoundException;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.BoardCategoryRepository;
import com.coperatecoding.secodeverseback.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    public void deleteBoard(User user, Long boardPk) throws NoSuchElementException, ForbiddenException {
//        Board board = findBoard(boardPk);

        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글을 찾을 수 없습니다."));


//        if (user.getRoleType() != RoleType.ADMIN && board.getUser().getPk() != user.getPk())
//            throw new ForbiddenException("권한이 없는 사용자입니다");

        boardRepository.delete(board);
    }

    private Board findBoard(Long boardPk) {
        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글을 찾을 수 없습니다"));

        return board;
    }

//    public BoardDTO.SearchListResponse getBoardList(Long categoryPk, String q, Integer page, Integer pageSize, SortType sort) {
//        BoardDTO.SearchListRequest request = BoardDTO.SearchListRequest.makeRequest(categoryPk, q, pageSize, page, sort);
//        return getBoardList(request);
//    }


    public void editBoard(User user, Long boardPk, BoardDTO.AddBoardRequest addBoardRequest) {
//        Board board = verifyWriterAndfindBoard(user, boardPk);
        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글을 찾을 수 없습니다."));
        
        BoardCategory boardCategory = boardCategoryRepository.findById(addBoardRequest.getCategoryPk())
                        .orElseThrow(() -> new NoSuchElementException("해당하는 게시글의 카테고리를 찾을 수 없습니다."));

        board.edit(
                boardCategory,
                addBoardRequest.getTitle(),
                addBoardRequest.getContent()
        );

        boardRepository.save(board);
    }

    private Pageable makePageable(SortType sortType, Integer page, Integer pageSize) throws RuntimeException {

        Sort sort;
        if (sortType == null || sortType == SortType.POP) {
            sort = Sort.by(Sort.Direction.DESC, "likeCnt");
        }
        else {
            sort = Sort.by(Sort.Direction.DESC, "updateAt");
        }

        if (page == null)
            page = 1;

        if (pageSize == null)
            pageSize = 10;

        return PageRequest.of(page-1, pageSize, sort);
    }

    private Board verifyWriterAndfindBoard(User user, Long boardPk) {
        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글이 없습니다"));

        //글 작성자거나, admin이 아니라면 수정 불가능
        if (user.getRoleType() != RoleType.ADMIN && board.getUser() != user)
            throw new ForbiddenException("권한이 없는 사용자");
        return board;
    }

//    private List<BoardImage> getBoardImage(Board board, List<BoardImage> imageList) {
//
//
//
//    }

//    public BoardDTO.SearchListResponse getBoardList(Long categoryPk, String q, int page, int pageSize, SortType sort) {
//    }
}
