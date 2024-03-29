package com.smsoft.playgroundbe.domain.board.controller;

import com.smsoft.playgroundbe.domain.board.dto.CategoryDTO;
import com.smsoft.playgroundbe.domain.board.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO.Response> createCategory(@RequestBody CategoryDTO.Request categoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(categoryDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO.Response> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryDTO.Request categoryDTO
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO.Response>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }
}
