package com.bzavoevanyy.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
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

    @Id
    private String id;

    private String bookTitle;

    @Field("author")
    private Author author;
    @Field("genre")
    private Genre genre;

}
