package com.bzavoevanyy.controllers.exceptions;

public class GenreHasBookException extends RuntimeException{
    public GenreHasBookException() {
        super();
    }

    public GenreHasBookException(String message) {
        super(message);
    }
}
