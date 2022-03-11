package com.bzavoevanyy.services;

import com.bzavoevanyy.entities.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAll();
    Author getAuthorById(Long id);
    Author createAuthor(String name);
    void deleteAuthorById(Long id);
    Author updateAuthor(Long id, String name);
}
