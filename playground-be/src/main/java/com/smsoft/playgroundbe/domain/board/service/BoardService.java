package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.dto.BoardDTO;
import com.smsoft.playgroundbe.domain.board.entity.Board;
import com.smsoft.playgroundbe.domain.board.entity.Category;
import com.smsoft.playgroundbe.domain.board.repository.BoardRepository;
import com.smsoft.playgroundbe.domain.board.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardDTO.Response createBoard(BoardDTO.Request boardDTO) {
        Category category = categoryRepository.findById(boardDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id : " + boardDTO.getCategoryId()));

        Board board = Board.builder()
                .title(boardDTO.getTitle())
                .category(category)
                .build();

        Board savedBoard = boardRepository.save(board);

        return BoardDTO.Response.of(savedBoard);
    }

    @Transactional
    public BoardDTO.Response updateBoard(Long id, BoardDTO.Request boardDTO) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id : " + id ));

        board.patch(boardDTO);

        return BoardDTO.Response.of(boardRepository.save(board));
    }

    @Transactional
    public void deleteBoard(Long id) {
        Board findBoard = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id : " + id));

        boardRepository.delete(findBoard);
    }

    @Transactional
    public List<BoardDTO.Response> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(BoardDTO.Response::of)
                .collect(Collectors.toList());
    }
}
