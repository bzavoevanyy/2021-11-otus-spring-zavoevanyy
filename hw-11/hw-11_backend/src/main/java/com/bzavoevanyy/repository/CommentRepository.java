package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;


public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findAllByBookId(String bookId);
}
