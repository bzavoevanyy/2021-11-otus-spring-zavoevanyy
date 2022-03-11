package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorRepository extends MongoRepository<Author, Long> {
}
