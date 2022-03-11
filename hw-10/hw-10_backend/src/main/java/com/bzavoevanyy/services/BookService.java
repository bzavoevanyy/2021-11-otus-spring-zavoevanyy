package com.bzavoevanyy.services;

import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Genre;

import java.util.List;

public interface BookService {
    List<Book> getAll();

    Book getBookById(Long id);

    Book createBook(String bookTitle, Author author, Genre genre);

    void deleteBookById(Long id);

    Book updateBook(Long bookId, String bookTitle, Author author, Genre genre);
}
