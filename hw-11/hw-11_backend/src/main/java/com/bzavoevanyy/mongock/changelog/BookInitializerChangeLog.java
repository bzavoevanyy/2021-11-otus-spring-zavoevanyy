package com.bzavoevanyy.mongock.changelog;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.*;
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync;
import io.mongock.driver.mongodb.reactive.util.SubscriberSync;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeUnit(id = "init-book", order = "003", author = "bzavoevanyy")
public class BookInitializerChangeLog {
    private final static Logger logger = LoggerFactory.getLogger(CommentInitializerChangeLog.class);
    private final static String COLLECTION_NAME = "book";
    private final CodecRegistry codecRegistry;
    private List<Author> authors;
    private List<Genre> genres;

    public BookInitializerChangeLog(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @BeforeExecution
    public void beforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.createCollection(COLLECTION_NAME).subscribe(subscriber);
        subscriber.await();

        SubscriberSync<Author> authorSubscriberSync = new MongoSubscriberSync<>();
        mongoDatabase.withCodecRegistry(codecRegistry).getCollection("author", Author.class).find().subscribe(authorSubscriberSync);
        authors = new ArrayList<>(authorSubscriberSync.get());

        SubscriberSync<Genre> genreSubscriberSync = new MongoSubscriberSync<>();
        mongoDatabase.withCodecRegistry(codecRegistry).getCollection("genre", Genre.class).find().subscribe(genreSubscriberSync);
        genres = new ArrayList<>(genreSubscriberSync.get());

    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection(COLLECTION_NAME).drop().subscribe(subscriber);
        subscriber.await();
    }

    @Execution
    public void execution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        SubscriberSync<InsertManyResult> subscriber = new MongoSubscriberSync<>();

        mongoDatabase.withCodecRegistry(codecRegistry).getCollection(COLLECTION_NAME, Book.class)
                .insertMany(clientSession, IntStream.range(0,10)
                        .mapToObj(i -> getBook(i,
                                this.authors.get(new Random().nextInt(3)),
                                this.genres.get(new Random().nextInt(3))))
                        .collect(Collectors.toList()))
                .subscribe(subscriber);

        InsertManyResult result = subscriber.getFirst();
        logger.info("BookInitializerChangeLog.execution wasAcknowledged: {}", result.wasAcknowledged());
        result.getInsertedIds()
                .forEach((key, value) -> logger.info("update id[{}] : {}", key, value));
    }

    @RollbackExecution
    public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        SubscriberSync<DeleteResult> subscriber = new MongoSubscriberSync<>();

        mongoDatabase
                .withCodecRegistry(codecRegistry)
                .getCollection(COLLECTION_NAME, Book.class)
                .deleteMany(clientSession, new Document())
                .subscribe(subscriber);
        DeleteResult result = subscriber.getFirst();
        logger.info("BookInitializerChangeLog.rollbackExecution wasAcknowledged: {}", result.wasAcknowledged());
        logger.info("BookInitializerChangeLog.rollbackExecution deleted count: {}", result.getDeletedCount());
    }

    private static Book getBook(int i, Author author, Genre genre) {
        return new Book(UUID.randomUUID().toString(), "book" + i, author, genre);
    }
}
