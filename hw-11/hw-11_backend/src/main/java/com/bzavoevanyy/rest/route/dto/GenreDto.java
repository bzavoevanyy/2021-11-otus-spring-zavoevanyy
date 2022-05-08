package com.bzavoevanyy.rest.route.dto;

import com.bzavoevanyy.domain.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class GenreDto {
    private String id;
    @NotBlank
    @Size(min = 2, max = 30)
    private String genreName;

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getGenreName());
    }

    public static Genre toDomainObject(GenreDto genreDto) {
        return new Genre(genreDto.getId(), genreDto.getGenreName());
    }
}
