package com.bzavoevanyy.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class QuizResult {
    private final Participant participant;
    private final int score;

    public String getParticipantName() {
        return this.participant.getName();
    }
}
