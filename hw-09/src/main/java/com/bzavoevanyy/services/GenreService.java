package com.bzavoevanyy.services;

import com.bzavoevanyy.entities.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> getAll();
    Genre getGenreById(Long id);
    Genre createGenre(String name);
    void deleteGenreById(Long id);
    Genre updateGenre(Long id, String name);
}
