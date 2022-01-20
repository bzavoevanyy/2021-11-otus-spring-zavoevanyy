package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Genre;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GenreRepository extends MongoRepository<Genre, Long> {
}
