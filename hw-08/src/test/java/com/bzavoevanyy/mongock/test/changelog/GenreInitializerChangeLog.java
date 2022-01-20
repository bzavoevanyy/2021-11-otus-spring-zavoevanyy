package com.bzavoevanyy.mongock.test.changelog;

import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.repository.GenreRepository;
import com.bzavoevanyy.service.SequenceGeneratorService;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import lombok.val;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

@ChangeLog(order = "002")
public class GenreInitializerChangeLog {

    @ChangeSet(author = "bzavoevanyy", id = "insert-genres", order = "001")
    public void insertGenres(SequenceGeneratorService generatorService, GenreRepository genreRepository) {
        val genres = LongStream
                .generate(() -> generatorService.generateSequence(Genre.SEQUENCE_NAME))
                .limit(10).mapToObj(GenreInitializerChangeLog::getGenre).collect(Collectors.toList());
        genreRepository.saveAll(genres);
    }

    private static Genre getGenre(Long id) {
        return new Genre(id, "genre" + id);
    }
}
