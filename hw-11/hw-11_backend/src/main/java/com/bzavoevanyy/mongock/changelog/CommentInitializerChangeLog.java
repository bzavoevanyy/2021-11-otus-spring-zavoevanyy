package com.bzavoevanyy.mongock.changelog;

import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Comment;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeUnit(id = "init-comment", order = "004", author = "bzavoevanyy")
public class CommentInitializerChangeLog {
    private final static Logger logger = LoggerFactory.getLogger(CommentInitializerChangeLog.class);
    private final static String COLLECTION_NAME = "comment";
    private final CodecRegistry codecRegistry;
    private List<Book> books;

    public CommentInitializerChangeLog(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @BeforeExecution
    public void beforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.createCollection(COLLECTION_NAME).subscribe(subscriber);
        subscriber.await();

        SubscriberSync<Book> bookSubscriberSync = new MongoSubscriberSync<>();
        mongoDatabase.withCodecRegistry(codecRegistry)
                .getCollection("book", Book.class)
                .find().subscribe(bookSubscriberSync);
        books = new ArrayList<>(bookSubscriberSync.get());


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

        mongoDatabase.withCodecRegistry(codecRegistry).getCollection(COLLECTION_NAME, Comment.class)
                .insertMany(clientSession, IntStream.range(0,7)
                        .mapToObj(i -> getComment(i,
                                this.books.get(new Random().nextInt(10)).getId()))
                        .collect(Collectors.toList()))
                .subscribe(subscriber);

        InsertManyResult result = subscriber.getFirst();
        logger.info("CommentInitializerChangeLog.execution wasAcknowledged: {}", result.wasAcknowledged());
        result.getInsertedIds()
                .forEach((key, value) -> logger.info("update id[{}] : {}", key, value));
    }

    @RollbackExecution
    public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        SubscriberSync<DeleteResult> subscriber = new MongoSubscriberSync<>();

        mongoDatabase
                .withCodecRegistry(codecRegistry)
                .getCollection(COLLECTION_NAME, Comment.class)
                .deleteMany(clientSession, new Document())
                .subscribe(subscriber);
        DeleteResult result = subscriber.getFirst();
        logger.info("CommentInitializerChangeLog.rollbackExecution wasAcknowledged: {}", result.wasAcknowledged());
        logger.info("CommentInitializerChangeLog.rollbackExecution deleted count: {}", result.getDeletedCount());
    }

    private static Comment getComment(int i, String bookId) {
        return new Comment(UUID.randomUUID().toString(), bookId, "comment" + i, LocalDateTime.now());
    }
}
