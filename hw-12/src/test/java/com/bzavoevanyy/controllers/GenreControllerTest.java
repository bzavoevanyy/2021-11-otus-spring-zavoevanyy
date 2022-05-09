package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.GenreDto;
import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.repositories.UserRepository;
import com.bzavoevanyy.services.GenreService;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
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

@WebMvcTest(GenreController.class)
@DisplayName("GenreController test should")
class GenreControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private GenreService genreService;
    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName(" return correct model and view for GET request /genre")
    void shouldReturnCorrectModelAndViewForGetAllGenres() throws Exception {
        val testGenre = new Genre(1L, "test genre");
        given(genreService.getAll()).willReturn(List.of(testGenre));
        mvc.perform(get("/genre"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("genres", List.of(GenreDto.toDto(testGenre))))
                .andExpect(view().name("genre"));
    }

    @Test
    @DisplayName(" redirect to login page when unauthorized for GET request /genre")
    void shouldRedirectToLoginPageWhenUnauthorizedForGetAllGenres() throws Exception {
        val testGenre = new Genre(1L, "test genre");
        given(genreService.getAll()).willReturn(List.of(testGenre));
        mvc.perform(get("/genre"))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName(" invoke createGenre with right args and redirect to GET /genre")
    void shouldInvokeCreateAuthorAndRedirect() throws Exception{
        mvc.perform(post("/genre")
                        .param("genreName", "test"))
                .andExpect(status().is3xxRedirection());
        verify(genreService, times(1)).createGenre("test");
    }

    @Test
    @DisplayName(" redirect to login page when unauthorized for POST request /genre")
    void shouldRedirectToLoginPageWhenUnauthorizedForCreateGenres() throws Exception {
        mvc.perform(post("/genre")
                        .param("genreName", "test"))
                .andExpect(status().is3xxRedirection());
        verify(genreService, times(0)).createGenre("test");
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName(" redirect with errMsg if wrong genreName for create")
    void shouldRedirectWithErrMsgForCreate() throws Exception{
        mvc.perform(post("/genre")
                        .param("genreName", "t"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errMsg", "Input data is incorrect"));
        verify(genreService, times(0)).createGenre(anyString());
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName(" invoke updateGenre with right args and redirect to GET /genre")
    void shouldInvokeUpdateGenreAndRedirect() throws Exception{
        mvc.perform(post("/genre/1")
                        .param("genreName", "test"))
                .andExpect(status().is3xxRedirection());
        verify(genreService, times(1)).updateGenre(1L, "test");
    }

    @Test
    @DisplayName(" redirect to login page when unauthorized for POST request /genre/{id}")
    void shouldRedirectToLoginPageWhenUnauthorizedForUpdateGenres() throws Exception {
        mvc.perform(post("/genre/1")
                        .param("genreName", "test"))
                .andExpect(status().is3xxRedirection());
        verify(genreService, times(0)).createGenre("test");
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName(" redirect with errMsg if wrong genreName for update")
    void shouldRedirectWithErrMsgForUpdate() throws Exception{
        mvc.perform(post("/genre/1")
                        .param("genreName", "t"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("errMsg", "Input data is incorrect"));
        verify(genreService, times(0)).updateGenre(anyLong(), anyString());
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName(" invoke deleteGenreById with right args")
    void shouldDeleteGenreById() throws Exception{
        given(bookRepository.countAllByGenreId(anyLong())).willReturn(0L);
        mvc.perform(post("/genre/delete/1")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
        verify(genreService, times(1)).deleteGenreById(1L);
    }

    @Test
    @DisplayName(" redirect to login page when unauthorized for POST request /genre/delete/{id}")
    void shouldRedirectToLoginPageWhenUnauthorizedForDeleteGenres() throws Exception {
        mvc.perform(post("/genre/delete/1")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
        verify(genreService, times(0)).createGenre("test");
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @DisplayName(" redirect with errMsg if genre has books")
    void shouldRedirectWithErrMsgIfGenreHasBooks() throws Exception{
        given(bookRepository.countAllByGenreId(anyLong())).willReturn(1L);
        mvc.perform(post("/genre/delete/1")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash()
                        .attribute("errMsg", "There is/are 1 book(s) by this genre." +
                                " Please firstly remove it"));
        verify(genreService, times(0)).deleteGenreById(anyLong());
    }
}