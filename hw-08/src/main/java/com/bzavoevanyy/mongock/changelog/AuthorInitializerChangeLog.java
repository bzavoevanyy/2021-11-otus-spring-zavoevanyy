package com.bzavoevanyy.mongock.changelog;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.repository.AuthorRepository;
import com.bzavoevanyy.service.SequenceGeneratorService;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import lombok.val;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

@ChangeLog(order = "001")
public class AuthorInitializerChangeLog {
    @ChangeSet(author = "bzavoevanyy", id = "dropDB", order = "001")
    public void dropTableLibrary(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(author = "bzavoevanyy", id = "insert-authors", order = "002")
    public void insertAuthors(SequenceGeneratorService generatorService, AuthorRepository authorRepository) {
        val authors = LongStream
                .generate(() -> generatorService.generateSequence(Author.SEQUENCE_NAME))
                .limit(10).mapToObj(AuthorInitializerChangeLog::getAuthor).collect(Collectors.toList());
        authorRepository.saveAll(authors);
    }

    private static Author getAuthor(Long id) {
        return new Author(id, "author" + id);
    }
}
