package com.bzavoevanyy.repositories;

import com.bzavoevanyy.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
