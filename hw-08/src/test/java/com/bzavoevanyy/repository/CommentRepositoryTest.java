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
@Import(SequenceGeneratorServiceImpl.class)
@DisplayName("CommentRepository test")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName(" should find all comments by book id")
    void findAllByBookId() {
        val comments = commentRepository.findAllByBookId(1L);
        assertThat(comments).isNotNull().hasSize(5);
    }

}