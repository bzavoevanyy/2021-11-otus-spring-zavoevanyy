package com.bzavoevanyy.repositories;

import com.bzavoevanyy.entities.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(Long id);
    List<Comment> findAllByBookId(Long bookId);
    Comment save(Comment comment);
    void deleteById(Long id);
}
