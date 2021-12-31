package com.bzavoevanyy.repositories.Impl;

import com.bzavoevanyy.entities.Author;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(AuthorRepositoryJpa.class)
@DisplayName("AuthorRepository test")
class AuthorRepositoryJpaTest {
    @Autowired
    private AuthorRepositoryJpa repositoryJpa;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName(" should return author by author_id correctly")
    void shouldFindExpectedAuthorById() {
        val optionalActualAuthor = repositoryJpa.findById(1L);
        val expectedAuthor = em.find(Author.class, 1L);
        assertThat(optionalActualAuthor).isPresent().get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    @DisplayName(" should return List of all Authors")
    void shouldReturnListOfAllAuthors() {
        val authors = repositoryJpa.findAll();
        assertThat(authors).extracting(Author::getId, Author::getName)
                .containsExactly(
                        tuple(1L, "author1"),
                        tuple(2L, "author2"),
                        tuple(3L, "author3"));
    }

    @Test
    @DisplayName(" should save author in db")
    void shouldSaveAuthorInfo() {
        val savedAuthor = repositoryJpa.save(new Author(null, "test_author", null));

        assertThat(savedAuthor.getId()).isNotNull().isGreaterThan(0);

        val actualAuthor = em.find(Author.class, savedAuthor.getId());
        assertThat(actualAuthor).isNotNull().extracting(Author::getName).isEqualTo("test_author");
    }

    @Test
    @DisplayName(" should delete author by id")
    void shouldDeleteAuthorById() {
        val author = em.find(Author.class, 3L);
        em.detach(author);
        assertThat(author).isNotNull();

        repositoryJpa.deleteById(3L);
        val deletedAuthor = em.find(Author.class, 3L);
        assertThat(deletedAuthor).isNull();
    }
}