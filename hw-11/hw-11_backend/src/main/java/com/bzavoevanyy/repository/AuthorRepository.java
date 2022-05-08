package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Author;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
