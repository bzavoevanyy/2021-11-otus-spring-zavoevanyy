package com.bzavoevanyy.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Book {
    @Transient
    public static final String SEQUENCE_NAME = "book_sequence";
    @Id
    private Long id;

    private String bookTitle;

    @Field("author")
    private Author author;
    @Field("genre")
    private Genre genre;

    public String getAuthorName() {
        return this.author.getAuthorName();
    }

    public String getGenreName() {
        return this.genre.getGenreName();
    }
}
