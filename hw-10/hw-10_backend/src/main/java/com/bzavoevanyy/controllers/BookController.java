package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.*;
import com.bzavoevanyy.services.AuthorService;
import com.bzavoevanyy.services.BookService;
import com.bzavoevanyy.services.GenreService;
import com.bzavoevanyy.services.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    @GetMapping("/book")
    public List<BookDto> getAllBooks() {
        return bookService.getAll().stream().map(BookDto::toDto).collect(Collectors.toList());
    }

    @GetMapping("/book/{id}")
    public BookDto getBookById(@PathVariable long id) {
        return BookDto.toDto(bookService.getBookById(id));
    }

    @PostMapping("/book")
    public BookDto createBook(@Valid @RequestBody BookDto book) {
        val genre = genreService.getGenreById(book.getGenreId());
        val author = authorService.getAuthorById(book.getAuthorId());
        val savedBook = bookService.createBook(book.getTitle(), author, genre);
        return BookDto.toDto(savedBook);
    }

    @PutMapping("/book/{id}")
    public BookDto updateBook(
            @PathVariable long id,
            @Valid @RequestBody BookDto book
    ) {
        val genre = genreService.getGenreById(book.getGenreId());
        val author = authorService.getAuthorById(book.getAuthorId());
        val updatedBook = bookService.updateBook(id, book.getTitle(), author, genre);
        return BookDto.toDto(updatedBook);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<SuccessResponse> deleteBook(@PathVariable long id) {
        bookService.deleteBookById(id);
        return new ResponseEntity<>(new SuccessResponse("deleted"), HttpStatus.OK);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound() {
        return new ResponseEntity<>(new ErrorResponse("Book not found"), HttpStatus.BAD_REQUEST);
    }
}
