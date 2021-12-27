package com.bzavoevanyy.domain;

import lombok.Data;

@Data
public class Book {
    private final Long id;
    private final String title;
    private final Author author;
    private final Genre genre;

    public String getAuthorName() {
        return this.author.getName();
    }

    public String getGenreName() {
        return this.genre.getName();
    }
}
