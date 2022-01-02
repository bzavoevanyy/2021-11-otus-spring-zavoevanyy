package com.bzavoevanyy.repositories.Impl;

import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.repositories.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepositoryJpa implements BookRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(Long id) {
        val entityGraph = em.createEntityGraph("book-entity-graph");
        final Map<String, Object> properties = Map.of("javax.persistence.fetchgraph", entityGraph);
        return Optional.ofNullable(em.find(Book.class, id, properties));
    }

    @Override
    public List<Book> findAll() {
        val query = em.createQuery("select b from Book b join fetch b.author join fetch b.genre",
                Book.class);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public void deleteById(Long id) {
        val book = findById(id).orElseThrow(BookNotFoundException::new);
        em.remove(book);
    }
}
