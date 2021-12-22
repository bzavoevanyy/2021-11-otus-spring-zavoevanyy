package com.bzavoevanyy.dao;

import com.bzavoevanyy.domain.Author;

import java.util.List;

public interface AuthorDao {
    Author getById(long id);
    void insert(Author author);
    List<Author> getAll();
}
