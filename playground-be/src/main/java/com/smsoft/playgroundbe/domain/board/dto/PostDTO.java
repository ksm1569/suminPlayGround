package com.smsoft.playgroundbe.domain.board.dto;

import com.smsoft.playgroundbe.domain.board.entity.Board;
import com.smsoft.playgroundbe.domain.board.entity.Comment;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class PostDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String authorId;
        private String title;
        private String content;
    }

    @Getter @Setter
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String authorId;
        private String title;
        private String content;
    }
}
