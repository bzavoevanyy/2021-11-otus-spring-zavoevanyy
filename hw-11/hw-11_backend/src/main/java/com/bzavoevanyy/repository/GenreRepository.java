package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Genre;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
}
