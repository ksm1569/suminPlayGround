package com.smsoft.playgroundbe.domain.board.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsoft.playgroundbe.domain.board.dto.PostDTO;
import com.smsoft.playgroundbe.domain.board.entity.Post;
import com.smsoft.playgroundbe.domain.board.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostController.class)
@ActiveProfiles("test")
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("게시글 생성 API 테스트")
    public void createPost() throws Exception {
        PostDTO.Request postDTO = new PostDTO.Request();
        postDTO.setAuthorId("작성자");
        postDTO.setTitle("제목");
        postDTO.setContent("내용");
        postDTO.setBoardId(1L);

        PostDTO.Response response = new PostDTO.Response(1L, "작성자", "제목", "내용", "축구");

        given(postService.createPost(Mockito.any(PostDTO.Request.class))).willReturn(response);

        mockMvc.perform(post("/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(response.getTitle())));
    }

    @Test
    @DisplayName("게시글 수정 API 테스트")
    public void updatePost() throws Exception {
        Long postId = 1L;

        PostDTO.Request postDTO = new PostDTO.Request();
        postDTO.setAuthorId("작성자");
        postDTO.setTitle("제목");
        postDTO.setContent("내용");
        postDTO.setBoardId(1L);

        PostDTO.Response response = new PostDTO.Response(1L, "작성자", "제목", "내용", "시사");

        given(postService.updatePost(Mockito.eq(postId), any(PostDTO.Request.class))).willReturn(response);

        mockMvc.perform(put("/posts/{id}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("제목")));
    }

    @Test
    @DisplayName("게시글 삭제 API 테스트")
    public void deletePost() throws Exception {
        Long postId = 1L;

        Mockito.doNothing().when(postService).deletePost(postId);

        mockMvc.perform(delete("/posts/{id}", postId))
                .andExpect(status().isNoContent());
    }
    
    @Test
    @DisplayName("게시글 전체 조회 API 테스트")
    public void getPosts() throws Exception {
        List<PostDTO.Response> posts = Arrays.asList(
                new PostDTO.Response(1L, "작성자1", "제목1", "내용1", "스포츠"),
                new PostDTO.Response(2L, "작성자2", "제목2", "내용2", "스포츠")
        );

        given(postService.getAllPosts()).willReturn(posts);

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("제목1")))
                .andExpect(jsonPath("$[1].content", is("내용2")))
                .andExpect(jsonPath("$[0].boardTitle", is("스포츠")));
    }
}