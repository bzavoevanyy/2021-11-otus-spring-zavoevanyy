package com.bzavoevanyy.services.exceptions;

public class GenreNotFoundException extends RuntimeException{
    public GenreNotFoundException() {
    }

    public GenreNotFoundException(String message) {
        super(message);
    }
}
