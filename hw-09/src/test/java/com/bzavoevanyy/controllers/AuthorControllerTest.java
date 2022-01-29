package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.AuthorDto;
import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.services.AuthorService;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
@DisplayName("AuthorController test should ")
class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;
    @MockBean
    private BookRepository bookRepository;

    @Test
    @DisplayName(" return correct model and view for GET request /author")
    void shouldReturnCorrectModelAndViewForGetAllAuthors() throws Exception {
        val testAuthor = new Author(1L, "test author");
        given(authorService.getAll()).willReturn(List.of(testAuthor));
        mvc.perform(get("/author"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attribute("authors", List.of(AuthorDto.toDto(testAuthor))))
                .andExpect(view().name("author"));
    }

    @Test
    @DisplayName(" invoke createAuthor with right args and redirect to GET /author")
    void shouldInvokeCreateAuthorAndRedirect() throws Exception{
        mvc.perform(post("/author")
                        .param("authorName", "test"))
                .andExpect(status().is3xxRedirection());
        verify(authorService, times(1)).createAuthor("test");
    }
    @Test
    @DisplayName(" redirect with errMsg if wrong authorName for create")
    void shouldRedirectWithErrMsgForCreate() throws Exception{
        mvc.perform(post("/author")
                        .param("authorName", "t"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errMsg", "Input data is incorrect"));
        verify(authorService, times(0)).createAuthor(anyString());
    }

    @Test
    @DisplayName(" invoke updateAuthor with right args and redirect to GET /author")
    void shouldInvokeUpdateAuthorAndRedirect() throws Exception{
        mvc.perform(post("/author/1")
                        .param("authorName", "test"))
                .andExpect(status().is3xxRedirection());
        verify(authorService, times(1)).updateAuthor(1L, "test");
    }

    @Test
    @DisplayName(" redirect with errMsg if wrong authorName for update")
    void shouldRedirectWithErrMsgForUpdate() throws Exception{
        mvc.perform(post("/author/1")
                        .param("authorName", "t"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errMsg", "Input data is incorrect"));
        verify(authorService, times(0)).updateAuthor(anyLong(), anyString());
    }

    @Test
    @DisplayName(" invoke deleteAuthorById with right args")
    void shouldDeleteAuthorById() throws Exception{
        given(bookRepository.countAllByAuthorId(anyLong())).willReturn(0L);
        mvc.perform(post("/author/delete/1")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
        verify(authorService, times(1)).deleteAuthorById(1L);
    }
    @Test
    @DisplayName(" redirect with errMsg if author has books")
    void shouldRedirectWithErrMsgIfAuthorHasBooks() throws Exception{
        given(bookRepository.countAllByAuthorId(anyLong())).willReturn(1L);
        mvc.perform(post("/author/delete/1")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash()
                        .attribute("errMsg", "There is/are 1 book(s) by this author." +
                                " Please firstly remove it"));
        verify(authorService, times(0)).deleteAuthorById(anyLong());
    }
}