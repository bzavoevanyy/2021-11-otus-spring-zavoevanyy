package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.ErrorResponse;
import com.bzavoevanyy.controllers.dto.GenreDto;
import com.bzavoevanyy.controllers.dto.SuccessResponse;
import com.bzavoevanyy.controllers.exceptions.GenreHasBookException;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.services.GenreService;
import com.bzavoevanyy.services.exceptions.GenreNotFoundException;
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
public class GenreController {
    private final GenreService genreService;
    private final BookRepository bookRepository;

    @GetMapping("/genre")
    public List<GenreDto> getAllGenres() {
        return genreService.getAll()
                .stream().map(GenreDto::toDto).collect(Collectors.toList());
    }

    @PostMapping("/genre")
    public GenreDto createGenre(@Valid @RequestBody GenreDto genreDto) {
        return GenreDto.toDto(genreService.createGenre(genreDto.getGenreName()));
    }

    @PutMapping("/genre/{id}")
    public GenreDto updateGenre(@PathVariable long id, @Valid @RequestBody GenreDto genreDto) {
        return GenreDto.toDto(genreService.updateGenre(id, genreDto.getGenreName()));
    }
    @DeleteMapping("/genre/{id}")
    public ResponseEntity<SuccessResponse> deleteGenre(@PathVariable long id) {

        val booksWithGenreId = bookRepository.countAllByGenreId(id);
        if (booksWithGenreId > 0) {
            throw new GenreHasBookException();
        }
        genreService.deleteGenreById(id);
        return new ResponseEntity<>(new SuccessResponse("deleted"), HttpStatus.OK);
    }
    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound() {
        return new ResponseEntity<>(new ErrorResponse("Genre not found"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenreHasBookException.class)
    public ResponseEntity<ErrorResponse> handleAuthorHasBook() {
        return new ResponseEntity<>(
                new ErrorResponse("Genre has book, please firstly delete it"), HttpStatus.BAD_REQUEST);
    }
}
