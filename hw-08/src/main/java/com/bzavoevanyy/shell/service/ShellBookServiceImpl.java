package com.bzavoevanyy.shell.service;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.repository.AuthorRepository;
import com.bzavoevanyy.repository.GenreRepository;
import com.bzavoevanyy.service.BookService;
import com.bzavoevanyy.shell.utils.ShellHelper;
import com.bzavoevanyy.shell.utils.ShellInputReader;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShellBookServiceImpl implements ShellBookService {
    private final ShellHelper shellHelper;
    private final BookService bookService;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final ShellInputReader inputReader;

    @Override
    public Book createBookWithInputArgs() {
        String bookTitle = getBookTitle();
        Author author = getAuthor();
        Genre genre = getGenre();
        return bookService.createBook(bookTitle, author, genre);
    }

    @Override
    public Book createBookWithInputArgs(Long id) {
        val book = bookService.getBookById(id);
        String bookTitle = getBookTitle(book.getBookTitle());
        Author author = getAuthor(book.getAuthor().getAuthorId().toString());
        Genre genre = getGenre(book.getGenre().getGenreId().toString());
        return bookService.updateBook(book.getId(), bookTitle, author, genre);
    }

    @Override
    @Transactional(readOnly = true)
    public String getBookString() {
        return makeMessage(bookService.getAll());
    }

    @Override
    @Transactional(readOnly = true)
    public String getBookString(Long id) {
        return makeMessage(List.of(bookService.getBookById(id)));
    }

    private String getBookTitle(String... args) {
        String placeHolder = getPlaceHolder(args);
        String bookTitle;
        do {
            val message = placeHolder.isEmpty() ? "Book title" : "Book title (leave blank for default) ";
            bookTitle = inputReader.prompt(message, placeHolder);
            if (!StringUtils.hasText(bookTitle)) {
                shellHelper.printWarning("Book title can't be empty string");
            }
        } while (!StringUtils.hasText(bookTitle));
        return bookTitle;
    }
    private Author getAuthor(String... args) {
        String placeHolder = getPlaceHolder(args);
        val authors = authorRepository.findAll();
        val authorsMap = authors.stream().collect(Collectors.toMap(author -> author.getAuthorId().toString(),
                Author::getAuthorName));
        String authorId;
        do {
            authorId = inputReader.selectFromList("Choose Author:",
                    "Please enter one of the [] values", authorsMap, true, placeHolder);
        } while (authorId.isEmpty());

        return authors.get(Integer.parseInt(authorId) - 1);
    }

    private Genre getGenre(String... args) {
        String placeHolder = getPlaceHolder(args);
        val genres = genreRepository.findAll();
        val genresMap = genres.stream().collect(Collectors.toMap(genre -> genre.getGenreId().toString(),
                Genre::getGenreName));
        String genreId;
        do {
            genreId = inputReader.selectFromList("Choose Genre:",
                    "Please enter one of the [] values", genresMap, true, placeHolder);
        } while (genreId.isEmpty());

        return genres.get(Integer.parseInt(genreId) - 1);
    }
    private String getPlaceHolder(String[] args) {
        if (args.length > 0) {
            return args[0];
        } else {
            return "";
        }
    }
    private String makeMessage(List<Book> books) {
        val result = new StringBuilder();
        result.append(String.format("%-8s | ", "Book_id"))
                .append(String.format("%-30s | ", "Book title"))
                .append(String.format("%-30s | ", "Author name"))
                .append(String.format("%-30s | %n", "Genre"));
        books.forEach(book -> result.append(String.format("%-8s | ", book.getId()))
                .append(String.format("%-30s | ", book.getBookTitle()))
                .append(String.format("%-30s | ", book.getAuthorName()))
                .append(String.format("%-30s | %n", book.getGenreName())));
        return result.toString();
    }
}
