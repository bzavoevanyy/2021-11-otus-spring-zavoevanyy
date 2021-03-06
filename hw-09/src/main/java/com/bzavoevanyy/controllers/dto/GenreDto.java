package com.bzavoevanyy.controllers.dto;

import com.bzavoevanyy.entities.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class GenreDto {
    private Long genreId;
    @NotBlank
    @Size(min = 2, max = 30)
    private String genreName;

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public static Genre toDomainObject(GenreDto genreDto) {
        return new Genre(genreDto.getGenreId(), genreDto.getGenreName());
    }
}
