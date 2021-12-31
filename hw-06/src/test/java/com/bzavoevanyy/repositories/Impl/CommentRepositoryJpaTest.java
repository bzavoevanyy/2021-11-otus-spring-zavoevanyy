package com.bzavoevanyy.repositories.Impl;

import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Comment;
import com.bzavoevanyy.repositories.CommentRepository;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;


import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@Import(CommentRepositoryJpa.class)
@DisplayName("Comment repository test")
class CommentRepositoryJpaTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName(" should return all comment of book by book id")
    void shouldReturnAllCommentsByBookId() {
        val comments = commentRepository.findAllByBookId(1L);
        assertThat(comments).isNotNull().hasSize(2).extracting(Comment::getComment, Comment::getCommentDate)
                .containsExactly(
                        tuple("comment1", LocalDate.of(2021,12,12)),
                        tuple("comment2", LocalDate.of(2021,11,11)));
    }

    @Test
    @DisplayName(" should save new comment")
    void shouldSaveNewComment() {
        val book = em.find(Book.class, 3L);
        val comment = new Comment(null, "new comment", LocalDate.of(2020,12,12),
                book);
        commentRepository.save(comment);
        em.detach(comment);
        val newComment = em.find(Comment.class, 5L);
        assertThat(newComment).isNotNull().extracting(Comment::getId, Comment::getComment, Comment::getCommentDate)
                .containsExactly(5L, "new comment", LocalDate.of(2020,12,12));
    }
    @Test
    @DisplayName(" should update comment")
    void shouldUpdateComment() {
        val comment = em.find(Comment.class, 3L);
        comment.setComment("new comment");
        comment.setCommentDate(LocalDate.now());
        commentRepository.save(comment);
        em.flush();
        em.detach(comment);
        val newComment = em.find(Comment.class, 3L);
        assertThat(newComment).isNotNull().extracting(Comment::getComment, Comment::getCommentDate)
                .containsExactly("new comment", LocalDate.now());
    }
    @Test
    @DisplayName(" should delete comment by id")
    void shouldDeleteCommentById() {
        val comment = em.find(Comment.class, 4L);
        assertThat(comment).isNotNull();
        em.detach(comment);
        commentRepository.deleteById(4L);
        val deletedComment = em.find(Comment.class, 4L);
        assertThat(deletedComment).isNull();
    }
}