package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.ErrorResponse;
import com.bzavoevanyy.controllers.dto.GenreDto;
import com.bzavoevanyy.controllers.dto.SuccessResponse;
import com.bzavoevanyy.controllers.exceptions.GenreHasBookException;
import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.services.GenreService;
import com.bzavoevanyy.services.exceptions.GenreNotFoundException;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreController.class)
@DisplayName("GenreController test should")
class GenreControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private GenreService genreService;
    @MockBean
    private BookRepository bookRepository;

    private final static Genre TEST_GENRE = new Genre(1L, "test genre");

    @Test
    @DisplayName("return json with all genres")
    void shouldReturnJsonWithAllGenres() throws Exception {
        given(genreService.getAll()).willReturn(List.of(TEST_GENRE));
        val expectedResult = Stream.of(TEST_GENRE).map(GenreDto::toDto).collect(Collectors.toList());
        mvc.perform(get("/genre"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedResult)));
    }

    @Test
    @DisplayName("create genre and return json with new genre")
    void shouldCreateGenreAndReturnNewGenre() throws Exception {
        given(genreService.createGenre(anyString())).willReturn(TEST_GENRE);
        val requestBody = "{\"genreName\":\"test genre\"}";
        mvc.perform(post("/genre")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(GenreDto.toDto(TEST_GENRE))));
    }

    @Test
    @DisplayName("update Genre and return updated genre")
    void shouldUpdateGenreAndReturnCorrectJson() throws Exception {
        given(genreService.updateGenre(anyLong(), anyString())).willReturn(TEST_GENRE);
        val requestBody = "{\"genreName\":\"test genre\"}";
        mvc.perform(put("/genre/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(GenreDto.toDto(TEST_GENRE))));
    }

    @Test
    @DisplayName("delete genre by id and return correct message")
    void shouldDeleteGenreByIdAndReturnCorrectMessage() throws Exception {
        doNothing().when(genreService).deleteGenreById(anyLong());
        val expectedResult = mapper.writeValueAsString(new SuccessResponse("deleted"));
        mvc.perform(delete("/genre/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("return correct message when genre not found")
    void shouldReturnCorrectMessageWhenGenreNotFound() throws Exception {
        doThrow(GenreNotFoundException.class).when(genreService).deleteGenreById(anyLong());
        val expectedResult = mapper.writeValueAsString(new ErrorResponse("Genre not found"));
        mvc.perform(delete("/genre/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedResult));
    }

    @Test
    @DisplayName("return correct message when genre has books")
    void shouldReturnCorrectMessageWhenGenreHasBook() throws Exception{
        doThrow(GenreHasBookException.class).when(genreService).deleteGenreById(anyLong());
        val expectedResult = mapper
                .writeValueAsString(new ErrorResponse("Genre has book, please firstly delete it"));
        mvc.perform(delete("/genre/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json(expectedResult));
    }
}