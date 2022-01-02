package com.bzavoevanyy.repositories;

import com.bzavoevanyy.entities.Book;
import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("BookRepository test")
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName(" should execute only 1 sql query")
    void shouldExecuteOneSqlQuery() {
        val sessionFactory = em.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        val book = bookRepository.findById(1L);
        assertThat(book).isPresent().get().extracting(Book::getAuthorName, Book::getGenreName)
                .containsExactly("author2", "genre2");
        val statementCount = sessionFactory.getStatistics().getPrepareStatementCount();
        assertThat(statementCount).isEqualTo(1);
    }
}