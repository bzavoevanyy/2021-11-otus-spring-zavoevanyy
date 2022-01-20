package com.bzavoevanyy.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Genre {
    @Transient
    public static final String SEQUENCE_NAME = "genre_sequence";
    @Id
    private Long genreId;
    private String genreName;
}
