package com.smsoft.playgroundbe.domain.board.repository;

import com.smsoft.playgroundbe.domain.board.entity.Board;
import com.smsoft.playgroundbe.domain.board.entity.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 없이 게시판 생성 및 저장 테스트")
    public void createAndSaveBoardWithoutCategory() {
        Board board1 = Board.builder()
                .title("축구")
                .build();

        Board board2 = Board.builder()
                .title("야구")
                .build();

        Board savedBoard1 = boardRepository.save(board1);
        Board savedBoard2 = boardRepository.save(board2);

        assertThat(savedBoard1.getId()).isNotNull();
        assertThat(savedBoard2.getId()).isNotNull();
        assertThat(savedBoard1.getTitle()).isEqualTo("축구");
        assertThat(savedBoard2.getTitle()).isEqualTo("야구");
    }

    @Test
    @DisplayName("하나의 카테고리 함께 여러 게시판 생성,저장,연결 테스트")
    public void createAndSaveMultipleBoardWithCategory() {
        Category category = Category.builder()
                .name("스포츠")
                .build();

        Category savedCategory = categoryRepository.save(category);

        Board board1 = Board.builder()
                .title("축구")
                .category(savedCategory)
                .build();

        Board board2 = Board.builder()
                .title("야구")
                .category(savedCategory)
                .build();

        Board board3 = Board.builder()
                .title("농구")
                .category(savedCategory)
                .build();

        boardRepository.saveAll(List.of(board1, board2, board3));
        List<Board> savedBoards = boardRepository.findAllByCategoryId(savedCategory.getId());
        assertEquals(3, savedBoards.size());
    }

    @Test
    @DisplayName("게시판 삭제 시 카테고리 영향도 검증 테스트")
    public void deleteBoardAndVerifyCategoryIntegrity() {
        Category category = Category.builder()
                .name("영향도 테스트")
                .build();

        Category savedCategory = categoryRepository.save(category);

        Board board1 = Board.builder()
                .title("축구")
                .category(savedCategory)
                .build();

        Board savedBoard = boardRepository.save(board1);

        boardRepository.delete(savedBoard);
        Optional<Category> remainingCategory = categoryRepository.findById(savedCategory.getId());

        assertTrue(remainingCategory.isPresent());
    }

    @Test
    @DisplayName("특정 카테고리에 속한 게시판 조회 테스트")
    public void findBoardsByCategory() {
        Category category1 = Category.builder()
                .name("E스포츠")
                .build();

        Category category2 = Category.builder()
                .name("주식")
                .build();

        Category savedCategory1 = categoryRepository.save(category1);
        Category savedCategory2 = categoryRepository.save(category2);

        Board board1 = Board.builder()
                .title("스타크래프트")
                .category(savedCategory1)
                .build();

        Board board2 = Board.builder()
                .title("롤")
                .category(savedCategory1)
                .build();

        Board board3 = Board.builder()
                .title("워크래프트")
                .category(savedCategory1)
                .build();

        boardRepository.saveAll(List.of(board1, board2, board3));
        List<Board> boards1 = boardRepository.findAllByCategoryId(savedCategory1.getId());
        assertEquals(3, boards1.size());

        List<Board> boards2 = boardRepository.findAllByCategoryId(savedCategory2.getId());
        assertEquals(0, boards2.size());
    }
}