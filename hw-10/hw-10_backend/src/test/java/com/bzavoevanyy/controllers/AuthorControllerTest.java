package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.AuthorDto;
import com.bzavoevanyy.controllers.dto.ErrorResponse;
import com.bzavoevanyy.controllers.dto.SuccessResponse;
import com.bzavoevanyy.controllers.exceptions.AuthorHasBookException;
import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.services.AuthorService;
import com.bzavoevanyy.services.exceptions.AuthorNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
@DisplayName("AuthorController test should ")
class AuthorControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookRepository bookRepository;

    private final static Author TEST_AUTHOR = new Author(1L, "test author");

    @Test
    @DisplayName("return json with all authors")
    void shouldReturnJsonWithAllAuthors() throws Exception {
        given(authorService.getAll()).willReturn(List.of(TEST_AUTHOR));
        val expectedResult = Stream.of(TEST_AUTHOR).map(AuthorDto::toDto).collect(Collectors.toList());
        mvc.perform(get("/author"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("create author and return json with new author")
    void shouldCreateAuthorAndReturnNewAuthor() throws Exception {
        given(authorService.createAuthor(anyString())).willReturn(TEST_AUTHOR);
        val requestBody = "{\"authorName\":\"test author\"}";
        mvc.perform(post("/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(AuthorDto.toDto(TEST_AUTHOR))));
    }

    @Test
    @DisplayName("update Author and return updated author")
    void shouldUpdateAuthorAndReturnCorrectJson() throws Exception {
        given(authorService.updateAuthor(anyLong(), anyString())).willReturn(TEST_AUTHOR);
        val requestBody = "{\"authorName\":\"test author\"}";
        mvc.perform(put("/author/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(AuthorDto.toDto(TEST_AUTHOR))));
    }

    @Test
    @DisplayName("delete author by id and return correct message")
    void shouldDeleteAuthorByIdAndReturnCorrectMessage() throws Exception {
        doNothing().when(authorService).deleteAuthorById(anyLong());
        val expectedResult = mapper.writeValueAsString(new SuccessResponse("deleted"));
        mvc.perform(delete("/author/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("return correct message when author not found")
    void shouldReturnCorrectMessageWhenAuthorNotFound() throws Exception {
        doThrow(AuthorNotFoundException.class).when(authorService).deleteAuthorById(anyLong());
        val expectedResult = mapper.writeValueAsString(new ErrorResponse("Author not found"));
        mvc.perform(delete("/author/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("return correct message when author has books")
    void shouldReturnCorrectMessageWhenAuthorHasBook() throws Exception{
        doThrow(AuthorHasBookException.class).when(authorService).deleteAuthorById(anyLong());
        val expectedResult = mapper
                .writeValueAsString(new ErrorResponse("Author has book, please firstly delete it"));
        mvc.perform(delete("/author/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedResult));
    }
}