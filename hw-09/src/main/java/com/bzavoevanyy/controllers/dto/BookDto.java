package com.bzavoevanyy.controllers.dto;

import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class BookDto {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 50)
    private String title;
    @NotNull
    private Long authorId;
    private String authorName;
    @NotNull
    private Long genreId;
    private String genreName;

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getAuthorName(),
                book.getGenre().getId(),
                book.getGenreName());
    }

    public static Book toDomainObject(BookDto bookDto) {
        return new Book(
                bookDto.getId(),
                bookDto.getTitle(),
                new Author(bookDto.getAuthorId(), bookDto.getAuthorName()),
                new Genre(bookDto.getGenreId(), bookDto.getGenreName()));
    }
}
