package com.bzavoevanyy.exceptions;

public class AuthorHasBooksException extends RuntimeException{
    public AuthorHasBooksException(String message) {
        super(message);
    }
}
