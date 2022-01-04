package com.bzavoevanyy.services.Impl;

import com.bzavoevanyy.entities.Comment;
import com.bzavoevanyy.repositories.CommentRepository;
import com.bzavoevanyy.services.exceptions.CommentNotFoundException;
import com.bzavoevanyy.services.BookService;
import com.bzavoevanyy.services.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BookService bookService;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBookId(Long bookId) {
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment createComment(String comment, Long bookId) {
        val book = bookService.getBookById(bookId);
        val currentDate = LocalDate.now();
        return commentRepository.save(new Comment(null, comment, currentDate, book));
    }

    @Override
    @Transactional
    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public Comment updateById(Long commentId, String comment) {
        val commentForUpdate = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        commentForUpdate.setComment(comment);
        commentForUpdate.setCommentDate(LocalDate.now());
        return commentRepository.save(commentForUpdate);
    }
}
