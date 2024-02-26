package com.smsoft.playgroundbe.domain.board.repository;

import com.smsoft.playgroundbe.domain.board.constant.ReactionType;
import com.smsoft.playgroundbe.domain.board.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    int countByPostIdAndReactionType(Long postId, ReactionType reactionType);
    int countByCommentIdAndReactionType(Long commentId, ReactionType reactionType);
}
