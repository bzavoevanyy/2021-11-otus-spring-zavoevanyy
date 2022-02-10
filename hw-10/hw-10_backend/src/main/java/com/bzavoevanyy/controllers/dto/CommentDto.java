package com.bzavoevanyy.controllers.dto;

import com.bzavoevanyy.entities.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long commentId;
    @NotBlank
    @Size(min = 2)
    private String comment;
    private LocalDate commentDate;
    @NotNull
    private Long bookId;
    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getComment(),
                comment.getCommentDate(),
                comment.getBook().getId());
    }
    public static Comment toDomainObject(CommentDto commentDto, BookDto bookDto) {
        return new Comment(
                commentDto.getCommentId(),
                commentDto.getComment(),
                commentDto.getCommentDate(),
                BookDto.toDomainObject(bookDto));
    }
}
