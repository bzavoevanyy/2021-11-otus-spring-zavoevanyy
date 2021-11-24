package ru.otus.service;

import ru.otus.domain.Question;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class QuizServiceImpl implements QuizService {
    private final PrintStream printStream;
    private final QuestionService questionService;
    private final ByteArrayOutputStream baos;

    public QuizServiceImpl(PrintStream printStream, QuestionService questionService, ByteArrayOutputStream baos) {
        this.printStream = printStream;
        this.questionService = questionService;
        this.baos = baos;
    }

    @Override
    public void showAllQuestions() {
        fillUpBaos();
        System.out.print(baos.toString());
    }
    void fillUpBaos() {
        final var allQuestions = questionService.getAllQuestions();
        for (Question question : allQuestions) {
            printStream.printf("%s %s%n", question.getId(), question.getQuestion());
            for (int i = 0; i < question.getOptions().size(); i++) {
                printStream.printf("   %s %s%n", i + 1, question.getOptions().get(i));
            }
        }
    }
}
