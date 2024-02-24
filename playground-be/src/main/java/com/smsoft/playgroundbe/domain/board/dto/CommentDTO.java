package com.smsoft.playgroundbe.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CommentDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String authorId;
        private String content;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class Response {
        private String authorId;
        private String content;
    }
}
