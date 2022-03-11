package com.bzavoevanyy.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Comment {
    @Transient
    public static final String SEQUENCE_NAME = "comment_sequence";
    @Id
    private Long commentId;
    private Long bookId;
    private String comment;
    @EqualsAndHashCode.Exclude
    private LocalDateTime date;
}
