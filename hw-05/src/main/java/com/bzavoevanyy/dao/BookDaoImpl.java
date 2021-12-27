package com.bzavoevanyy.dao;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        return namedParameterJdbcOperations.queryForObject("select book_id, book_title, b.author_id, a.author_name, b.genre_id, g.genre_name " +
                        "from books b " +
                        "left join authors a on a.author_id = b.author_id " +
                        "left join genres g on g.genre_id = b.genre_id where b.book_id=:id", params,
                new BookMapper());
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select book_id, book_title, b.author_id, a.author_name, b.genre_id, g.genre_name " +
                "from books b " +
                "left join authors a on a.author_id = b.author_id " +
                "left join genres g on g.genre_id = b.genre_id;", new BookMapper());
    }

    @Override
    public Long insert(Book book) {
        val param = new MapSqlParameterSource(Map.of(
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcOperations.update("insert into books (book_title, author_id, genre_id) " +
                "values (:title, :author_id, :genre_id)", param, keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public int update(Book book) {
        final Map<String, Object> param = Map.of("book_id", book.getId(),
                "book_title", book.getTitle(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId());
        return namedParameterJdbcOperations.update("update books set book_title = :book_title, author_id = :author_id, " +
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
}
