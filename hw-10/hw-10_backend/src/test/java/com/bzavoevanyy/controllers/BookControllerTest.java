package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.*;
import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.services.AuthorService;
import com.bzavoevanyy.services.BookService;
import com.bzavoevanyy.services.GenreService;
import com.bzavoevanyy.services.exceptions.BookNotFoundException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@DisplayName("BookController test should ")
class BookControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;


    private final static Author TEST_AUTHOR = new Author(1L, "test author");
    private final static Genre TEST_GENRE = new Genre(1L, "test genre");
    private final static Book TEST_BOOK = new Book(1L, "test book", TEST_AUTHOR, TEST_GENRE);

    @Test
    @DisplayName("return json with all books")
    void shouldReturnJsonWithAllBooks() throws Exception {

        given(bookService.getAll()).willReturn(List.of(TEST_BOOK));
        val expectedResult = Stream.of(TEST_BOOK).map(BookDto::toDto).collect(Collectors.toList());
        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("return json with book by id")
    void shouldReturnJsonWithBookById() throws Exception {
        given(bookService.getBookById(1L)).willReturn(TEST_BOOK);
        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(BookDto.toDto(TEST_BOOK))));
    }

    @Test
    @DisplayName("return json with error message - book not found")
    void shouldReturnErrorMessageBookNotFound() throws Exception {
        given(bookService.getBookById(1L)).willThrow(BookNotFoundException.class);
        val expectedResult = new ErrorResponse("Book not found");
        mvc.perform(get("/book/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("invoke createBook and return json with new book")
    void shouldInvokeCreateBookAndReturnJsonWithNewBook() throws Exception {
        given(authorService.getAuthorById(anyLong())).willReturn(TEST_AUTHOR);
        given(genreService.getGenreById(anyLong())).willReturn(TEST_GENRE);
        given(bookService.createBook(anyString(), any(), any())).willReturn(TEST_BOOK);
        val requestBody = mapper.writeValueAsString(BookDto.toDto(TEST_BOOK));
        mvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody));
        verify(bookService, times(1))
                .createBook("test book", TEST_AUTHOR, TEST_GENRE);
    }

    @Test
    @DisplayName("invoke updateBook and return json with updated book")
    void shouldInvokeUpdateBookAndReturnCorrectJson() throws Exception {
        given(authorService.getAuthorById(anyLong())).willReturn(TEST_AUTHOR);
        given(genreService.getGenreById(anyLong())).willReturn(TEST_GENRE);
        given(bookService.updateBook(anyLong(), anyString(), any(), any())).willReturn(TEST_BOOK);
        val requestBody = mapper.writeValueAsString(BookDto.toDto(TEST_BOOK));
        mvc.perform(put("/book/1").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(requestBody));
        verify(bookService, times(1))
                .updateBook(1L, "test book", TEST_AUTHOR, TEST_GENRE);
    }

    @Test
    @DisplayName("invoke deleteBookByID and return json with correct message")
    void shouldInvokeDeleteBookByIdAndReturnJsonWithCorrectMessage() throws Exception {

        doNothing().when(bookService).deleteBookById(anyLong());
        final var successResponse = new SuccessResponse("deleted");
        mvc.perform(delete("/book/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(successResponse)));
        verify(bookService, times(1)).deleteBookById(1L);
    }
}