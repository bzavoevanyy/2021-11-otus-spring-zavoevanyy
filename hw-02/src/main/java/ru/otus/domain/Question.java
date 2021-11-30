package ru.otus.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
@RequiredArgsConstructor
@Getter
@ToString
public class Question {
    private final int id;
    private final String question;
    private final List<String> options;
    private final int rightAnswerIndex;
}
