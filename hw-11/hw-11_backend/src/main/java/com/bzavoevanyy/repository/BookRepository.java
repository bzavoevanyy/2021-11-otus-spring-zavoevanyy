package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;


public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    Mono<Book> findFirstByAuthorId(String authorId);
    Mono<Book> findFirstByGenreId(String genreId);
}
