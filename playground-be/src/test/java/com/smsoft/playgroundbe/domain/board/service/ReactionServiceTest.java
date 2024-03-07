package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.constant.ReactionType;
import com.smsoft.playgroundbe.domain.board.dto.ReactionDTO;
import com.smsoft.playgroundbe.domain.board.entity.Comment;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import com.smsoft.playgroundbe.domain.board.entity.Reaction;
import com.smsoft.playgroundbe.domain.board.repository.CommentRepository;
import com.smsoft.playgroundbe.domain.board.repository.PostRepository;
import com.smsoft.playgroundbe.domain.board.repository.ReactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class ReactionServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private ReactionService reactionService;

    @Test
    @DisplayName("반응 추가/업데이트 - 성공 테스트")
    public void addOrUpdateReactionSuccess() {
        // Given
        ReactionDTO.Request reactionDTO = new ReactionDTO.Request();
        reactionDTO.setUserId("user123");
        reactionDTO.setReactionType(ReactionType.LOVE);
        reactionDTO.setPostId(1L);

        Post mockPost = Post.builder().build();
        Comment mockComment = Comment.builder().id(1L).build();

        Reaction mockReaction = Reaction.builder()
                .id(1L)
                .userId("user123")
                .post(mockPost)
                .comment(mockComment) // 댓글 설정
                .reactionType(ReactionType.LOVE)
                .build();

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(mockPost));
        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(mockComment));
        given(reactionRepository.save(any(Reaction.class))).willReturn(mockReaction);

        // When
        ReactionDTO.Response response = reactionService.addOrUpdateReaction(reactionDTO);

        // Then
        assertNotNull(response);
        assertEquals(ReactionType.LOVE, response.getReactionType());
        assertEquals(mockComment.getId(), response.getCommentId());
    }

    @Test
    @DisplayName("반응 추가/업데이트 - 실패 테스트 (게시글 찾기 실패)")
    public void addOrUpdateReactionPostNotFound() {
        // Given
        ReactionDTO.Request reactionDTO = new ReactionDTO.Request();
        reactionDTO.setUserId("user123");
        reactionDTO.setReactionType(ReactionType.LOVE);
        reactionDTO.setPostId(1L);

        given(postRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> reactionService.addOrUpdateReaction(reactionDTO));
    }

    @Test
    @DisplayName("반응 추가/업데이트 - 실패 테스트 (댓글 찾기 실패)")
    public void addOrUpdateReactionCommentNotFound() {
        // Given
        ReactionDTO.Request reactionDTO = new ReactionDTO.Request();
        reactionDTO.setUserId("user123");
        reactionDTO.setReactionType(ReactionType.LOVE);
        reactionDTO.setCommentId(1L);

        given(commentRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> reactionService.addOrUpdateReaction(reactionDTO));
    }
}
