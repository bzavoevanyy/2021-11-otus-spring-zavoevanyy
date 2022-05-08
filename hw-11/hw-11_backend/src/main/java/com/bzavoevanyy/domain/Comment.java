package com.bzavoevanyy.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
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
    @Id
    private String id;
    private String bookId;
    private String comment;
    @EqualsAndHashCode.Exclude
    private LocalDateTime date;
}
