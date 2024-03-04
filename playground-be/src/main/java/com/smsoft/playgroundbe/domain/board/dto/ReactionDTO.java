package com.smsoft.playgroundbe.domain.board.dto;

import com.smsoft.playgroundbe.domain.board.constant.ReactionType;
import com.smsoft.playgroundbe.domain.board.entity.Reaction;
import lombok.*;

public class ReactionDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String userId;
        private ReactionType reactionType;
        private Long postId;
        private Long commentId;
    }

    @Getter @Setter
    @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private Long id;
        private String userId;
        private ReactionType reactionType;
        private Long postId;
        private Long commentId;

        public static Response of(Reaction reaction) {
            return Response.builder()
                    .id(reaction.getId())
                    .userId(reaction.getUserId())
                    .reactionType(reaction.getReactionType())
                    .postId(reaction.getId() != null ? reaction.getPost().getId() : null)
                    .commentId(reaction.getId() != null ? reaction.getComment().getId() : null)
                    .build();
        }
    }
}
