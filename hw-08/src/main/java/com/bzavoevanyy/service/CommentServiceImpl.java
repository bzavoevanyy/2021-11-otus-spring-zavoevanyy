package com.bzavoevanyy.service;

import com.bzavoevanyy.domain.Comment;
import com.bzavoevanyy.repository.BookRepository;
import com.bzavoevanyy.repository.CommentRepository;
import com.bzavoevanyy.service.exceptions.BookNotFoundException;
import com.bzavoevanyy.service.exceptions.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final SequenceGeneratorServiceImpl generatorService;
    private final BookRepository bookRepository;

    @Override
    public List<Comment> findAllByBookId(Long bookId) {
        return commentRepository.findAllByBookId(bookId);
    }

    @Override
    public Comment createComment(String comment, Long bookId) {
        bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException("Book not found"));
        val currentDateTime = LocalDateTime.now();
        return commentRepository.save(new Comment(generatorService.generateSequence(Comment.SEQUENCE_NAME),
                bookId, comment, currentDateTime));
    }

    @Override
    public void deleteById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment updateById(Long commentId, String comment) {
        val commentForUpdate = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        commentForUpdate.setComment(comment);
        commentForUpdate.setDate(LocalDateTime.now());
        return commentRepository.save(commentForUpdate);
    }
}
