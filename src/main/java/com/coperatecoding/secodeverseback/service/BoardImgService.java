package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.BoardImage;
import com.coperatecoding.secodeverseback.dto.BoardImgDTO;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.BoardImageRepository;
import com.coperatecoding.secodeverseback.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardImgService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;

    public BoardImage makeBoardImage(Long boardPk, BoardImgDTO.AddBoardImgRequest addBoardImgRequest) throws RuntimeException {
        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NotFoundException("해당하는 게시글이 존재하지 않음"));

        BoardImage boardImage = BoardImage.makeBoardImage(board, addBoardImgRequest.getImgUrl());

        return boardImageRepository.save(boardImage);
    }

    public BoardImgDTO.SearchResponse getBoardImg(BoardImgDTO.SearchRequest request) {
        BoardImgDTO.SearchResponse response = BoardImgDTO.SearchResponse.makeResponse(
                request.getPk(),
                request.getImgUrl());

        return response;
    }

    public List<BoardImgDTO.SearchResponse> getBoardImg(Long boardPk) {
        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NotFoundException("해당하는 게시글이 존재하지 않음"));

        List<BoardImage> boardImageList = boardImageRepository.findByBoard(board);
        List<BoardImgDTO.SearchResponse> boardImgDTOS = new ArrayList<>();

        for (BoardImage boardImage: boardImageList) {
            BoardImgDTO.SearchRequest request = BoardImgDTO.SearchRequest.makeRequest(
                    boardImage.getPk(),
                    boardImage.getImgUrl()
            );

            BoardImgDTO.SearchResponse response = getBoardImg(request);

            BoardImgDTO.SearchResponse boardImgDTO = BoardImgDTO.SearchResponse.makeResponse(
                    response.getPk(),
                    response.getImgUrl());
            boardImgDTOS.add(boardImgDTO);
        }

        return boardImgDTOS;
    }



    public void delete(Long boardImgPk) throws RuntimeException {
        BoardImage boardImage = boardImageRepository.findById(boardImgPk)
                .orElseThrow(() -> new NotFoundException("해당하는 게시글 이미지가 존재하지 않음"));

        boardImageRepository.delete(boardImage);
    }


}
