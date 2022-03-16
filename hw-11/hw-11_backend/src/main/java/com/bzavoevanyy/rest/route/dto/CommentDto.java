package com.bzavoevanyy.rest.route.dto;

import com.bzavoevanyy.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CommentDto {
    private String id;
    @NotBlank
    @Size(min = 2)
    private String comment;
    private LocalDateTime commentDate;
    @NotNull
    private String bookId;
    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getComment(),
                comment.getDate(),
                comment.getBookId());
    }
    public static Comment toDomainObject(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getBookId(),
                commentDto.getComment(),
                commentDto.getCommentDate());
    }
}
