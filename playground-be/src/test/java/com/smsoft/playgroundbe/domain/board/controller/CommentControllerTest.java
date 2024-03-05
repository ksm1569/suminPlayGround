package com.smsoft.playgroundbe.domain.board.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsoft.playgroundbe.domain.board.dto.CommentDTO;
import com.smsoft.playgroundbe.domain.board.entity.Comment;
import com.smsoft.playgroundbe.domain.board.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(CommentController.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    @DisplayName("댓글 생성 API 테스트")
    public void createComment() throws Exception {
        CommentDTO.Request commentDTO = new CommentDTO.Request();
        commentDTO.setPostId(1L);
        commentDTO.setAuthorId("댓글 작성자");
        commentDTO.setContent("댓글 내용");
        commentDTO.setParentCommentId(null);

        CommentDTO.Response response = new CommentDTO.Response(1L, "댓글 작성자", "댓글 내용", "스포츠", null);

        given(commentService.createComment(any(CommentDTO.Request.class))).willReturn(response);

        mockMvc.perform(post("/comments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is(response.getContent())));
    }

    @Test
    @DisplayName("댓글 수정 API 테스트")
    public void updateComment() throws Exception {
        Long commentId = 1L;

        CommentDTO.Request commentDTO = new CommentDTO.Request();
        commentDTO.setPostId(1L);
        commentDTO.setAuthorId("댓글 작성자");
        commentDTO.setContent("댓글 내용");
        commentDTO.setParentCommentId(null);

        CommentDTO.Response response = new CommentDTO.Response(1L, "댓글 작성자", "댓글 내용", "스포츠", null);

        given(commentService.updateComment(Mockito.eq(commentId), Mockito.any(CommentDTO.Request.class))).willReturn(response);

        mockMvc.perform(put("/comments/{id}", commentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("댓글 내용")));
    }

    @Test
    @DisplayName("댓글 삭제 API 테스트")
    public void deleteComment() throws Exception {
        Long commentId = 1L;

        Mockito.doNothing().when(commentService).deleteComment(commentId);

        mockMvc.perform(delete("/comments/{id}", commentId))
                .andExpect(status().isNoContent());
    }
}