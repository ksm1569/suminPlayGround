package com.smsoft.playgroundbe.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CategoryDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String name;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String name;
    }
}
