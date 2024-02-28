package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.dto.BoardDTO;
import com.smsoft.playgroundbe.domain.board.entity.Board;
import com.smsoft.playgroundbe.domain.board.entity.Category;
import com.smsoft.playgroundbe.domain.board.repository.BoardRepository;
import com.smsoft.playgroundbe.domain.board.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class BoardServiceTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("게시판 생성 - 성공 테스트")
    public void createBoardSuccess() {
        // Given
        BoardDTO.Request boardDTO = new BoardDTO.Request();
        boardDTO.setTitle("축구");
        boardDTO.setCategoryId(1L);

        Category mockCategory = new Category(1L, "스포츠", new ArrayList<>());
        Board mockBoard = new Board(1L, "축구", mockCategory, new ArrayList<>());

        given(categoryRepository.findById(boardDTO.getCategoryId())).willReturn(Optional.of(mockCategory));
        given(boardRepository.save(any(Board.class))).willReturn(mockBoard);

        // When
        BoardDTO.Response responseDTO = boardService.createBoard(boardDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals(boardDTO.getTitle(), responseDTO.getTitle());
        assertEquals(mockCategory.getName(), responseDTO.getCategoryName());
    }

    @Test
    @DisplayName("게시판 생성 - 실패 테스트")
    public void createBoardNotFound() {
        // Given
        BoardDTO.Request boardDTO = new BoardDTO.Request();
        boardDTO.setTitle("축구");
        boardDTO.setCategoryId(1L);

        given(categoryRepository.findById(boardDTO.getCategoryId())).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            boardService.createBoard(boardDTO);
        });

        assertEquals("Category not found with id : " + boardDTO.getCategoryId(), exception.getMessage());
    }

    @Test
    @DisplayName("게시판 수정 - 성공 테스트")
    public void updateBoardSuccess() {
        // Given
        Long boardId = 1L;
        BoardDTO.Request boardDTO = new BoardDTO.Request();
        boardDTO.setTitle("농구");
        boardDTO.setCategoryId(1L);

        Category mockCategory = new Category(1L, "스포츠", new ArrayList<>());
        Board existingBoard = new Board(1L, "축구", mockCategory, new ArrayList<>());

        given(boardRepository.findById(boardId)).willReturn(Optional.of(existingBoard));
        given(boardRepository.save(any(Board.class))).willReturn(existingBoard);

        // When
        BoardDTO.Response response = boardService.updateBoard(boardId, boardDTO);

        // Then
        assertNotNull(response);
        assertEquals(boardDTO.getTitle(), response.getTitle());
    }

    @Test
    @DisplayName("게시판 수정 - 실패 테스트")
    public void updateBoardNotFound() {
        // Given
        Long boardId = 1L;
        BoardDTO.Request boardDTO = new BoardDTO.Request();
        boardDTO.setTitle("농구");

        given(boardRepository.findById(boardId)).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            boardService.updateBoard(boardId, boardDTO);
        });

        assertEquals("Board not found with id : " + boardId, exception.getMessage());
    }

    @Test
    @DisplayName("게시판 삭제 - 성공 테스트")
    public void deleteBoardSuccess() {
        // Given
        Long boardId = 1L;
        Category mockCategory = new Category(1L, "스포츠", new ArrayList<>());
        Board existingBoard = new Board(1L, "축구", mockCategory, new ArrayList<>());

        given(boardRepository.findById(boardId)).willReturn(Optional.of(existingBoard));

        // When
        boardService.deleteBoard(boardId);

        // Then
        verify(boardRepository, times(1)).delete(existingBoard);
    }

    @Test
    @DisplayName("게시판 삭제 - 실패 테스트")
    public void deleteBoardNotFound() {
        // Given
        Long boardId = 1L;
        given(boardRepository.findById(boardId)).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            boardService.deleteBoard(boardId);
        });

        assertEquals("Board not found with id : " + boardId, exception.getMessage());
    }

    @Test
    @DisplayName("모든 게시판 조회 - 성공 테스트")
    public void getAllBoardsSuccess() {
        // Given
        Category mockCategory = new Category(1L, "스포츠", new ArrayList<>());
        List<Board> boards = new ArrayList<>();
        boards.add(new Board(1L, "농구", mockCategory, new ArrayList<>()));
        boards.add(new Board(2L, "축구", mockCategory, new ArrayList<>()));
        boards.add(new Board(3L, "야구", mockCategory, new ArrayList<>()));

        given(boardRepository.findAll()).willReturn(boards);

        // When
        List<BoardDTO.Response> responses = boardService.getAllBoards();

        // Then
        assertEquals(3, responses.size());
        assertEquals("농구", responses.get(0).getTitle());
        assertEquals("축구", responses.get(1).getTitle());
        assertEquals("야구", responses.get(2).getTitle());
    }
}