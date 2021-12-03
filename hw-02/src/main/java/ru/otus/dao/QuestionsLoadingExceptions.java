package ru.otus.dao;

public class QuestionsLoadingExceptions extends RuntimeException{
    public QuestionsLoadingExceptions(String message) {
        super(message);
    }

    public QuestionsLoadingExceptions(String message, Throwable cause) {
        super(message, cause);
    }
}
