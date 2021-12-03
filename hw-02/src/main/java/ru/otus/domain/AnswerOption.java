package ru.otus.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class AnswerOption {
    private final String option;
    private boolean right;
}
