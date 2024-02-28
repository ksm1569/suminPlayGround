package com.smsoft.playgroundbe.domain.board.controller;

import com.smsoft.playgroundbe.domain.board.dto.BoardDTO;
import com.smsoft.playgroundbe.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardDTO.Response> createBoard(@RequestBody BoardDTO.Request boardDTO) {
        return new ResponseEntity<>(boardService.createBoard(boardDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO.Response> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardDTO.Request boardDTO
    ) {
        return ResponseEntity.ok(boardService.updateBoard(id, boardDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<BoardDTO.Response>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }
}
