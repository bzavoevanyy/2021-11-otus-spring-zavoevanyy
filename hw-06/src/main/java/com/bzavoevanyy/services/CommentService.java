package com.bzavoevanyy.services;

import com.bzavoevanyy.entities.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> findAllByBookId(Long bookId);
    Comment createComment(String comment, Long bookId);
    void deleteById(Long commentId);
    Comment updateById(Long commentId, String comment);
}
