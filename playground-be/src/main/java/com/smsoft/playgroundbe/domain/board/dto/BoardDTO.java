package com.smsoft.playgroundbe.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class BoardDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String title;
        private Long categoryId;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String categoryName;
    }
}
