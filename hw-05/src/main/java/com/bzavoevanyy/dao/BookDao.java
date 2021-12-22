package com.bzavoevanyy.dao;

import com.bzavoevanyy.domain.Book;

import java.util.List;

public interface BookDao {
    Book getById(long id);
    List<Book> getAll();
    void insert(Book book);
    void update(Book book);
    void deleteById(long id);
}
