package com.smsoft.playgroundbe.domain.board.dto;

import com.smsoft.playgroundbe.domain.board.constant.ReactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ReactionDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String userId;
        private ReactionType reactionType;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class Response {
        private String userId;
        private ReactionType reactionType;
    }
}
