package com.smsoft.playgroundbe.domain.board.service;

import com.smsoft.playgroundbe.domain.board.dto.CommentDTO;
import com.smsoft.playgroundbe.domain.board.entity.Comment;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import com.smsoft.playgroundbe.domain.board.repository.CommentRepository;
import com.smsoft.playgroundbe.domain.board.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentDTO.Response createComment(CommentDTO.Request commentDTO) {
        Post findPost = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + commentDTO.getPostId()));

        Comment comment = Comment.builder()
                .authorId(commentDTO.getAuthorId())
                .content(commentDTO.getContent())
                .post(findPost)
                .parentComment(commentDTO.getParentCommentId() != null ?
                        commentRepository.findById(commentDTO.getParentCommentId())
                                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found")) : null)
                .build();

        return CommentDTO.Response.of(commentRepository.save(comment));
    }

    @Transactional
    public CommentDTO.Response updateComment(Long id, CommentDTO.Request commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

        comment.patch(commentDTO);

        return CommentDTO.Response.of(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment findComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

        commentRepository.delete(findComment);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO.Response> getAllComments() {
        return commentRepository.findAll().stream()
                .map(CommentDTO.Response::of)
                .collect(Collectors.toList());
    }
}
