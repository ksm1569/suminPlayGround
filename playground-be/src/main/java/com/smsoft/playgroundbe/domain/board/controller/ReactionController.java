package com.smsoft.playgroundbe.domain.board.controller;

import com.smsoft.playgroundbe.domain.board.dto.ReactionDTO;
import com.smsoft.playgroundbe.domain.board.entity.Reaction;
import com.smsoft.playgroundbe.domain.board.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/reactions")
@RestController
public class ReactionController {
    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<ReactionDTO.Response> addorUpdateReation(@RequestBody ReactionDTO.Request reactionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reactionService.addOrUpdateReaction(reactionDTO));
    }
}
