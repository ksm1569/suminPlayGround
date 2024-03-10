package com.smsoft.playgroundbe.domain.board.entity;

import com.smsoft.playgroundbe.domain.board.common.BaseEntity;
import com.smsoft.playgroundbe.domain.board.dto.CommentDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorId;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    public void patch(CommentDTO.Request commentDTO) {
        this.authorId = commentDTO.getAuthorId();
        this.content = commentDTO.getContent();
    }
}
