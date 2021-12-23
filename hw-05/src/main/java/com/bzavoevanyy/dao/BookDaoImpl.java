package com.bzavoevanyy.dao;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {
    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Book getById(long id) {
        final Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject("select book_id, book_title, books.author_id," +
                        " author_name, books.genre_id, genre_name from books, authors, genres " +
                        "where books.author_id = authors.author_id and books.genre_id = genres.genre_id " +
                        "and book_id = :id", params,
                new BookMapper());
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select book_id, book_title, books.author_id," +
                " author_name, books.genre_id, genre_name from books, authors, genres " +
                "where books.author_id = authors.author_id and books.genre_id = genres.genre_id", new BookSetMapper());
    }

    @Override
    public void insert(Book book) {
        final Map<String, Object> param = Map.of(
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId());
        namedParameterJdbcOperations.update("insert into books (book_title, author_id, genre_id) " +
                "values (:title, :author_id, :genre_id)", param);
    }

    @Override
    public void update(Book book) {
        final Map<String, Object> param = Map.of("book_id", book.getId(),
                "book_title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId());
        namedParameterJdbcOperations.update("update books set book_title = :book_title, author_id = :author_id, " +
                "genre_id = :genre_id where book_id = :book_id", param);
    }

    @Override
    public void deleteById(long id) {
        final Map<String, Object> param = Collections.singletonMap("book_id", id);
        namedParameterJdbcOperations.update("delete from books where book_id = :book_id", param);
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("book_id");
            String title = rs.getString("book_title");
            Author author = new Author(rs.getLong("books.author_id"), rs.getString("author_name"));
            Genre genre = new Genre(rs.getLong("books.genre_id"), rs.getString("genre_name"));
            return new Book(id, title, author, genre);
        }
    }

    private static class BookSetMapper implements ResultSetExtractor<List<Book>> {

        @Override
        public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Book> books = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("book_id");
                String title = rs.getString("book_title");
                Author author = new Author(rs.getLong("books.author_id"), rs.getString("author_name"));
                Genre genre = new Genre(rs.getLong("books.genre_id"), rs.getString("genre_name"));
                books.add(new Book(id, title, author, genre));
            }
            return books;
        }
    }
}
