package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.repository.CommentRepository;
import com.smsoft.playgroundbe.domain.board.repository.PostRepository;
import com.smsoft.playgroundbe.domain.board.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReactionService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

}
