package com.bzavoevanyy.mongock.test.changelog;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.repository.BookRepository;
import com.bzavoevanyy.service.SequenceGeneratorService;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import lombok.val;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

@ChangeLog(order = "003")
public class BookInitializerChangeLog {

    @ChangeSet(author = "bzavoevanyy", id = "insert-books", order = "001")
    public void insertBooks(SequenceGeneratorService generatorService, BookRepository bookRepository) {
        val books = LongStream
                .generate(() -> generatorService.generateSequence(Book.SEQUENCE_NAME))
                .limit(10).mapToObj(BookInitializerChangeLog::getBook).collect(Collectors.toList());
        bookRepository.saveAll(books);
    }

    private static Book getBook(Long id) {
        return new Book(id, "book" + id,
                new Author(id, "author" + id),
                new Genre(id, "genre" + id));
    }
}
