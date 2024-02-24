package com.smsoft.playgroundbe.domain.board.repository;

import com.smsoft.playgroundbe.domain.board.dto.PostDTO;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글 생성 및 저장 테스트")
    public void createPostAndSave() {
        Post post = Post.builder()
                .authorId("sumin")
                .title("축구에대하여")
                .content("축구는재밌다")
                .build();

        Post savedPost = postRepository.save(post);
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getAuthorId()).isEqualTo("sumin");
        assertThat(savedPost.getTitle()).isNotEqualTo("축구에 대하여");
        assertThat(savedPost.getTitle()).isEqualTo("축구에대하여");
        assertThat(savedPost.getContent()).isEqualTo("축구는재밌다");
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    public void findPost() {
        Post post1 = Post.builder()
                .authorId("sumin")
                .title("농구란")
                .content("왼손은 거들뿐이다")
                .build();

        Post post2 = Post.builder()
                .authorId("sumin")
                .title("게맛을알어?")
                .content("롯데리아여")
                .build();

        List<Post> posts = postRepository.saveAll(List.of(post1, post2));

        Optional<Post> foundPost1 = postRepository.findById(posts.get(0).getId());
        Optional<Post> foundPost2 = postRepository.findById(posts.get(1).getId());

        assertThat(foundPost1).isPresent();
        assertThat(foundPost1.get().getTitle()).isEqualTo("농구란");

        assertThat(foundPost2).isPresent();
        assertThat(foundPost2.get().getTitle()).isEqualTo("게맛을알어?");
    }

    @Test
    @DisplayName("게시글 정보 수정 테스트")
    public void updatePost() {
        Post post = Post.builder()
                .authorId("sumin1")
                .title("제목1")
                .content("내용1")
                .build();

        Post savedPost = postRepository.save(post);

        // 수정전
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getAuthorId()).isEqualTo("sumin1");
        assertThat(savedPost.getTitle()).isEqualTo("제목1");
        assertThat(savedPost.getContent()).isEqualTo("내용1");

        // 수정후
        PostDTO.Request postDTO = new PostDTO.Request();
        postDTO.setAuthorId("sumin2");
        postDTO.setTitle("제목2");
        postDTO.setContent("내용2");

        savedPost.patch(postDTO);
        Post updatedPost = postRepository.save(savedPost);

        Optional<Post> foundPost = postRepository.findById(updatedPost.getId());
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getAuthorId()).isEqualTo("sumin2");
        assertThat(foundPost.get().getTitle()).isEqualTo("제목2");
        assertThat(foundPost.get().getContent()).isEqualTo("내용2");
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deletePost() {
        Post post = Post.builder()
                .authorId("sumin")
                .title("삭제할 게시글")
                .content("삭제 테스트")
                .build();

        Post savedPost = postRepository.save(post);
        Long postId = savedPost.getId();

        postRepository.deleteById(postId);

        boolean deletedPost = postRepository.existsById(postId);
        assertThat(deletedPost).isFalse();
    }
}