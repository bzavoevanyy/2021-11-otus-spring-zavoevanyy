package com.bzavoevanyy.rest.route.dto;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class BookDto {
    private String id;
//    @NotBlank
//    @Size(min = 2, max = 50)
    private String title;
//    @NotNull
    private String authorId;
    private String authorName;
//    @NotNull
    private String genreId;
    private String genreName;

    public static BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getBookTitle(),
                book.getAuthor().getId(),
                book.getAuthor().getAuthorName(),
                book.getGenre().getId(),
                book.getGenre().getGenreName());
    }

    public static Book toDomainObject(BookDto bookDto) {
        return new Book(
                bookDto.getId(),
                bookDto.getTitle(),
                new Author(bookDto.getAuthorId(), bookDto.getAuthorName()),
                new Genre(bookDto.getGenreId(), bookDto.getGenreName())
        );
    }
}
