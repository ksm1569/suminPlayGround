package com.smsoft.playgroundbe.domain.board.repository;

import com.smsoft.playgroundbe.domain.board.constant.ReactionType;
import com.smsoft.playgroundbe.domain.board.dto.ReactionDTO;
import com.smsoft.playgroundbe.domain.board.entity.Comment;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import com.smsoft.playgroundbe.domain.board.entity.Reaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReactionRepositoryTest {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("게시글 - 좋아요 싫어요 생성,저장 테스트")
    public void createAndSaveReactionForPost() {
        Post post = Post.builder()
                .authorId("글쓴이")
                .title("제목")
                .content("내용")
                .build();

        Post savedPost = postRepository.save(post);

        Reaction reaction1 = Reaction.builder()
                .post(savedPost)
                .userId("회원1")
                .reactionType(ReactionType.LOVE)
                .build();

        Reaction reaction2 = Reaction.builder()
                .post(savedPost)
                .userId("회원2")
                .reactionType(ReactionType.DISLOVE)
                .build();

        Reaction reaction3 = Reaction.builder()
                .post(savedPost)
                .userId("회원3")
                .reactionType(ReactionType.LOVE)
                .build();

        reactionRepository.saveAll(List.of(reaction1, reaction2, reaction3));

        int postLovesCount = reactionRepository.countByPostIdAndReactionType(post.getId(), ReactionType.LOVE);
        int postNotLovesCount = reactionRepository.countByPostIdAndReactionType(post.getId(), ReactionType.DISLOVE);

        assertThat(postLovesCount).isEqualTo(2);
        assertThat(postNotLovesCount).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글에 대한 '좋아요' 생성 및 저장 테스트")
    public void createAndSaveReactionForComment() {
        Post post = Post.builder()
                .authorId("글쓴이")
                .title("제목")
                .content("내용")
                .build();

        Post savedPost = postRepository.save(post);

        Comment comment = Comment.builder()
                .post(savedPost)
                .authorId("회원0")
                .build();

        Comment savedComment = commentRepository.save(comment);

        Reaction reaction1 = Reaction.builder()
                .comment(savedComment)
                .userId("회원1")
                .reactionType(ReactionType.LOVE)
                .build();

        Reaction reaction2 = Reaction.builder()
                .comment(savedComment)
                .userId("회원2")
                .reactionType(ReactionType.DISLOVE)
                .build();

        Reaction reaction3 = Reaction.builder()
                .comment(savedComment)
                .userId("회원3")
                .reactionType(ReactionType.LOVE)
                .build();

        reactionRepository.saveAll(List.of(reaction1, reaction2, reaction3));

        int commentLovesCount = reactionRepository.countByCommentIdAndReactionType(savedComment.getId(), ReactionType.LOVE);
        int commentNotLovesCount = reactionRepository.countByCommentIdAndReactionType(savedComment.getId(), ReactionType.DISLOVE);

        assertThat(commentLovesCount).isEqualTo(2);
        assertThat(commentNotLovesCount).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글에 대한 좋아요 정보 변경 - 좋아요 취소")
    public void updateReaction() {
        Post post = Post.builder()
                .authorId("글쓴이")
                .title("제목")
                .content("내용")
                .build();

        Post savedPost = postRepository.save(post);

        Reaction reaction1 = Reaction.builder()
                .post(savedPost)
                .userId("회원1")
                .reactionType(ReactionType.LOVE)
                .build();

        Reaction reaction2 = Reaction.builder()
                .post(savedPost)
                .userId("회원2")
                .reactionType(ReactionType.DISLOVE)
                .build();

        Reaction reaction3 = Reaction.builder()
                .post(savedPost)
                .userId("회원3")
                .reactionType(ReactionType.LOVE)
                .build();

        Reaction savedReaction1 = reactionRepository.save(reaction1);
        reactionRepository.saveAll(List.of(reaction2, reaction3));

        // 변경 전
        int postLovesCount = reactionRepository.countByPostIdAndReactionType(post.getId(), ReactionType.LOVE);
        int postNotLovesCount = reactionRepository.countByPostIdAndReactionType(post.getId(), ReactionType.DISLOVE);

        assertThat(postLovesCount).isEqualTo(2);
        assertThat(postNotLovesCount).isEqualTo(1);
        
        // 변경 후
        ReactionDTO.Request reactionDTO = new ReactionDTO.Request();
        reactionDTO.setReactionType(ReactionType.NONE);
        savedReaction1.patch(reactionDTO);

        int updatePostLovesCount = reactionRepository.countByPostIdAndReactionType(post.getId(), ReactionType.LOVE);
        int updatePostNotLovesCount = reactionRepository.countByPostIdAndReactionType(post.getId(), ReactionType.DISLOVE);

        assertThat(updatePostLovesCount).isEqualTo(1);
        assertThat(updatePostNotLovesCount).isEqualTo(1);
    }
}