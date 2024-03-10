package com.smsoft.playgroundbe.domain.board.entity;

import com.smsoft.playgroundbe.domain.board.common.BaseEntity;
import com.smsoft.playgroundbe.domain.board.dto.PostDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authorId;
    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    public void patch(PostDTO.Request postDTO) {
        this.authorId = postDTO.getAuthorId();
        this.title = postDTO.getTitle();
        this.content = postDTO.getContent();
    }
}
