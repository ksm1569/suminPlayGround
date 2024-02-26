package com.smsoft.playgroundbe.domain.board.repository;

import com.smsoft.playgroundbe.domain.board.dto.CommentDTO;
import com.smsoft.playgroundbe.domain.board.dto.PostDTO;
import com.smsoft.playgroundbe.domain.board.entity.Comment;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("게시글 없이 댓글 생성 및 저장 테스트")
    public void createAndSaveComment() {
        Comment comment = Comment.builder()
                .authorId("sumin2")
                .content("좋아요 누르고 갑니다!")
                .build();

        Comment savedComment = commentRepository.save(comment);
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("좋아요 누르고 갑니다!");
    }

    @Test
    @DisplayName("하나의 게시글에 여러개의 댓글 생성,저장,연결 테스트")
    public void createAndSaveMultipleCommentWithPost() {
        Post post = Post.builder()
                .authorId("sumin")
                .title("하하하 안녕하세요")
                .content("테스트 입니다.")
                .build();

        Post savedPost = postRepository.save(post);

        Comment comment1 = Comment.builder()
                .post(savedPost)
                .authorId("댓글단사람1")
                .content("댓글1 내용")
                .build();

        Comment comment2 = Comment.builder()
                .post(savedPost)
                .authorId("댓글단사람2")
                .content("댓글2 내용")
                .build();

        Comment comment3 = Comment.builder()
                .post(savedPost)
                .authorId("댓글단사람3")
                .content("댓글3 내용")
                .build();

        commentRepository.saveAll(List.of(comment1, comment2, comment3));
        List<Comment> foundComments = commentRepository.findAllByPostId(savedPost.getId());

        assertThat(foundComments.size()).isEqualTo(3);
        assertThat(foundComments.get(1).getContent()).isEqualTo("댓글2 내용");
    }

    @Test
    @DisplayName("댓글의 댓글 생성 및 저장 테스트")
    public void createAndSaveCommentInComment() {
        Post post = Post.builder()
                .authorId("sumin")
                .title("하하하 안녕하세요")
                .content("테스트 입니다.")
                .build();

        Post savedPost = postRepository.save(post);

        Comment comment = Comment.builder()
                .post(savedPost)
                .authorId("댓글단사람")
                .content("댓글 내용")
                .build();

        Comment savedComment = commentRepository.save(comment);

        Comment commentInComment1 = Comment.builder()
                .authorId("대댓글단사람1")
                .content("대댓글 내용1")
                .parentComment(savedComment)
                .build();

        Comment commentInComment2 = Comment.builder()
                .authorId("대댓글단사람2")
                .content("대댓글 내용2")
                .parentComment(savedComment)
                .build();

        commentRepository.saveAll(List.of(commentInComment1, commentInComment2));

        // lazy 로딩 관련 refresh
        entityManager.refresh(savedComment);

        assertThat(savedComment.getContent()).isEqualTo("댓글 내용");
        assertThat(savedComment.getReplies().size()).isEqualTo(2);
        assertThat(savedComment.getReplies().get(0).getAuthorId()).isEqualTo("대댓글단사람1");
        assertThat(savedComment.getReplies().get(1).getContent()).isEqualTo("대댓글 내용2");
    }

    @Test
    @DisplayName("댓글 정보 수정 테스트")
    public void updateComment() {
        Post post = Post.builder()
                .authorId("글쓴이")
                .title("제목!")
                .content("내용!")
                .build();

        Post savedPost = postRepository.save(post);

        Comment comment = Comment.builder()
                .authorId("댓글쓴사람")
                .content("댓글내용!")
                .post(savedPost)
                .build();

        Comment savedComment = commentRepository.save(comment);

        // 변경전
        assertThat(savedComment.getContent()).isEqualTo("댓글내용!");

        // 변경후
        CommentDTO.Request commentDTO = new CommentDTO.Request();
        commentDTO.setAuthorId("댓글쓴사람 변경");
        commentDTO.setContent("댓글내용 변경!");

        savedComment.patch(commentDTO);
        Comment updatedComment = commentRepository.save(savedComment);

        Optional<Comment> foundComment = commentRepository.findById(updatedComment.getId());
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("댓글내용 변경!");
    }

    @Test
    @DisplayName("댓글 삭제 시 게시글 영향도 검증 테스트")
    public void deleteCommentAndVerifyPostIntegrity() {
        Post post = Post.builder()
                .authorId("글쓴이")
                .title("제목")
                .content("내용")
                .build();

        Post savedPost = postRepository.save(post);

        Comment comment = Comment.builder()
                .authorId("댓글쓴사람")
                .content("댓글내용!")
                .post(savedPost)
                .build();

        Comment savedComment = commentRepository.save(comment);

        Optional<Comment> foundComment = commentRepository.findById(savedComment.getId());
        assertThat(foundComment.get().getContent()).isEqualTo("댓글내용!");

        // 삭제
        commentRepository.delete(savedComment);

        Optional<Comment> deleteFoundComment = commentRepository.findById(savedComment.getId());

        assertThat(deleteFoundComment.isPresent()).isFalse();

        Optional<Post> foundPost = postRepository.findById(savedPost.getId());
        assertThat(foundPost).isPresent();
    }

    @Test
    @DisplayName("특정 게시글에 속한 댓글 조회 테스트")
    public void findCommentsByPost() {
        Post post = Post.builder()
                .authorId("글쓴이")
                .title("제목")
                .content("내용")
                .build();

        Post savedPost = postRepository.save(post);

        Comment comment1 = Comment.builder()
                .authorId("댓글쓴사람1")
                .content("댓글내용1!")
                .post(savedPost)
                .build();

        Comment comment2 = Comment.builder()
                .authorId("댓글쓴사람2")
                .content("댓글내용2!")
                .post(savedPost)
                .build();

        commentRepository.saveAll(List.of(comment1, comment2));

        List<Comment> comments = commentRepository.findAllByPostId(savedPost.getId());
        assertThat(comments.get(1).getContent()).isEqualTo("댓글내용2!");
    }
}