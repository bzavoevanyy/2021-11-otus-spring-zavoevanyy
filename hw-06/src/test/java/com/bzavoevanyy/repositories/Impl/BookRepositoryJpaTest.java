package com.bzavoevanyy.repositories.Impl;

import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.repositories.BookRepository;
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
@Import(BookRepositoryJpa.class)
@DisplayName("BookRepositoryJpa test")
class BookRepositoryJpaTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName(" should return book by id")
    void shouldReturnBookById() {
        val book = bookRepository.findById(1L);
        assertThat(book).isPresent().get().extracting(Book::getId, Book::getTitle)
                .containsExactly(1L, "book1");
    }

    @Test
    @DisplayName(" should return list of all books")
    void shouldReturnListOfAllBooks() {
        val books = bookRepository.findAll();
        assertThat(books).isNotNull().extracting(Book::getId, Book::getTitle)
                .containsExactly(
                        tuple(1L, "book1"),
                        tuple(2L, "book2"),
                        tuple(3L, "book3"));
    }

    @Test
    @DisplayName(" should save new book on db")
    void shouldSaveBook() {
        val author = em.find(Author.class, 1L);
        val genre = em.find(Genre.class, 1L);
        val newBook = new Book(null, "new book", author, genre, new ArrayList<>());
        bookRepository.save(newBook);
        assertThat(newBook.getId()).isNotNull().isGreaterThan(0);
    }

    @Test
    @DisplayName(" should update book")
    void shouldUpdateBook() {
        val book = em.find(Book.class, 1L);
        em.detach(book);
        book.setTitle("new book");
        bookRepository.save(book);
        val updatedBook = em.find(Book.class, 1L);
        assertThat(updatedBook.getTitle()).isEqualTo("new book");
    }

    @Test
    @DisplayName(" should delete book from db")
    void shouldDeleteBookFromDb() {
        val book = em.find(Book.class, 3L);
        assertThat(book).isNotNull();
        em.detach(book);
        bookRepository.deleteById(book.getId());
        val deletedBook = em.find(Book.class, 3L);
        assertThat(deletedBook).isNull();
    }
}