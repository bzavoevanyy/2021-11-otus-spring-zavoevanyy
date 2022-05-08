package com.bzavoevanyy.mongock.changelog;

import com.bzavoevanyy.domain.Author;
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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeUnit(id = "init-author", order = "001", author = "bzavoevanyy")
public class AuthorInitializerChangeLog {
    private final static Logger logger = LoggerFactory.getLogger(AuthorInitializerChangeLog.class);
    private final static String COLLECTION_NAME = "author";
    private final CodecRegistry codecRegistry;

    public AuthorInitializerChangeLog(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @BeforeExecution
    public void beforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.createCollection(COLLECTION_NAME).subscribe(subscriber);
        subscriber.await();
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
        mongoDatabase.withCodecRegistry(codecRegistry).getCollection(COLLECTION_NAME, Author.class)
                .insertMany(clientSession, IntStream.range(0,3)
                        .mapToObj(AuthorInitializerChangeLog::getAuthor).collect(Collectors.toList()))
                .subscribe(subscriber);

        InsertManyResult result = subscriber.getFirst();
        logger.info("AuthorInitializerChangeLog.execution wasAcknowledged: {}", result.wasAcknowledged());
        result.getInsertedIds()
                .forEach((key, value) -> logger.info("update id[{}] : {}", key, value));
    }

    @RollbackExecution
    public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        SubscriberSync<DeleteResult> subscriber = new MongoSubscriberSync<>();

        mongoDatabase
                .withCodecRegistry(codecRegistry)
                .getCollection(COLLECTION_NAME, Author.class)
                .deleteMany(clientSession, new Document())
                .subscribe(subscriber);
        DeleteResult result = subscriber.getFirst();
        logger.info("ClientInitializerChangeLog.rollbackExecution wasAcknowledged: {}", result.wasAcknowledged());
        logger.info("ClientInitializerChangeLog.rollbackExecution deleted count: {}", result.getDeletedCount());
    }


    private static Author getAuthor(int i) {
        return new Author(UUID.randomUUID().toString(), "author" + i);
    }

}
