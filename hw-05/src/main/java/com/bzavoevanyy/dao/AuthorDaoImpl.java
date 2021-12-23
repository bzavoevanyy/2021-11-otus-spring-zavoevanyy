package com.bzavoevanyy.dao;

import com.bzavoevanyy.domain.Author;
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
public class AuthorDaoImpl implements AuthorDao {
    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Author getById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject("select author_id, author_name from authors where author_id = :id",
                params, new AuthorMapper());
    }

    @Override
    public void insert(Author author) {
        Map<String, Object> param = Map.of("author_name", author.getName());
        namedParameterJdbcOperations.update("insert into authors (author_name) values (:author_name)", param);
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select author_id, author_name from authors", new AuthorSetMapper());
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long author_id = resultSet.getLong("author_id");
            String author_name = resultSet.getString("author_name");
            return new Author(author_id, author_name);
        }
    }

    private static class AuthorSetMapper implements ResultSetExtractor<List<Author>> {

        @Override
        public List<Author> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Author> authors = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("author_id");
                String authorName = rs.getString("author_name");
                authors.add(new Author(id, authorName));
            }
            return authors;
        }
    }

}
