package com.bzavoevanyy.controllers;

import com.bzavoevanyy.services.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@DisplayName("CommentController test should ")
class CommentControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName(" invoke createComment with right args and redirect to GET /book/{id}")
    void shouldInvokeCreateCommentAndRedirect() throws Exception{
        mvc.perform(post("/comment")
                        .param("comment", "test")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
        verify(commentService, times(1)).createComment("test", 1L);
    }
    @Test
    @DisplayName(" redirect with errMsg if wrong comment for create")
    void shouldRedirectWithErrMsgForCreate() throws Exception{
        mvc.perform(post("/comment")
                        .param("comment", "t")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errMsg", "Input data is incorrect"));
        verify(commentService, times(0)).createComment(anyString(), anyLong());
    }

    @Test
    @DisplayName(" invoke updateById with right args and redirect to GET /book/{id}")
    void shouldInvokeUpdateByIdAndRedirect() throws Exception{
        mvc.perform(post("/comment/1")
                        .param("comment", "test"))
                .andExpect(status().is3xxRedirection());
        verify(commentService, times(1)).updateById(1L, "test");
    }

    @Test
    @DisplayName(" redirect with errMsg if wrong comment for update")
    void shouldRedirectWithErrMsgForUpdate() throws Exception{
        mvc.perform(post("/comment/1")
                        .param("comment", "t"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errMsg", "Input data is incorrect"));
        verify(commentService, times(0)).updateById(anyLong(), anyString());
    }

    @Test
    @DisplayName(" invoke deleteById with right args")
    void shouldDeleteCommentById() throws Exception{
        mvc.perform(post("/comment/delete/1"))
                .andExpect(status().is3xxRedirection());
        verify(commentService, times(1)).deleteById(1L);
    }
}