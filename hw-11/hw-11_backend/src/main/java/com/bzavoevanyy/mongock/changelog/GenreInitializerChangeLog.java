package com.bzavoevanyy.mongock.changelog;

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

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ChangeUnit(id = "init-genre", order = "002", author = "bzavoevanyy")
public class GenreInitializerChangeLog {
    private static final Logger logger = LoggerFactory.getLogger(GenreInitializerChangeLog.class);
    private final CodecRegistry codecRegistry;
    private final static String COLLECTION_NAME = "genre";

    public GenreInitializerChangeLog(CodecRegistry codecRegistry) {
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
        mongoDatabase.withCodecRegistry(codecRegistry).getCollection(COLLECTION_NAME, Genre.class)
                .insertMany(clientSession, IntStream.range(0,3)
                        .mapToObj(GenreInitializerChangeLog::getGenre).collect(Collectors.toList()))
                .subscribe(subscriber);

        InsertManyResult result = subscriber.getFirst();
        logger.info("GenreInitializerChangeLog.execution wasAcknowledged: {}", result.wasAcknowledged());
        result.getInsertedIds()
                .forEach((key, value) -> logger.info("update id[{}] : {}", key, value));
    }

    @RollbackExecution
    public void rollbackExecution(ClientSession clientSession, MongoDatabase mongoDatabase) {
        SubscriberSync<DeleteResult> subscriber = new MongoSubscriberSync<>();

        mongoDatabase
                .withCodecRegistry(codecRegistry)
                .getCollection(COLLECTION_NAME, Genre.class)
                .deleteMany(clientSession, new Document())
                .subscribe(subscriber);
        DeleteResult result = subscriber.getFirst();
        logger.info("GenreInitializerChangeLog.rollbackExecution wasAcknowledged: {}", result.wasAcknowledged());
        logger.info("GenreInitializerChangeLog.rollbackExecution deleted count: {}", result.getDeletedCount());
    }


    private static Genre getGenre(int i) {
        return new Genre(UUID.randomUUID().toString(), "genre" + i);
    }
}
