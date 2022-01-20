package com.bzavoevanyy.shell.service;

import com.bzavoevanyy.domain.Comment;
import com.bzavoevanyy.service.CommentService;
import com.bzavoevanyy.shell.utils.ShellInputReader;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShellCommentServiceImpl implements ShellCommentService {
    private final CommentService commentService;
    private final ShellInputReader inputReader;
    @Override
    public String getCommentStringByBookId(Long bookId) {
        return makeMessage(commentService.findAllByBookId(bookId));
    }

    @Override
    public Comment writeCommentByBookId(Long bookId) {
        val commentString = inputReader.prompt(String.format("Write your comment for book with id %s", bookId));
        return commentService.createComment(commentString, bookId);
    }

    @Override
    public Comment updateCommentByCommentId(Long commentId) {
        val commentString = inputReader.prompt("Write comment");
        return commentService.updateById(commentId, commentString);
    }

    @Override
    public void deleteCommentById(Long id) {
        commentService.deleteById(id);
    }
    private String makeMessage(List<Comment> comments) {
        val result = new StringBuilder();
        result.append(String.format("%-8s | ", "Id"))
                .append(String.format("%-30s | ", "Comment"))
                .append(String.format("%-30s | %n", "Date"));
        comments.forEach(comment -> result.append(String.format("%-8s | ", comment.getCommentId()))
                .append(String.format("%-30s | ", comment.getComment()))
                .append(String.format("%-30s | %n", comment.getDate())));
        return result.toString();
    }
}
