package com.smsoft.playgroundbe.domain.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smsoft.playgroundbe.domain.board.dto.CategoryDTO;
import com.smsoft.playgroundbe.domain.board.service.CategoryService;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
@ActiveProfiles("test")
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("카테고리 생성 API 테스트")
    public void createCategory() throws Exception {
        CategoryDTO.Request requestDTO = new CategoryDTO.Request();
        requestDTO.setName("기술");
        CategoryDTO.Response responseDTO = new CategoryDTO.Response(1L, "기술");

        given(categoryService.createCategory(Mockito.any(CategoryDTO.Request.class))).willReturn(responseDTO);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("기술")));
    }

    @Test
    @DisplayName("카테고리 업데이트 API 테스트")
    public void updateCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDTO.Request requestDTO = new CategoryDTO.Request();
        requestDTO.setName("신기술");
        CategoryDTO.Response responseDTO = new CategoryDTO.Response(categoryId, "신기술");

        given(categoryService.updateCategory(Mockito.eq(categoryId), Mockito.any(CategoryDTO.Request.class))).willReturn(responseDTO);

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("신기술")));
    }

    @Test
    @DisplayName("카테고리 삭제 API 테스트")
    public void deleteCategory() throws Exception {
        Long categoryId = 1L;

        Mockito.doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("카테고리 조회 API 테스트")
    public void getCategories() throws Exception {
        List<CategoryDTO.Response> categories = Arrays.asList(new CategoryDTO.Response(1L, "기술"), new CategoryDTO.Response(2L, "스포츠"));

        given(categoryService.getAllCategories()).willReturn(categories);

        mockMvc.perform(get("/categories")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("기술")))
                .andExpect(jsonPath("$[1].name", is("스포츠")));
    }
}