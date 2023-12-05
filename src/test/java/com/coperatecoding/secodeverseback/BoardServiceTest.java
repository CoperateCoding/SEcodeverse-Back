package com.coperatecoding.secodeverseback;

import com.coperatecoding.secodeverseback.domain.User;
import com.coperatecoding.secodeverseback.domain.board.Board;
import com.coperatecoding.secodeverseback.domain.board.BoardCategory;
import com.coperatecoding.secodeverseback.dto.board.BoardDTO;
import com.coperatecoding.secodeverseback.repository.BoardCategoryRepository;
import com.coperatecoding.secodeverseback.repository.BoardRepository;
import com.coperatecoding.secodeverseback.repository.UserRepository;
import com.coperatecoding.secodeverseback.service.BoardService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BoardCategoryRepository boardCategoryRepository;

    @MockBean
    private BoardRepository boardRepository;

    @Test
    public void makeBoardTest() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        User user = new User();
        ReflectionTestUtils.setField(user, "pk", 1L);

        BoardDTO.AddBoardRequest request = new BoardDTO.AddBoardRequest();
        ReflectionTestUtils.setField(request, "categoryPk", 1L);
        ReflectionTestUtils.setField(request, "title", "Title");
        ReflectionTestUtils.setField(request, "content", "test");

        // Reflection을 이용한 객체 생성
        Constructor<BoardCategory> boardCategoryConstructor = BoardCategory.class.getDeclaredConstructor();
        boardCategoryConstructor.setAccessible(true);
        BoardCategory boardCategory = boardCategoryConstructor.newInstance();

        Constructor<Board> boardConstructor = Board.class.getDeclaredConstructor();
        boardConstructor.setAccessible(true);
        Board board = boardConstructor.newInstance();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(boardCategoryRepository.findById(anyLong())).thenReturn(Optional.of(boardCategory));
        when(boardRepository.save(any())).thenReturn(board);

        boardService.makeBoard(user, request);

        verify(userRepository, times(1)).findById(anyLong());
        verify(boardCategoryRepository, times(1)).findById(anyLong());
        verify(boardRepository, times(1)).save(any());

    }

    @Test
    public void deleteBoardTest() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        User user = new User();
        ReflectionTestUtils.setField(user, "pk", 1L);

        BoardCategory boardCategory = BoardCategory.makeCategory("카테고리");

        Constructor<Board> boardConstructor = Board.class.getDeclaredConstructor();
        boardConstructor.setAccessible(true);
        Board board = Board.makeBoard(user, boardCategory, "제목", "내용");
        ReflectionTestUtils.setField(board, "pk", 1L);

        when(boardCategoryRepository.findById(anyLong())).thenReturn(Optional.of(boardCategory));
        when(boardRepository.findById(any())).thenReturn(Optional.of(board));

        boardService.deleteBoard(user, 1L);

        verify(boardRepository, times(1)).findById(anyLong());
        verify(boardRepository, times(1)).delete(any());
    }

}
