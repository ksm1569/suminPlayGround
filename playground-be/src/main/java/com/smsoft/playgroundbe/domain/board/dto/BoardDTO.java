package com.smsoft.playgroundbe.domain.board.dto;

import com.smsoft.playgroundbe.domain.board.entity.Board;
import com.smsoft.playgroundbe.domain.board.entity.Category;
import lombok.*;

public class BoardDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String title;
        private Long categoryId;
    }

    @Getter @Setter
    @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String categoryName;

        public static Response of(Board board) {
            return Response.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .categoryName(board.getCategory().getName())
                    .build();
        }
    }
}
