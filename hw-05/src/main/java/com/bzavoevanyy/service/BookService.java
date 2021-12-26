package com.bzavoevanyy.service;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;

import java.util.List;

public interface BookService {
    List<Book> getAll();

    List<Book> getBookById(long id);

    Long createBook(String bookTitle, Author author, Genre genre);

    void deleteBookById(long id);

    int updateBook(Long bookId, String bookTitle, Author author, Genre genre);
}
