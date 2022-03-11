package com.bzavoevanyy.services.Impl;

import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.services.BookService;
import com.bzavoevanyy.services.exceptions.BookNotFoundException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("BookService test")
class BookServiceImplTest {
    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;

    @Test
    @DisplayName(" should throw BookNotFoundException")
    void shouldThrowBookNotFoundException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrowsExactly(BookNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    @DisplayName(" updateBook method should call save method with right argument")
    void shouldCallSaveMethodWithRightArgForUpdate() {
        val author = new Author(null, "author1");
        val genre = new Genre(null, "genre1");
        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Book(1L, "test book", author, genre)));
        bookService.updateBook(1L, "updated book", author, genre);
        val book = new Book(1L, "updated book", author, genre);
        verify(bookRepository, times(1))
                .save(refEq(book));
    }
    @Test
    @DisplayName(" createBook method should call save method with right argument")
    void shouldCallSaveMethodWithRightArgForSave() {
        val author = new Author(1L, "author1");
        val genre = new Genre(1L, "genre1");
        val book = new Book(null, "created book", author, genre);
        bookService.createBook("created book", author, genre);

        verify(bookRepository, times(1)).save(refEq(book));
    }
}