package com.smsoft.playgroundbe.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsoft.playgroundbe.domain.board.dto.BoardDTO;
import com.smsoft.playgroundbe.domain.board.entity.Category;
import com.smsoft.playgroundbe.domain.board.repository.BoardRepository;
import com.smsoft.playgroundbe.domain.board.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BoardController.class)
@ActiveProfiles("test")
class BoardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("게시판 생성 API 테스트")
    public void createBoard() throws Exception {
        BoardDTO.Request boardDTO = new BoardDTO.Request();
        boardDTO.setTitle("축구");
        boardDTO.setCategoryId(1L);

        BoardDTO.Response responseDTO = new BoardDTO.Response(1L, "축구", "스포츠");

        given(boardService.createBoard(Mockito.any(BoardDTO.Request.class))).willReturn(responseDTO);

        mockMvc.perform(post("/boards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(boardDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("축구")));
    }

    @Test
    @DisplayName("게시판 수정 API 테스트")
    public void updateBoard() throws Exception {
        Long boardId = 1L;
        BoardDTO.Request boardDTO = new BoardDTO.Request();
        boardDTO.setTitle("농구");
        boardDTO.setCategoryId(1L);

        BoardDTO.Response responseDTO = new BoardDTO.Response(1L, "농구", "스포츠");

        given(boardService.updateBoard(Mockito.eq(boardId), Mockito.any(BoardDTO.Request.class))).willReturn(responseDTO);

        mockMvc.perform(put("/boards/{id}", boardId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(boardDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("농구")));
    }

    @Test
    @DisplayName("게시판 삭제 API 테스트")
    public void deleteBoard() throws Exception {
        Long boardId = 1L;

        Mockito.doNothing().when(boardService).deleteBoard(boardId);

        mockMvc.perform(delete("/boards/{id}", boardId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("게시판 전체 조회 API 테스트")
    public void getBoards() throws Exception {
        List<BoardDTO.Response> boards = Arrays.asList(
                new BoardDTO.Response(1L, "축구", "스포츠"),
                new BoardDTO.Response(2L, "농구", "스포츠")
        );

        given(boardService.getAllBoards()).willReturn(boards);

        mockMvc.perform(get("/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("축구")))
                .andExpect(jsonPath("$[0].categoryName", is("스포츠")))
                .andExpect(jsonPath("$[1].title", is("농구")))
                .andExpect(jsonPath("$[1].categoryName", is("스포츠")));
    }
}