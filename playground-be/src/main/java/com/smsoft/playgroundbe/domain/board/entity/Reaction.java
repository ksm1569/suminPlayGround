package com.smsoft.playgroundbe.domain.board.entity;

import com.smsoft.playgroundbe.domain.board.common.BaseEntity;
import com.smsoft.playgroundbe.domain.board.constant.ReactionType;
import com.smsoft.playgroundbe.domain.board.dto.ReactionDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Reaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Setter
    @ManyToOne
    private Post post;

    @Setter
    @ManyToOne
    private Comment comment;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;

    public void patch(ReactionDTO.Request reactionDTO) {
        this.userId = reactionDTO.getUserId();
        this.reactionType = reactionDTO.getReactionType();
    }
}