package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.AuthorDto;
import com.bzavoevanyy.controllers.dto.BookDto;
import com.bzavoevanyy.controllers.dto.CommentDto;
import com.bzavoevanyy.controllers.dto.GenreDto;
import com.bzavoevanyy.services.AuthorService;
import com.bzavoevanyy.services.BookService;
import com.bzavoevanyy.services.CommentService;
import com.bzavoevanyy.services.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CommentService commentService;

    @GetMapping("/book")
    public String getAllBooks(Model model) {
        val books = bookService.getAll().stream().map(BookDto::toDto).collect(Collectors.toList());
        val authors = authorService.getAll().stream().map(AuthorDto::toDto).collect(Collectors.toList());
        val genres = genreService.getAll().stream().map(GenreDto::toDto).collect(Collectors.toList());
        model.addAttribute("books", books);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "book";
    }

    @PostMapping("/book/{id}")
    public RedirectView updateBook(
            @PathVariable long id,
            @Valid BookDto book,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errMsg", "Input data is incorrect");
            return new RedirectView("/book");
        }
        val author = authorService.getAuthorById(book.getAuthorId());
        val genre = genreService.getGenreById(book.getGenreId());
        bookService.updateBook(id, book.getTitle(), author, genre);
        return new RedirectView("/book");
    }

    @PostMapping("/book")
    public RedirectView createBook(@Valid BookDto book,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errMsg", "Input data is incorrect");
            return new RedirectView("/book");
        }
        val author = authorService.getAuthorById(book.getAuthorId());
        val genre = genreService.getGenreById(book.getGenreId());
        bookService.createBook(book.getTitle(), author, genre);
        return new RedirectView("/book");
    }
    @PostMapping("/book/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteBookById(id);
        return "redirect:/book";
    }
    @GetMapping("/book/{id}")
    public String getBookWithComments(@PathVariable long id, Model model) {
        val book = BookDto.toDto(bookService.getBookById(id));
        val comments = commentService
                .findAllByBookId(book.getId()).stream().map(CommentDto::toDto).collect(Collectors.toList());
        val authors = authorService.getAll().stream().map(AuthorDto::toDto).collect(Collectors.toList());
        val genres = genreService.getAll().stream().map(GenreDto::toDto).collect(Collectors.toList());
        model.addAttribute("books", List.of(book));
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        model.addAttribute("comments", comments);
        return "book";
    }
}
