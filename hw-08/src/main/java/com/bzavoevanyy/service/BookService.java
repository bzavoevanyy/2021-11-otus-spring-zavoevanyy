package com.bzavoevanyy.service;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;

import java.util.List;

public interface BookService {
    List<Book> getAll();

    Book getBookById(Long id);

    Book createBook(String bookTitle, Author author, Genre genre);

    void deleteBookById(Long id);

    Book updateBook(Long bookId, String bookTitle, Author author, Genre genre);
}
