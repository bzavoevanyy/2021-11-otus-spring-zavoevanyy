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
public class Author {
    @Transient
    public static final String SEQUENCE_NAME = "author_sequence";
    @Id
    private Long authorId;
    private String authorName;

}
