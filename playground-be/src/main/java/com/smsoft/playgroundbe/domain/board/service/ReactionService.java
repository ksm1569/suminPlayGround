package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.dto.ReactionDTO;
import com.smsoft.playgroundbe.domain.board.entity.Comment;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import com.smsoft.playgroundbe.domain.board.entity.Reaction;
import com.smsoft.playgroundbe.domain.board.repository.CommentRepository;
import com.smsoft.playgroundbe.domain.board.repository.PostRepository;
import com.smsoft.playgroundbe.domain.board.repository.ReactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReactionService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReactionRepository reactionRepository;

    @Transactional
    public ReactionDTO.Response addOrUpdateReaction(ReactionDTO.Request reactionDTO) {
        Reaction reaction = Reaction.builder()
                .userId(reactionDTO.getUserId())
                .reactionType(reactionDTO.getReactionType())
                .build();

        Optional.ofNullable(reactionDTO.getPostId()).ifPresent(postId -> {
            Post findPost = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

            reaction.setPost(findPost);
        });

        Optional.ofNullable(reactionDTO.getCommentId()).ifPresent(commentId -> {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));
        });

        return ReactionDTO.Response.of(reactionRepository.save(reaction));
    }
}
