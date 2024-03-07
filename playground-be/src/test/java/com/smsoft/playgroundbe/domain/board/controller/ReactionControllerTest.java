package com.smsoft.playgroundbe.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsoft.playgroundbe.domain.board.constant.ReactionType;
import com.smsoft.playgroundbe.domain.board.dto.ReactionDTO;
import com.smsoft.playgroundbe.domain.board.service.ReactionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(ReactionController.class)
class ReactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReactionService reactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Reaction 생성/업데이트 API 테스트")
    public void addOrUpdateReaction() throws Exception {
        // Given
        ReactionDTO.Request reactionDTO = new ReactionDTO.Request();
        reactionDTO.setUserId("user123");
        reactionDTO.setReactionType(ReactionType.LOVE);
        reactionDTO.setPostId(1L);

        ReactionDTO.Response response = ReactionDTO.Response.builder()
                .id(1L)
                .userId("user123")
                .reactionType(ReactionType.LOVE)
                .build();

        given(reactionService.addOrUpdateReaction(any(ReactionDTO.Request.class))).willReturn(response);

        // When & Then
        mockMvc.perform(post("/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reactionDTO)))
                .andExpect(status().isCreated());
    }
}