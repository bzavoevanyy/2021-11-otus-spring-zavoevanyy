package com.bzavoevanyy.repositories;

import com.bzavoevanyy.entities.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Optional<Author> findById(Long id);
    List<Author> findAll();
    Author save(Author author);
    void deleteById(Long id);
}
