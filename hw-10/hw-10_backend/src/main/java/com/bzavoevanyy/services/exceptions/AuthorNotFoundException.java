package com.bzavoevanyy.services.exceptions;

public class AuthorNotFoundException extends RuntimeException{
    public AuthorNotFoundException() {
    }

    public AuthorNotFoundException(String message) {
        super(message);
    }
}
