package com.bzavoevanyy.service;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.repository.*;
import com.bzavoevanyy.service.exceptions.BookNotFoundException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("BookService test")
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration," +
                "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
                "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration"
})
class BookServiceImplTest {
    @Autowired
    private BookService bookService;
    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private SequenceGeneratorServiceImpl generatorService;
    @MockBean
    private CustomBookRepositoryImpl customBookRepository;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("mongock.enabled", () -> "false");
    }

    @Test
    @DisplayName(" should throw BookNotFound exception")
    void shouldThrowBookNotFoundException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.getBookById(1L)).isInstanceOf(BookNotFoundException.class);
    }

    @Test
    @DisplayName(" should call save method with right args")
    void shouldCallSaveMethodWithRightArgs() {
        when(generatorService.generateSequence(anyString())).thenReturn(1L);
        val author = new Author(1L, "author");
        val genre = new Genre(1L, "genre");
        bookService.createBook("book", author, genre);
        verify(bookRepository, times(1)).save(new Book(1L, "book", author, genre));
    }
    @Test
    @DisplayName(" should call save method for update with right args")
    void shouldCallSaveMethodForUpdateWithRightArgs() {
        val author = new Author(1L, "author");
        val genre = new Genre(1L, "genre");
        val book = new Book(1L, "book", author, genre);
        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(book));
        val newAuthor = new Author(2L, "author");
        val newGenre = new Genre(2L, "genre");
        bookService.updateBook(1L, "new title", newAuthor, newGenre);
        verify(bookRepository, times(1))
                .save(new Book(1L, "new title", newAuthor, newGenre));
    }
}