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

import org.hamcrest.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @DisplayName("bind-exception test - 바인딩 예외 GET 메서드 테스트")
    public void bindExceptionTest() throws Exception {
        mockMvc.perform(get("/api/exception/bind-exception-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // 빈 객체 전송으로 @Valid 검증 실패 유도
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    @DisplayName("bind-exception test - Required 값 미입력 시 예외 테스트")
    public void testPostRequiredFieldValidation() throws Exception {
        BindExceptionTestDTO dto = new BindExceptionTestDTO();
        dto.setValue2(9);

        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/exception/bind-exception-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", containsString("해당 값은 필수입니다.")));
    }

    @Test
    @DisplayName("bind-exception test - Max 값 초과 예외 테스트")
    public void testPostMaxValidation() throws Exception {
        BindExceptionTestDTO dto = new BindExceptionTestDTO();
        dto.setValue1("값1");
        dto.setValue2(11);

        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/exception/bind-exception-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage", containsString("최대 입력값은 10자리 입니다.")));

    }

    @Test
    @DisplayName("bind-exception test - 정상 입력값 테스트")
    public void testPostSuccess() throws Exception {
        BindExceptionTestDTO dto = new BindExceptionTestDTO();
        dto.setValue1("값1");
        dto.setValue2(4);

        String requestBody = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/exception/bind-exception-test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(is("OK")));
    }

    @Test
    @DisplayName("type-exception-test - 타입 미스매치 예외 테스트")
    public void typeMismatchExceptionTest() throws Exception {
        mockMvc.perform(get("/api/exception/type-exception-test")
                        .param("testEnum", "INVALID_VALUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("400 BAD_REQUEST")))
                .andExpect(jsonPath("$.errorMessage").exists());
    }

    @Test
    @DisplayName("business-exception-test - 비즈니스 로직 에러 테스트")
    public void businessExceptionTest() throws Exception {
        mockMvc.perform(get("/api/exception/business-exception-test")
                        .param("isError", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is("001")))
                .andExpect(jsonPath("$.errorMessage", is("business exception test")));
    }

    @Test
    @DisplayName("exception-test - 기타 예외 테스트")
    public void exceptionTest() throws Exception {
        mockMvc.perform(get("/api/exception/exception-test")
                        .param("isError", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode", is("500 INTERNAL_SERVER_ERROR")))
                .andExpect(jsonPath("$.errorMessage", is("예외 테스트")));
    }
}