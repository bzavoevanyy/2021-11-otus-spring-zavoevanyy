package com.bzavoevanyy.service;

import com.bzavoevanyy.domain.Book;

import java.util.List;
import java.util.Map;

public interface BookService {
    List<Book> getAll();
    List<Book> getBookById(long id);
    void createBook(Map<String, Object> bookArgs);
    void deleteBookById(long id);
    void updateBook(Map<String, Object> bookArgs);
}
