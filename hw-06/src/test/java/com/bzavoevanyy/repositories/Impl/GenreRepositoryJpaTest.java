package com.bzavoevanyy.repositories.Impl;

import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.repositories.GenreRepository;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;


import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(GenreRepositoryJpa.class)
@DisplayName("Genre jpa repository test")
class GenreRepositoryJpaTest {
    @Autowired
    private EntityManager em;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName(" should find and return genre by id")
    void shouldFindGenreById() {
        val genre = genreRepository.findById(1L);
        assertThat(genre).isNotNull().get().extracting(Genre::getName).isEqualTo("genre1");
    }

    @Test
    @DisplayName(" should return List of all genres")
    void shouldReturnAllGenres() {
        val genres = genreRepository.findAll();
        assertThat(genres).extracting(Genre::getId, Genre::getName).containsExactly(
                tuple(1L, "genre1"),
                tuple(2L, "genre2"),
                tuple(3L, "genre3"));
    }

    @Test
    @DisplayName(" should insert new genre to db")
    void shouldInsertGenre() {
        val testGenre = new Genre(null, "test genre", new ArrayList<>());
        genreRepository.save(testGenre);
        em.detach(testGenre);
        val expectedGenre = em.find(Genre.class, 4L);

        assertThat(testGenre).usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    void deleteById() {
        val genre = em.find(Genre.class, 3L);

        assertThat(genre).isNotNull();

        genreRepository.deleteById(3L);
        val deletedGenre = em.find(Genre.class, 3L);

        assertThat(deletedGenre).isNull();
    }
}