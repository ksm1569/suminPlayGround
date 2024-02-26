package com.smsoft.playgroundbe.domain.board.dto;

import com.smsoft.playgroundbe.domain.board.entity.Category;
import lombok.*;

public class CategoryDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String name;
    }

    @Getter @Setter
    @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;

        public static Response of(Category category) {
            return Response.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        }
    }
}
