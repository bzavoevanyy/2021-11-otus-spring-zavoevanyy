package com.bzavoevanyy.controllers.dto;

import com.bzavoevanyy.entities.Author;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AuthorDto {
    private Long authorId;
    @NotBlank
    @Size(min = 2, max = 50)
    private String authorName;

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getName());
    }

    public static Author toDomainObject(AuthorDto authorDto) {
        return new Author(authorDto.getAuthorId(), authorDto.getAuthorName());
    }
}
