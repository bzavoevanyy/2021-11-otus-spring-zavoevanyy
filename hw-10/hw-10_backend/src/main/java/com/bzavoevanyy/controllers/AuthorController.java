package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.AuthorDto;
import com.bzavoevanyy.controllers.dto.ErrorResponse;
import com.bzavoevanyy.controllers.dto.SuccessResponse;
import com.bzavoevanyy.controllers.exceptions.AuthorHasBookException;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.services.AuthorService;
import com.bzavoevanyy.services.exceptions.AuthorNotFoundException;
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
public class AuthorController {
    private final AuthorService authorService;
    private final BookRepository bookRepository;

    @GetMapping("/author")
    public List<AuthorDto> getAllAuthors() {
        return authorService.getAll()
                .stream().map(AuthorDto::toDto).collect(Collectors.toList());
    }

    @PostMapping("/author")
    public AuthorDto createAuthor(@Valid @RequestBody AuthorDto authorDto) {
        return AuthorDto.toDto(authorService.createAuthor(authorDto.getAuthorName()));
    }

    @PutMapping("/author/{id}")
    public AuthorDto updateAuthor(@PathVariable long id, @Valid @RequestBody AuthorDto authorDto) {
        return AuthorDto.toDto(authorService.updateAuthor(id, authorDto.getAuthorName()));
    }

    @DeleteMapping("/author/{id}")
    public ResponseEntity<SuccessResponse> deleteAuthor(@PathVariable long id) {

        val booksWithAuthorId = bookRepository.countAllByAuthorId(id);

        if (booksWithAuthorId > 0) {
            throw new AuthorHasBookException();
        }
        authorService.deleteAuthorById(id);
        return new ResponseEntity<>(new SuccessResponse("deleted"), HttpStatus.OK);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound() {
        return new ResponseEntity<>(new ErrorResponse("Author not found"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorHasBookException.class)
    public ResponseEntity<ErrorResponse> handleAuthorHasBook() {
        return new ResponseEntity<>(
                new ErrorResponse("Author has book, please firstly delete it"), HttpStatus.BAD_REQUEST);
    }
}
