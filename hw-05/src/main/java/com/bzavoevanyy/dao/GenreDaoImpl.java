package com.bzavoevanyy.dao;

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
public class GenreDaoImpl implements GenreDao {
    private final JdbcOperations jdbc;
    private final NamedParameterJdbcOperations namedParameterJdbcOperations;

    @Override
    public Genre getById(long id) {
        Map<String, Object> param = Collections.singletonMap("id", id);
        return namedParameterJdbcOperations.queryForObject("select * from genres where genre_id = :id", param,
                new GenreMapper());
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select * from genres", new GenreSetMapper());
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long genre_id = resultSet.getLong("genre_id");
            String genre_name = resultSet.getString("genre_name");
            return new Genre(genre_id, genre_name);
        }
    }

    private static class GenreSetMapper implements ResultSetExtractor<List<Genre>> {

        @Override
        public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
            List<Genre> genres = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getLong("genre_id");
                String genreName = rs.getString("genre_name");
                genres.add(new Genre(id, genreName));
            }
            return genres;
        }
    }
}
