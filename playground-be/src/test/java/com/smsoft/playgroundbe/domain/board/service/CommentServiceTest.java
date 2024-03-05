package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.dto.CommentDTO;
import com.smsoft.playgroundbe.domain.board.entity.Board;
import com.smsoft.playgroundbe.domain.board.entity.Comment;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import com.smsoft.playgroundbe.domain.board.repository.CommentRepository;
import com.smsoft.playgroundbe.domain.board.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class CommentServiceTest {
    @MockBean
    private PostRepository postRepository;

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("댓글 생성 - 성공 테스트")
    public void commentCreateSuccess() {
        // Given
        CommentDTO.Request commentDTO = new CommentDTO.Request();
        commentDTO.setAuthorId("작성자");
        commentDTO.setContent("내용");
        commentDTO.setPostId(1L);

        Board mockBoard = Board.builder()
                .build();

        Post mockPost = Post.builder()
                .id(1L)
                .authorId("게시글작성자")
                .title("글제목")
                .content("글내용")
                .board(mockBoard)
                .comments(new ArrayList<>())
                .build();

        Comment mockComment = Comment.builder()
                .id(1L)
                .authorId("댓글작성자")
                .content("댓글내용")
                .post(mockPost)
                .parentComment(null)
                .replies(new ArrayList<>())
                .build();

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(mockPost));
        given(commentRepository.save(any(Comment.class))).willReturn(mockComment);

        // When
        CommentDTO.Response response = commentService.createComment(commentDTO);

        // Then
        assertEquals("댓글내용", response.getContent());
    }

    @Test
    @DisplayName("댓글 생성 - 실패 테스트 - 게시글 찾기 실패")
    public void commentCreatePostNotFound() {
        // Given
        Long postId = 1L;

        CommentDTO.Request commentDTO = new CommentDTO.Request();
        commentDTO.setAuthorId("작성자2");
        commentDTO.setContent("내용2");
        commentDTO.setPostId(postId);
        commentDTO.setParentCommentId(null);

        given(postRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            commentService.createComment(commentDTO);
        });

        assertEquals("Post not found with id: " + postId, exception.getMessage());
    }

    @Test
    @DisplayName("댓글 생성 - 실패 테스트 - 부모 댓글 찾기 실패")
    public void commentCreateParentCommentNotFound() {
        // Given
        Long postId = 1L;

        Post mockPost = Post.builder()
                .id(postId)
                .authorId("게시글작성자")
                .title("글제목")
                .content("글내용")
                .board(new Board())
                .comments(new ArrayList<>())
                .build();

        CommentDTO.Request commentDTO = new CommentDTO.Request();
        commentDTO.setAuthorId("작성자2");
        commentDTO.setContent("내용2");
        commentDTO.setPostId(postId);
        commentDTO.setParentCommentId(1L);

        given(postRepository.findById(any(Long.class))).willReturn(Optional.of(mockPost));
        given(commentRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            commentService.createComment(commentDTO);
        });

        assertEquals("Parent comment not found", exception.getMessage());
    }

    @Test
    @DisplayName("댓글 수정 - 성공 테스트")
    public void commentUpdateSuccess() {
        // Given
        Long boardId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        CommentDTO.Request commentDTO = new CommentDTO.Request();
        commentDTO.setAuthorId("댓글작성자2");
        commentDTO.setContent("댓글내용2");
        commentDTO.setParentCommentId(null);
        commentDTO.setPostId(postId);

        Board mockBoard = Board.builder()
                .id(boardId)
                .title("스포츠")
                .build();

        Post mockPost = Post.builder()
                .id(postId)
                .board(mockBoard)
                .build();

        Comment mockComment = Comment.builder()
                .id(commentId)
                .authorId("댓글작성자1")
                .content("댓글내용1")
                .post(mockPost)
                .build();

        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(mockComment));
        given(commentRepository.save(any(Comment.class))).willReturn(mockComment);

        // When
        CommentDTO.Response response = commentService.updateComment(commentId, commentDTO);

        // Then
        assertEquals("댓글내용2", response.getContent());
    }

    @Test
    @DisplayName("댓글 수정 - 실패 테스트 - 댓글 찾기 실패")
    public void commentUpdateNotFound() {
        // Given
        Long boardId = 1L;
        Long postId = 1L;
        Long commentId = 1L;

        CommentDTO.Request commentDTO = new CommentDTO.Request();
        commentDTO.setAuthorId("댓글작성자2");
        commentDTO.setContent("댓글내용2");
        commentDTO.setParentCommentId(null);
        commentDTO.setPostId(postId);

        Board mockBoard = Board.builder()
                .id(boardId)
                .title("스포츠")
                .build();

        Post mockPost = Post.builder()
                .id(postId)
                .board(mockBoard)
                .build();

        Comment mockComment = Comment.builder()
                .id(commentId)
                .authorId("댓글작성자1")
                .content("댓글내용1")
                .post(mockPost)
                .build();

        given(commentRepository.findById(any(Long.class))).willReturn(Optional.empty());
        given(commentRepository.save(any(Comment.class))).willReturn(mockComment);

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            commentService.updateComment(commentId, commentDTO);
        });

        assertEquals("Comment not found with id: " + commentId, exception.getMessage());
    }

    @Test
    @DisplayName("댓글 삭제 - 성공 테스트")
    public void commentDeleteSuccess() {
        // Given
        Long commentId = 1L;

        Comment comment = Comment.builder()
                .id(commentId)
                .authorId("댓글작성자1")
                .content("댓글내용1")
                .post(Post.builder().build())
                .build();

        given(commentRepository.findById(any(Long.class))).willReturn(Optional.of(comment));

        // When
        commentService.deleteComment(commentId);

        // Then
        verify(commentRepository, times(1)).delete(comment);
    }
    
    @Test
    @DisplayName("댓글 삭제 - 실패 테스트 - 댓글 찾기 실패")
    public void commentDeleteNotFound() {
        // Given
        Long commentId = 1L;

        given(commentRepository.findById(any(Long.class))).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            commentService.deleteComment(commentId);
        });

        assertEquals("Comment not found with id: " + commentId, exception.getMessage());
    }
}