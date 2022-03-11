package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, Long> {
}
