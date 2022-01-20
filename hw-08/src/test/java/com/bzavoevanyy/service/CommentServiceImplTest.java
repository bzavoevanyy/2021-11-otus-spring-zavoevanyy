package com.bzavoevanyy.service;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Comment;
import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.repository.*;
import com.bzavoevanyy.service.exceptions.BookNotFoundException;
import com.bzavoevanyy.service.exceptions.CommentNotFoundException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("CommentService test")
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration," +
                "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
                "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration"
})
class CommentServiceImplTest {
    @Autowired
    private CommentService commentService;
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
        assertThatThrownBy(() -> commentService.createComment("", 1L))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    @DisplayName(" should call save method with right args")
    void shouldCallSaveMethodWithRightArgs() {
        val comment = new Comment(1L, 1L, "new comment", LocalDateTime.now());
        val author = new Author(1L, "author");
        val genre = new Genre(1L, "genre");
        val book = new Book(1L, "book", author, genre);
        when(generatorService.generateSequence(anyString())).thenReturn(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        commentService.createComment("new comment", 1L);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName(" should throw CommentNotFound exception")
    void shouldThrowCommentNotFoundException() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> commentService.updateById(1L, ""))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName(" should call save method for update with right args")
    void shouldCallSaveMethodForUpdateWithRightArgs() {
        val comment = new Comment(1L, 1L, "new comment", LocalDateTime.now());
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        commentService.updateById(1L, "updated comment");
        verify(commentRepository, times(1))
                .save(new Comment(1L, 1L, "updated comment", LocalDateTime.now()));
    }

}