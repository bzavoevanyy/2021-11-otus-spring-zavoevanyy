package com.bzavoevanyy.shell.service;

import com.bzavoevanyy.dao.AuthorDao;
import com.bzavoevanyy.dao.BookDao;
import com.bzavoevanyy.dao.GenreDao;
import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.service.BookService;
import com.bzavoevanyy.shell.utils.ShellHelper;
import com.bzavoevanyy.shell.utils.ShellInputReader;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShellBookServiceImpl implements ShellBookService {
    private final ShellHelper shellHelper;
    private final BookDao bookDao;
    private final BookService bookService;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;
    private final ShellInputReader inputReader;

    @Override
    public Map<String, Object> getBookArgs() {
        val bookArgs = new HashMap<String, Object>();
        bookArgs.put("book_title", getBookTitle());
        bookArgs.put("author", getAuthor());
        bookArgs.put("genre", getGenre());
        return bookArgs;
    }

    @Override
    public Map<String, Object> getBookArgs(long id) {
        val book = bookDao.getById(id);
        val bookArgs = new HashMap<String, Object>();
        bookArgs.put("book_id", id);
        bookArgs.put("book_title", getBookTitle(book.getTitle()));
        bookArgs.put("author", getAuthor(book.getAuthor().getId().toString()));
        bookArgs.put("genre", getGenre(book.getGenre().getId().toString()));
        return bookArgs;
    }

    @Override
    public String getBookString() {
        return makeMessage(bookService.getAll());
    }

    @Override
    public String getBookString(long id) {
        return makeMessage(bookService.getBookById(id));
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
        val authors = authorDao.getAll();
        val authorsMap = authors.stream().collect(Collectors.toMap(author -> author.getId().toString(),
                Author::getName));
        String authorId;
        do {
            authorId = inputReader.selectFromList("Choose Author:",
                    "Please enter one of the [] values", authorsMap, true, placeHolder);
        } while (authorId.isEmpty());

        return authors.get(Integer.parseInt(authorId) - 1);
    }

    private Genre getGenre(String... args) {
        String placeHolder = getPlaceHolder(args);
        val genres = genreDao.getAll();
        val genresMap = genres.stream().collect(Collectors.toMap(genre -> genre.getId().toString(),
                Genre::getName));
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
                .append(String.format("%-30s | ", book.getTitle()))
                .append(String.format("%-30s | ", book.getAuthorName()))
                .append(String.format("%-30s | %n", book.getGenreName())));
        return result.toString();
    }
}
