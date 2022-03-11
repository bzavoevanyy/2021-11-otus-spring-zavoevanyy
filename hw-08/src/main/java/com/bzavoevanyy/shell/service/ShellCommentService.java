package com.bzavoevanyy.shell.service;

import com.bzavoevanyy.domain.Comment;

public interface ShellCommentService {
    String getCommentStringByBookId(Long bookId);
    Comment writeCommentByBookId(Long bookId);
    Comment updateCommentByCommentId(Long commentId);
    void deleteCommentById(Long id);
}
