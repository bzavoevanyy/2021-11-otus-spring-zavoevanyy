package com.bzavoevanyy.repository;

import com.bzavoevanyy.service.SequenceGeneratorServiceImpl;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;


import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Import({SequenceGeneratorServiceImpl.class, CustomBookRepositoryImpl.class})
@DisplayName("CustomBookRepository test")
class CustomBookRepositoryImplTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomBookRepository customBookRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName(" should delete book with all comments")
    void deleteBookByIdWithComments() {
        customBookRepository.deleteBookByIdWithComments(1L);
        val book = bookRepository.findById(1L);
        val comments = commentRepository.findAllByBookId(1L);
        assertThat(book).isEmpty();
        assertThat(comments).isEmpty();
    }
}