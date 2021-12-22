package com.bzavoevanyy.service;

import com.bzavoevanyy.dao.BookDao;
import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DisplayName("Book service test")
class BookServiceImplTest {

    private final BookDao bookDao = Mockito.mock(BookDao.class);
    private final BookService bookService = new BookServiceImpl(bookDao);

    @Test
    @DisplayName(" should call BookDao.getAll and return list books")
    void getAll() {
        when(bookDao.getAll()).thenReturn(List.of(
                new Book(1L, "test_book", null, null),
                new Book(2L, "test_book2", null, null)));
        final var books = bookService.getAll();

        verify(bookDao, times(1)).getAll();
        assertThat(books).isNotNull().hasSize(2).extracting(Book::getId, Book::getTitle)
                .containsExactly(tuple(1L, "test_book"), tuple(2L, "test_book2"));
    }

    @Test
    @DisplayName(" should call BookDao.getBookById and return book")
    void getBookById() {
        when(bookDao.getById(anyLong())).thenReturn(new Book(3L, "test_book", null, null));
        assertThat(bookService.getBookById(3L)).isNotNull().hasSize(1).
                first().extracting(Book::getId, Book::getTitle).containsExactly(3L, "test_book");

    }

    @Test
    @DisplayName(" should create Book object and call BookDao.insert with right argument")
    void createBook() {
        final Map<String, Object> testMap = Map.of("book_id", 1L,
                "book_title", "test_book",
                "author", new Author(1L, "test_author"),
                "genre", new Genre(1L, "test_genre"));
        bookService.createBook(testMap);

        verify(bookDao, times(1)).insert(new Book(null, "test_book", new Author(1L, "test_author"),
                new Genre(1L, "test_genre")));

    }

    @Test
    @DisplayName(" should call BookDao.deleteById with right argument")
    void deleteBookById() {
        bookService.deleteBookById(1L);

        verify(bookDao, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName(" should create Book object and call BookDao.update with right argument")
    void updateBook() {
        final Map<String, Object> testMap = Map.of("book_id", 1L,
                "book_title", "test_book",
                "author", new Author(1L, "test_author"),
                "genre", new Genre(1L, "test_genre"));
        bookService.updateBook(testMap);

        verify(bookDao, times(1)).update(new Book(1L, "test_book", new Author(1L, "test_author"),
                new Genre(1L, "test_genre")));
    }

}