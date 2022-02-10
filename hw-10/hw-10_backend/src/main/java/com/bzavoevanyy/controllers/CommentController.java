package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.CommentDto;
import com.bzavoevanyy.controllers.dto.ErrorResponse;
import com.bzavoevanyy.controllers.dto.SuccessResponse;
import com.bzavoevanyy.services.CommentService;
import com.bzavoevanyy.services.exceptions.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comment")
    public List<CommentDto> getAllComments(@RequestParam(name = "bookId") Long bookId) {
        return commentService.findAllByBookId(bookId)
                .stream().map(CommentDto::toDto).collect(Collectors.toList());
    }
    @PostMapping("/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto) {
        return CommentDto
                .toDto(commentService.createComment(commentDto.getComment(), commentDto.getBookId()));
    }
    @PutMapping("/comment/{id}")
    public CommentDto updateComment(@PathVariable long id,
                                @Valid @RequestBody CommentDto commentDto) {
        return CommentDto.toDto(commentService.updateById(id, commentDto.getComment()));
    }
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<SuccessResponse> deleteComment(@PathVariable long id) {
        commentService.deleteById(id);
        return new ResponseEntity<>(new SuccessResponse("deleted"), HttpStatus.OK);
    }
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound() {
        return new ResponseEntity<>(new ErrorResponse("Comment not found"), HttpStatus.BAD_REQUEST);
    }
}
