package com.bzavoevanyy.controllers.exceptions;

public class AuthorHasBookException extends RuntimeException{
    public AuthorHasBookException() {
        super();
    }

    public AuthorHasBookException(String message) {
        super(message);
    }
}
