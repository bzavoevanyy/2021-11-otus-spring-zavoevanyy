package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.AuthorDto;
import com.bzavoevanyy.controllers.dto.BookDto;
import com.bzavoevanyy.controllers.dto.CommentDto;
import com.bzavoevanyy.controllers.dto.GenreDto;
import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Comment;
import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.services.AuthorService;
import com.bzavoevanyy.services.BookService;
import com.bzavoevanyy.services.CommentService;
import com.bzavoevanyy.services.GenreService;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@DisplayName("BookController test should ")
class BookControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName(" should return correct model and view for GET /book")
    void shouldReturnCorrectModelAndViewForGetAll() throws Exception {
        val author = new Author(1L, "test author");
        val genre = new Genre(1L, "test genre");
        val book = new Book(1L, "test book", author, genre);
        given(authorService.getAll()).willReturn(List.of(author));
        given(genreService.getAll()).willReturn(List.of(genre));
        given(bookService.getAll()).willReturn(List.of(book));
        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", List.of(AuthorDto.toDto(author))))
                .andExpect(model().attribute("genres", List.of(GenreDto.toDto(genre))))
                .andExpect(model().attribute("books", List.of(BookDto.toDto(book))))
                .andExpect(view().name("book"));
    }

    @Test
    @DisplayName(" should return correct model and view for GET /book/{id}")
    void shouldReturnCorrectModelAndViewForGetOne() throws Exception {
        val author = new Author(1L, "test author");
        val genre = new Genre(1L, "test genre");
        val book = new Book(1L, "test book", author, genre);
        val comment = new Comment(1L, "test comment", LocalDate.now(), book);
        given(authorService.getAll()).willReturn(List.of(author));
        given(genreService.getAll()).willReturn(List.of(genre));
        given(bookService.getBookById(anyLong())).willReturn(book);
        given(commentService.findAllByBookId(anyLong())).willReturn(List.of(comment));
        mvc.perform(get("/book/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", List.of(AuthorDto.toDto(author))))
                .andExpect(model().attribute("genres", List.of(GenreDto.toDto(genre))))
                .andExpect(model().attribute("books", List.of(BookDto.toDto(book))))
                .andExpect(model().attribute("comments", List.of(CommentDto.toDto(comment))))
                .andExpect(view().name("book"));
    }

    @Test
    @DisplayName(" should invoke updateBook with right args and redirect")
    void shouldInvokeUpdateBookWithRightArgs() throws Exception {
        val author = new Author(1L, "test author");
        val genre = new Genre(1L, "test genre");
        given(authorService.getAuthorById(1L)).willReturn(author);
        given(genreService.getGenreById(1L)).willReturn(genre);
        mvc.perform(post("/book/1")
                        .param("id", "1")
                        .param("title", "test book")
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().is3xxRedirection());
        verify(bookService, times(1))
                .updateBook(1L, "test book", author, genre);
    }

    @Test
    @DisplayName(" redirect with errMsg if wrong title for update")
    void shouldRedirectWithErrMsgForUpdate() throws Exception {
        mvc.perform(post("/book/1")
                        .param("id", "1")
                        .param("title", "t"))
                .andExpect(flash().attribute("errMsg", "Input data is incorrect"))
                .andExpect(status().is3xxRedirection());
        verify(bookService, times(0))
                .updateBook(anyLong(), anyString(), any(), any());
    }

    @Test
    @DisplayName(" should invoke createBook with right args and redirect")
    void shouldInvokeCreateBookWithRightArgs() throws Exception {
        val author = new Author(1L, "test author");
        val genre = new Genre(1L, "test genre");
        given(authorService.getAuthorById(1L)).willReturn(author);
        given(genreService.getGenreById(1L)).willReturn(genre);
        mvc.perform(post("/book")
                        .param("title", "test book")
                        .param("authorId", "1")
                        .param("genreId", "1"))
                .andExpect(status().is3xxRedirection());
        verify(bookService, times(1))
                .createBook("test book", author, genre);
    }

    @Test
    @DisplayName(" redirect with errMsg if wrong title for create")
    void shouldRedirectWithErrMsgForCreate() throws Exception {
        mvc.perform(post("/book")
                        .param("title", "t"))
                .andExpect(flash().attribute("errMsg", "Input data is incorrect"))
                .andExpect(status().is3xxRedirection());
        verify(bookService, times(0))
                .updateBook(anyLong(), anyString(), any(), any());
    }
    @Test
    @DisplayName(" invoke deleteBookById and redirect")
    void shouldInvokeDeleteBookByIdAndRedirect() throws Exception{
        mvc.perform(post("/book/delete/1"))
                .andExpect(status().is3xxRedirection());
        verify(bookService, times(1)).deleteBookById(1L);
    }
}