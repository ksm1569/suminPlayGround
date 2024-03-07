package com.smsoft.playgroundbe.api.exceptiontest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsoft.playgroundbe.api.exceptiontest.dto.BindExceptionTestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ExceptionTestController.class)
public class ExceptionTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("바인딩 예외 GET 메서드 테스트")
    public void bindExceptionTest() throws Exception {
        mockMvc.perform(get("/api/exception/bind-exception-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // 빈 객체 전송으로 @Valid 검증 실패 유도
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    @DisplayName("바인딩 예외 POST 메서드 테스트")
    public void testPostBindException() throws Exception {
        BindExceptionTestDTO bindExceptionTestDTO = new BindExceptionTestDTO();
        bindExceptionTestDTO.setValue2("11");

        String requestBody = objectMapper.writeValueAsString(bindExceptionTestDTO);

        mockMvc.perform(post("/api/exception/bind-exception-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    @DisplayName("타입 미스매치 예외 테스트")
    public void typeMismatchExceptionTest() throws Exception {
        mockMvc.perform(get("/api/exception/type-exception-test")
                        .param("testEnum", "INVALID_VALUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    @DisplayName("비즈니스 로직 에러 테스트")
    public void businessExceptionTest() throws Exception {
        mockMvc.perform(get("/api/exception/business-exception-test")
                        .param("isError", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is("001")))
                .andExpect(jsonPath("$.errorMessage", is("business exception test")));
    }

    @Test
    @DisplayName("기타 예외 테스트")
    public void exceptionTest() throws Exception {
        mockMvc.perform(get("/api/exception/exception-test")
                        .param("isError", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is("500 INTERNAL_SERVER_ERROR")))
                .andExpect(jsonPath("$.errorMessage", is("예외 테스트")));
    }
}