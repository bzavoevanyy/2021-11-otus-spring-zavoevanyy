package ru.otus.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Participant {
    private String name;
    private int score;
}
