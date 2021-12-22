package com.bzavoevanyy.dao;

import com.bzavoevanyy.domain.Genre;

import java.util.List;


public interface GenreDao {
    Genre getById(long id);
    List<Genre> getAll();
}
