package com.bzavoevanyy.dao;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DisplayName("BookDao test ")
class BookDaoImplTest {

    @Autowired
    private BookDao bookDao;

    @Test
    @DisplayName(" should get right book from db")
    void getById() {
        val book = bookDao.getById(1);
        assertThat(book).isNotNull().extracting(Book::getId, Book::getTitle, Book::getAuthorName, Book::getGenreName)
                .containsExactly(1L, "Казаки", "Достоевский Ф.Н.", "Повесть");
    }

    @Test
    @DisplayName(" should get all books from db")
    void getAll() {
        assertThat(bookDao.getAll()).isNotNull().hasSize(3);
    }

    @Test
    @DisplayName(" should insert book into db")
    void insert() {
        val book = new Book(null, "test_book", new Author(1L, null), new Genre(1L, null));
        bookDao.insert(book);
        val testBook = bookDao.getById(4);
        bookDao.deleteById(4);
        assertThat(testBook).isNotNull().extracting(Book::getId, Book::getTitle, Book::getAuthorName, Book::getGenreName)
                .containsExactly(4L, "test_book", "Пушкин А.С.", "Роман");
    }

    @Test
    @DisplayName(" should update field in db")
    void update() {
        val book = bookDao.getById(1);
        val book1 = new Book(book.getId(), "new_title",
                new Author(1L, null),
                new Genre(1L, null));
        bookDao.update(book1);
        assertThat(bookDao.getById(1)).isNotNull().extracting(Book::getId, Book::getTitle, Book::getAuthorName,
                Book::getGenreName).containsExactly(1L, "new_title", "Пушкин А.С.", "Роман");
        bookDao.update(book);
    }

    @Test
    @DisplayName(" should delete book from db")
    void deleteById() {
        bookDao.deleteById(3);
        bookDao.deleteById(2);
        assertThat(bookDao.getAll()).isNotNull().hasSize(1).first().extracting(Book::getId).isEqualTo(1L);
    }
}