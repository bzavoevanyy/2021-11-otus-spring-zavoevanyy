package com.bzavoevanyy.dao;

import com.bzavoevanyy.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"SqlNoDataSourceInspection", "SqlDialectInspection"})
@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Genre getById(long id) {
        Map<String, Object> param = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject("select genre_id, genre_name" +
                        " from genres where genre_id = :id", param,
                new GenreMapper());
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select genre_id, genre_name" +
                " from genres", new GenreMapper());
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long genre_id = resultSet.getLong("genre_id");
            String genre_name = resultSet.getString("genre_name");
            return new Genre(genre_id, genre_name);
        }
    }
}
