package com.smsoft.playgroundbe.domain.board.dto;

import com.smsoft.playgroundbe.domain.board.entity.Board;
import com.smsoft.playgroundbe.domain.board.entity.Comment;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

public class PostDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String authorId;
        private String title;
        private String content;
        private Long boardId;
    }

    @Getter @Setter
    @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private Long id;
        private String authorId;
        private String title;
        private String content;
        private String boardTitle;

        public static Response of(Post post) {
            return Response.builder()
                    .id(post.getId())
                    .authorId(post.getAuthorId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .boardTitle(post.getBoard().getTitle())
                    .build();
        }
    }
}
