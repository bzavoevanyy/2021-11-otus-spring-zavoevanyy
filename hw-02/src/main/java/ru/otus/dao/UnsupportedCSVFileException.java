package ru.otus.dao;

public class UnsupportedCSVFileException extends RuntimeException{
    public UnsupportedCSVFileException(String message) {
        super(message);
    }

    public UnsupportedCSVFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
