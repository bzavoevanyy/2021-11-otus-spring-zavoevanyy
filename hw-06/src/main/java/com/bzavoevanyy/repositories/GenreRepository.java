package com.bzavoevanyy.repositories;

import com.bzavoevanyy.entities.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(Long id);
    List<Genre> findAll();
    Genre save(Genre genre);
    void deleteById(Long id);
}
