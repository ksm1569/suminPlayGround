package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.dto.BoardDTO;
import com.smsoft.playgroundbe.domain.board.dto.PostDTO;
import com.smsoft.playgroundbe.domain.board.entity.Board;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import com.smsoft.playgroundbe.domain.board.repository.BoardRepository;
import com.smsoft.playgroundbe.domain.board.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostDTO.Response createPost(PostDTO.Request postDTO) {
        Board findBoard = boardRepository.findById(postDTO.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id : " + postDTO.getBoardId()));

        Post post = Post.builder()
                .authorId(postDTO.getAuthorId())
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .build();

        Post savedPost = postRepository.save(post);

        return PostDTO.Response.of(savedPost);
    }


    @Transactional
    public PostDTO.Response updatePost(Long postId, PostDTO.Request postDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post Not Found with id : " + postId));

        Optional.ofNullable(postDTO.getBoardId())
                        .ifPresent(boardId -> {
                            boardRepository.findById(boardId)
                                    .orElseThrow(() -> new EntityNotFoundException("Board Not Found with id : " + boardId));
                        });

        post.patch(postDTO);

        return PostDTO.Response.of(postRepository.save(post));
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id : " + postId));

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public List<PostDTO.Response> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostDTO.Response::of)
                .collect(Collectors.toList());
    }

}
