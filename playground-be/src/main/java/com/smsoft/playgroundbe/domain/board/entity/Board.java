package com.smsoft.playgroundbe.domain.board.entity;

import com.smsoft.playgroundbe.domain.board.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @OneToMany(mappedBy = "board")
    private List<Post> posts = new ArrayList<>();

    public void patch(BoardDTO.Request requestDTO) {
        this.title = requestDTO.getTitle();
    }
}