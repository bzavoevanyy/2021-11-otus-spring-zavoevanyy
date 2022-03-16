package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface BookRepository extends ReactiveMongoRepository<Book, String> {

}
