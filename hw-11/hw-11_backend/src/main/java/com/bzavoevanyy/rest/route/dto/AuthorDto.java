package com.bzavoevanyy.rest.route.dto;

import com.bzavoevanyy.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AuthorDto {
    private String id;
    @NotBlank
    @Size(min = 2, max = 50)
    private String authorName;

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(), author.getAuthorName());
    }

    public static Author toDomainObject(AuthorDto authorDto) {
        return new Author(authorDto.getId(), authorDto.getAuthorName());
    }
}
