package ru.otus.domain;

import java.util.List;

public class Question {
    private final int id;
    private final String question;
    private final List<String> options;
    private final int rightAnswerIndex;

    public Question(int id, String question, List<String> options, int rightAnswerIndex) {
        this.id = id;
        this.question = question;
        this.options = options;
        this.rightAnswerIndex = rightAnswerIndex;
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getRightAnswerIndex() {
        return rightAnswerIndex;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", rightAnswerIndex=" + rightAnswerIndex +
                '}';
    }
}
