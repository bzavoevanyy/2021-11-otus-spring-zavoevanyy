package com.bzavoevanyy.services.Impl;

import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Comment;
import com.bzavoevanyy.repositories.CommentRepository;
import com.bzavoevanyy.services.exceptions.CommentNotFoundException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("CommentServiceImpl test")
class CommentServiceImplTest {
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private BookServiceImpl bookService;
    @Autowired
    private CommentServiceImpl commentService;

    @Test
    @DisplayName(" should call repository method save with right args")
    void shouldCallSaveWithRightArgs() {
        val book = new Book();
        when(bookService.getBookById(anyLong())).thenReturn(book);
        val comment = new Comment(null, "comment1", LocalDate.now(), book);
        commentService.createComment("comment1", 1L);
        verify(commentRepository, times(1)).save(refEq(comment));
    }

    @Test
    @DisplayName(" should throw CommentNoFoundException")
    void shouldThrowException() {
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.updateById(1L, ""));
    }

    @Test
    @DisplayName(" should call save method with right args for update")
    void shouldCallSaveMethodForUpdate() {
        when(commentRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Comment(1L, "test comment", LocalDate.now(), null)));
        commentService.updateById(1L, "updated comment");
        val comment = new Comment(1L, "updated comment", LocalDate.now(), null);
        verify(commentRepository).save(refEq(comment));
    }
}