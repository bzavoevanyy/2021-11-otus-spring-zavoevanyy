package com.bzavoevanyy.repositories;

import com.bzavoevanyy.entities.Book;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            value = "book-entity-graph")
    List<Book> findAll();

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            value = "book-entity-graph")
    Optional<Book> findById(Long id);
}
