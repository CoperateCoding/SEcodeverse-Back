package com.coperatecoding.secodeverseback.service;

import com.coperatecoding.secodeverseback.domain.CodingBadge;
import com.coperatecoding.secodeverseback.domain.RoleType;
import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import com.coperatecoding.secodeverseback.dto.board.BoardDTO;
import com.coperatecoding.secodeverseback.dto.board.BoardSortType;
import com.coperatecoding.secodeverseback.exception.CategoryNotFoundException;
import com.coperatecoding.secodeverseback.exception.ForbiddenException;
import com.coperatecoding.secodeverseback.exception.NotFoundException;
import com.coperatecoding.secodeverseback.repository.BoardCategoryRepository;
import com.coperatecoding.secodeverseback.repository.BoardRepository;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public final UserRepository userRepository;

    public Board makeBoard(User user, BoardDTO.AddBoardRequest addBoardRequest) throws RuntimeException {

        User findUser = userRepository.findById(user.getPk())
                .orElseThrow(() -> new NotFoundException("해당하는 사용자가 없습니다."));

        System.out.println("여기 나오는지 "+findUser.getNickname());


        // 카테고리 올바른지 확인
        System.out.println(addBoardRequest.getCategoryPk());
        System.out.println(addBoardRequest.getTitle());
        System.out.println(addBoardRequest.getContent());
        BoardCategory category = boardCategoryRepository.findById(addBoardRequest.getCategoryPk())
                .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));


        Board board = Board.makeBoard(findUser, category, addBoardRequest.getTitle(), addBoardRequest.getContent());

        return boardRepository.save(board);
    }


    public void deleteBoard(User user, Long boardPk) throws NoSuchElementException, ForbiddenException {

        Board board = verifyWriterAndfindBoard(user, boardPk);

        boardRepository.delete(board);
    }

    public void editBoard(User user, Long boardPk, BoardDTO.AddBoardRequest addBoardRequest) {

        Board board = verifyWriterAndfindBoard(user, boardPk);

        BoardCategory boardCategory = boardCategoryRepository.findById(addBoardRequest.getCategoryPk())
                        .orElseThrow(() -> new NoSuchElementException("해당하는 게시글의 카테고리를 찾을 수 없습니다."));

        board.edit(
                boardCategory,
                addBoardRequest.getTitle(),
                addBoardRequest.getContent()
        );

        boardRepository.save(board);
    }


    private Pageable makePageable(BoardSortType sortType, Integer page, Integer pageSize) throws RuntimeException {

        Sort.Order defaultOrder = new Sort.Order(Sort.Direction.DESC, "createAt");
        Sort sort;
        if (sortType == BoardSortType.POP) {
            sort = Sort.by(Sort.Direction.DESC, "likeCnt")
                    .and(Sort.by(Sort.Direction.DESC, "commentCnt"))
                    .and(Sort.by(defaultOrder));
        }
        else if (sortType == BoardSortType.COMMENT) {
            sort = Sort.by(Sort.Direction.DESC, "commentCnt")
                    .and(Sort.by(Sort.Direction.DESC, "likeCnt"))
                    .and(Sort.by(defaultOrder));
        }
        else {
            sort = Sort.by(defaultOrder);
        }

        int pageNumber = page != null && page > 0 ? page - 1 : 0;
        int size = pageSize != null && pageSize > 1 ? pageSize : 10;

        return PageRequest.of(pageNumber, size, sort);
    }

    private Pageable makePageable(Integer page, Integer pageSize) throws RuntimeException {

        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");
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
        if (user.getRoleType() != RoleType.ADMIN && board.getUser().getPk() != user.getPk())
            throw new ForbiddenException("권한이 없는 사용자");

        return board;
    }

    public void deleteLike(Long boardPk) throws RuntimeException {
        Board board =boardRepository.findById(boardPk)
                        .orElseThrow(() -> new NotFoundException("해당하는 카테고리가 존재하지 않음"));

        board.deleteLikeCnt();
    }

    public Page<BoardDTO.SearchResponse> getBoardList(Long categoryPk, String q, int page, int pageSize, BoardSortType sort
    ) throws CategoryNotFoundException {
        Pageable pageable = makePageable(sort, page, pageSize);
        Page<Board> list;

        //선택한 카테고리, 검색어가 존재한다면 해당 카테고리에 해당하는 검색어와 일치하는 글을 조회
        if (categoryPk != null && q != null) {
            BoardCategory category = boardCategoryRepository.findById(categoryPk)
                    .orElseThrow(() -> new CategoryNotFoundException("해당하는 카테고리가 없습니다"));
            list = boardRepository.findByCategoryAndTitleOrContentContaining(category, q, pageable);
        } else if (categoryPk != null) { //카테고리만 지정된 경우
            BoardCategory category = boardCategoryRepository.findById(categoryPk)
                    .orElseThrow(() -> new CategoryNotFoundException("해당하는 카테고리가 없습니다"));
            list = boardRepository.findByCategory(category, pageable);
        } else if (q != null) { //검색어만 지정된 경우
            list = boardRepository.findByTitleOrContentContaining(q, pageable);
        } else { //아무것도 지정 x -> 그냥 줌
            list = boardRepository.findAll(pageable);
        }

        List<BoardDTO.SearchResponse> searchList = list.getContent().stream()
                .map(board -> BoardDTO.SearchResponse.builder()
                        .pk(board.getPk())
                        .writerNickname(board.getUser().getNickname())
                        .badgeImgUrl(board.getUser().getBadge().getImgUrl())
                        .title(board.getTitle())
                        .preview(board.getPreview())
                        .likeCnt(board.getLikeCnt())
                        .commentCnt(board.getCommentCnt())
                        .createAt(board.convertPreviewDate(board.getCreateAt()))
                        .build()
                )
                .collect(Collectors.toList());

        return new PageImpl<>(searchList, pageable, list.getTotalElements());
    }

    @Transactional(readOnly = true)
    public Page<BoardDTO.SearchResponse> getMyBoardList(User user, int page, int pageSize) throws RuntimeException {
        Pageable pageable = makePageable(page, pageSize);
        Page<Board> boardList = boardRepository.findByUser(user, pageable);
        CodingBadge codingBadge = user.getBadge();
        List<BoardDTO.SearchResponse> boardResponses = boardList.getContent().stream()
                .map(board -> BoardDTO.SearchResponse.builder()
                        .pk(board.getPk())
                        .writerNickname(board.getUser().getNickname())
                        .badgeImgUrl(codingBadge.getImgUrl())
                        .title(board.getTitle())
                        .preview(board.getPreview())
                        .likeCnt(board.getLikeCnt())
                        .commentCnt(board.getCommentCnt())
                        .createAt(board.convertPreviewDate(board.getCreateAt()))
                        .build())
                .collect(Collectors.toList());


        return new PageImpl<>(boardResponses, pageable, boardList.getTotalElements());
    }


    @Transactional(readOnly = true)
    public List<BoardDTO.PopularBoardResponse> getPopularBoardList() throws RuntimeException {

        Pageable pageable = makePageable(BoardSortType.POP, 1, 7); // 상위 인기 게시글 10개 가져옴
        Page<Board> list = boardRepository.findAll(pageable);

        List<BoardDTO.PopularBoardResponse> boardResponses = list.getContent().stream()
                .map(board -> BoardDTO.PopularBoardResponse.builder()
                        .boardPk(board.getPk())
                        .title(board.getTitle())
                        .likeCnt(board.getLikeCnt())
                        .commentCnt(board.getCommentCnt())
                        .createAt(board.convertPreviewDate(board.getCreateAt()))
                        .build())
                .collect(Collectors.toList());

        return boardResponses;
    }

    public BoardDTO.BoardDetailResponse getDetailBoard(Long boardPk) throws NoSuchElementException {
        Board board = boardRepository.findById(boardPk)
                .orElseThrow(() -> new NotFoundException("해당하는 게시글이 존재하지 않음"));

        User writer = board.getUser();

        BoardDTO.BoardDetailResponse boardDetailResponse = BoardDTO.BoardDetailResponse.builder()
                .pk(board.getPk())
                .writer(writer.getNickname())
                .profileUrl(writer.getBadge().getImgUrl())
                .category(board.getCategory())
                .createAt(board.convertDate(board.getCreateAt()))
                .updateAt(board.convertDate(board.getUpdateAt()))
                .likeCnt(board.getLikeCnt())
                .commentCnt(board.getCommentCnt())
                .title(board.getTitle())
                .content(board.getContent())
                .build();

        return boardDetailResponse;
    }

}
