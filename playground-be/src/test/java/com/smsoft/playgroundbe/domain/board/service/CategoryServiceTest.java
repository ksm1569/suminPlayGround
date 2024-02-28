package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.dto.CategoryDTO;
import com.smsoft.playgroundbe.domain.board.entity.Category;
import com.smsoft.playgroundbe.domain.board.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class CategoryServiceTest {

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 생성 - 성공 테스트")
    public void createCategorySuccess() {
        // Given
        CategoryDTO.Request categoryDTO = new CategoryDTO.Request();
        categoryDTO.setName("기술");
        Category mockCategory = new Category(1L, "기술", new ArrayList<>());
        Mockito.when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

        // When
        CategoryDTO.Response createdCategory = categoryService.createCategory(categoryDTO);

        // Then
        assertNotNull(createdCategory);
        assertEquals("기술", createdCategory.getName());
    }

    @Test
    @DisplayName("카테고리 업데이트 - 성공 테스트")
    public void updateCategorySuccess() {
        // Given
        Long categoryId = 1L;
        CategoryDTO.Request categoryDTO = new CategoryDTO.Request();
        categoryDTO.setName("스포츠");
        Category existingCategory = new Category(1L, "기술", new ArrayList<>());
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        Mockito.when(categoryRepository.save(any(Category.class))).thenReturn(existingCategory);

        // When
        CategoryDTO.Response updatedCategory = categoryService.updateCategory(categoryId, categoryDTO);

        // Then
        assertEquals("스포츠", updatedCategory.getName());
    }

    @Test
    @DisplayName("카테고리 업데이트 - 실패 테스트(Not Found)")
    public void updateCategoryNotFound() {
        // Given
        Long categoryId = 1L;
        CategoryDTO.Request categoryDTO = new CategoryDTO.Request();
        categoryDTO.setName("스포츠");
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.updateCategory(categoryId, categoryDTO);
        });

        assertEquals(exception.getMessage(), "Category not found with id: " + categoryId);
    }

    @Test
    @DisplayName("카테고리 삭제 - 성공 테스트")
    public void deleteCategorySuccess() {
        // Given
        Long categoryId = 1L;
        Category existingCategory = new Category(1L, "기술", new ArrayList<>());
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        // When
        categoryService.deleteCategory(categoryId);

        // Then
        Mockito.verify(categoryRepository, Mockito.times(1)).delete(existingCategory);
    }

    @Test
    @DisplayName("카테고리 삭제 - 실패 테스트(Not Found)")
    public void deleteCategoryNotFound() {
        // Given
        Long categoryId = 1L;
        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });

        assertEquals("Category not found with id: " + categoryId, exception.getMessage());
    }

    @Test
    @DisplayName("카테고리 전체조회 - 성공 테스트")
    public void getCategoriesSuccess() {
        // Given
        List<Category> categories = Arrays.asList(new Category(1L, "기술", new ArrayList<>()), new Category(2L, "과학", new ArrayList<>()));
        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        // When
        List<CategoryDTO.Response> allCategories = categoryService.getAllCategories();

        // Then
        assertEquals(2, allCategories.size());
        assertEquals("기술", allCategories.get(0).getName());
        assertEquals("과학", allCategories.get(1).getName());
    }
}