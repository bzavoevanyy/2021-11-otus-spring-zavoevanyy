package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.CommentDto;
import com.bzavoevanyy.controllers.dto.ErrorResponse;
import com.bzavoevanyy.controllers.dto.SuccessResponse;
import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Comment;
import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.services.CommentService;
import com.bzavoevanyy.services.exceptions.CommentNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CommentController.class)
@DisplayName("CommentController test should")
class CommentControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CommentService commentService;
    private final static Author TEST_AUTHOR = new Author(1L, "test author");
    private final static Genre TEST_GENRE = new Genre(1L, "test genre");
    private final static Book TEST_BOOK = new Book(1L, "test book", TEST_AUTHOR, TEST_GENRE);
    private final static Comment TEST_COMMENT = new Comment(1L, "test comment", LocalDate.now(), TEST_BOOK);

    @Test
    @DisplayName("return json with comments by book id")
    void shouldReturnJsonWithCommentsByBookId() throws Exception {
        given(commentService.findAllByBookId(anyLong())).willReturn(List.of(TEST_COMMENT));
        val expectedResult = Stream.of(TEST_COMMENT)
                .map(CommentDto::toDto).collect(Collectors.toList());
        mvc.perform(get("/comment")
                        .param("bookId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("create comment and return json with new comment")
    void shouldCreateCommentAndReturnJsonWithNewComment() throws Exception {
        given(commentService.createComment(anyString(), anyLong())).willReturn(TEST_COMMENT);
        val expectedResult = mapper.writeValueAsString(CommentDto.toDto(TEST_COMMENT));
        val requestBody = CommentDto.builder()
                .comment("test comment")
                .bookId(1L).build();
        mvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("update comment and return json with updated comment")
    void shouldUpdateCommentAndReturnJsonWithUpdatedComment() throws Exception {
        given(commentService.updateById(anyLong(), anyString())).willReturn(TEST_COMMENT);
        val expectedResult = mapper.writeValueAsString(CommentDto.toDto(TEST_COMMENT));
        val requestBody = CommentDto.builder()
                .comment("test comment")
                .bookId(1L).build();
        mvc.perform(put("/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("delete comment and return correct message")
    void shouldDeleteCommentAndReturnCorrectMessage() throws Exception {
        doNothing().when(commentService).deleteById(anyLong());
        val expectedResult = mapper.writeValueAsString(new SuccessResponse("deleted"));
        mvc.perform(delete("/comment/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("return correct message when comment not found")
    void shouldReturnCorrectMessageWhenCommentNotFound() throws Exception {
        doThrow(CommentNotFoundException.class).when(commentService).deleteById(anyLong());
        val expectedResult = mapper.writeValueAsString(new ErrorResponse("Comment not found"));
        mvc.perform(delete("/comment/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedResult));
    }
}