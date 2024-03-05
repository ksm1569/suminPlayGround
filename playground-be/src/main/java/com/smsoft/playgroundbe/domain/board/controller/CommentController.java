package com.smsoft.playgroundbe.domain.board.controller;

import com.smsoft.playgroundbe.domain.board.dto.CommentDTO;
import com.smsoft.playgroundbe.domain.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO.Response> createComment(@RequestBody CommentDTO.Request commentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.createComment(commentDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO.Response> updateComment(
            @PathVariable Long id,
            @RequestBody CommentDTO.Request commentDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.updateComment(id, commentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO.Response>> getAllComments() {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllComments());
    }
}
