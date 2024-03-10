package com.smsoft.playgroundbe.domain.board.entity;

import com.smsoft.playgroundbe.domain.board.common.BaseEntity;
import com.smsoft.playgroundbe.domain.board.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Board> boards = new ArrayList<>();

    public void patch(CategoryDTO.Request requestDTO) {
        this.name = requestDTO.getName();
    }
}
