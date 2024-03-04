package com.smsoft.playgroundbe.domain.board.dto;

import com.smsoft.playgroundbe.domain.board.entity.Comment;
import lombok.*;

import java.util.Optional;

public class CommentDTO {

    @Getter @Setter
    @NoArgsConstructor
    public static class Request {
        private String authorId;
        private String content;
        private Long postId;
        private Long parentCommentId;
    }

    @Getter @Setter
    @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Response {
        private Long id;
        private String authorId;
        private String content;
        private String boardTitle;
        private Long parentCommentId;

        public static Response of(Comment comment) {
            return Response.builder()
                    .id(comment.getId())
                    .authorId(comment.getAuthorId())
                    .content(comment.getContent())
                    .boardTitle(comment.getPost().getBoard().getTitle())
                    .parentCommentId(Optional.ofNullable(comment.getParentComment()).map(Comment::getId).orElse(null))
                    .build();
        }
    }
}
