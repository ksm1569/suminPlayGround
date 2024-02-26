package com.smsoft.playgroundbe.domain.board.repository;

import com.smsoft.playgroundbe.domain.board.dto.CategoryDTO;
import com.smsoft.playgroundbe.domain.board.entity.Category;
import jakarta.persistence.TableGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 생성 및 저장 테스트")
    public void createAndSaveCategory() {
        Category category = Category.builder()
                .name("스포츠")
                .build();

        Category savedCategory = categoryRepository.save(category);
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("스포츠");
    }

    @Test
    @DisplayName("카테고리 조회 테스트")
    public void findCategory() {
        Category category = Category.builder()
                .name("음악")
                .build();

        Category savedCategory = categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("음악");
    }

    @Test
    @DisplayName("카테고리 수정 테스트")
    public void updateCategoryInfo() {
        Category category = Category.builder()
                .name("스포츠")
                .build();

        // 변경 전
        Category savedCategory = categoryRepository.save(category);
        assertThat(savedCategory.getName()).isEqualTo("스포츠");

        // 변경 후
        CategoryDTO.Request categoryDTO = new CategoryDTO.Request();
        categoryDTO.setName("프로그래밍");

        savedCategory.patch(categoryDTO);
        categoryRepository.save(savedCategory);

        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());
        assertThat(foundCategory).isPresent();
        assertThat(foundCategory.get().getName()).isEqualTo("프로그래밍");
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    public void deleteCategory() {
        Category category = Category.builder()
                .name("스포츠")
                .build();

        Category savedCategory = categoryRepository.save(category);
        Long savedCategoryId = savedCategory.getId();

        boolean existsed = categoryRepository.existsById(savedCategoryId);
        assertThat(existsed).isTrue();

        categoryRepository.deleteById(savedCategoryId);
        existsed = categoryRepository.existsById(savedCategoryId);

        assertThat(existsed).isFalse();
    }
}