package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.dto.PostDTO;
import com.smsoft.playgroundbe.domain.board.entity.Board;
import com.smsoft.playgroundbe.domain.board.entity.Category;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import com.smsoft.playgroundbe.domain.board.repository.BoardRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
class PostServiceTest {
    @MockBean
    private BoardRepository boardRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("게시글 생성 - 성공 테스트")
    public void postCraeteSuccess() {
        // Given
        PostDTO.Request postDTO = new PostDTO.Request();
        postDTO.setAuthorId("작성자");
        postDTO.setTitle("제목");
        postDTO.setContent("내용");
        postDTO.setBoardId(1L);

        Board mockBoard = new Board(1L, "축구", new Category(), new ArrayList<>());
        Post mockPost = new Post(1L, "작성자", "제목", "내용", mockBoard, new ArrayList<>());

        given(boardRepository.findById(postDTO.getBoardId())).willReturn(Optional.of(mockBoard));
        given(postRepository.save(any(Post.class))).willReturn(mockPost);

        // When
        PostDTO.Response responseDTO = postService.createPost(postDTO);

        // Then
        assertNotNull(responseDTO);
        assertEquals("제목", responseDTO.getTitle());
        assertEquals("내용", responseDTO.getContent());
        assertEquals("축구", responseDTO.getBoardTitle());
    }

    @Test
    @DisplayName("게시글 생성 - 실패 테스트")
    public void postCreateNotFound() {
        // Given
        PostDTO.Request postDTO = new PostDTO.Request();
        postDTO.setAuthorId("작성자");
        postDTO.setTitle("제목");
        postDTO.setContent("내용");
        postDTO.setBoardId(1L);

        given(boardRepository.findById(postDTO.getBoardId())).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            postService.createPost(postDTO);
        });

        assertEquals("Board not found with id : " + postDTO.getBoardId(), exception.getMessage());
    }

    @Test
    @DisplayName("게시글 수정 - 성공 테스트")
    public void postUpdateSuccess() {
        // Given
        Long postId = 1L;
        PostDTO.Request postDTO = new PostDTO.Request();
        postDTO.setAuthorId("작성자2");
        postDTO.setTitle("제목2");
        postDTO.setContent("내용2");

        Post existingPost = new Post(1L, "작성자", "제목", "내용", new Board(), new ArrayList<>());

        given(postRepository.findById(postId)).willReturn(Optional.of(existingPost));
        given(postRepository.save(any(Post.class))).willReturn(existingPost);

        // When
        PostDTO.Response response = postService.updatePost(postId, postDTO);

        // Then
        assertNotNull(response);
        assertEquals(postDTO.getAuthorId(), response.getAuthorId());
        assertEquals(postDTO.getTitle(), response.getTitle());
        assertEquals(postDTO.getContent(), response.getContent());
    }

    @Test
    @DisplayName("게시글 수정 - post 찾기 실패 테스트")
    public void postUpdatePostNotFound() {
        // Given
        Long postId = 1L;
        PostDTO.Request postDTO = new PostDTO.Request();

        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            postService.updatePost(postId, postDTO);
        });
        assertEquals("Post Not Found with id : " + postId, exception.getMessage());
    }

    @Test
    @DisplayName("게시글 수정 - board 찾기 실패 테스트")
    public void postUpdateBoardNotFound() {
        // Given
        Long postId = 1L;
        Long boardId = 1L;
        PostDTO.Request postDTO = new PostDTO.Request();
        postDTO.setBoardId(boardId);
        Post existingPost = new Post(1L, "작성자", "제목", "내용", new Board(), new ArrayList<>());

        given(postRepository.findById(postId)).willReturn(Optional.of(existingPost));
        given(boardRepository.findById(boardId)).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            postService.updatePost(postId, postDTO);
        });

        assertEquals("Board Not Found with id : " + boardId, exception.getMessage());
    }

    @Test
    @DisplayName("게시글 삭제 - 성공 테스트")
    public void deletePostSuccess() {
        // Given
        Long postId = 1L;
        Board board = Board.builder().build();
        Post post = Post.builder()
                .id(postId)
                .authorId("작성자")
                .title("제목")
                .content("내용")
                .board(board)
                .build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post));

        // When
        postService.deletePost(postId);

        // Then
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("게시글 삭제 - 실패 테스트")
    public void deletePostNotFound() {
        // Given
        Long postId = 1L;
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            postService.deletePost(postId);
        });

        assertEquals("Post not found with id : " + postId, exception.getMessage());
    }

    @Test
    @DisplayName("모든 게시글 조회 - 성공 테스트")
    public void postAllPostSuccess() {
        // Given
        List<Post> posts = new ArrayList<>();
        posts.add(new Post(1L, "작성자1", "제목1", "내용1", new Board(), new ArrayList<>()));
        posts.add(new Post(2L, "작성자2", "제목2", "내용2", new Board(), new ArrayList<>()));
        posts.add(new Post(3L, "작성자3", "제목3", "내용3", new Board(), new ArrayList<>()));

        given(postRepository.findAll()).willReturn(posts);

        // When
        List<PostDTO.Response> responses = postService.getAllPosts();

        // Then
        assertEquals(3, responses.size());
        assertEquals("제목1", responses.get(0).getTitle());
        assertEquals("내용2", responses.get(1).getContent());
        assertEquals("작성자3", responses.get(2).getAuthorId());
    }
}