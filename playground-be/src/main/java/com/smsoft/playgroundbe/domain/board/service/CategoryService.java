package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.dto.CategoryDTO;
import com.smsoft.playgroundbe.domain.board.entity.Category;
import com.smsoft.playgroundbe.domain.board.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryDTO.Response createCategory(CategoryDTO.Request categoryDTO) {
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .build();

        Category savedCategory = categoryRepository.save(category);

        return CategoryDTO.Response.of(savedCategory);
    }

    @Transactional
    public CategoryDTO.Response updateCategory(Long categoryId, CategoryDTO.Request categoryDTO) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        findCategory.patch(categoryDTO);
        Category savedCategory = categoryRepository.save(findCategory);

        return CategoryDTO.Response.of(savedCategory);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        categoryRepository.delete(findCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO.Response> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO.Response::of)
                .collect(Collectors.toList());
    }
}
