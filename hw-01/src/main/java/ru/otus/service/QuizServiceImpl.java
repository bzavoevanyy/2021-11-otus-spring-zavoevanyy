package ru.otus.service;


import ru.otus.domain.Question;

public class QuizServiceImpl implements QuizService {

    private final QuestionService questionService;
    private final IOService ioService;


    public QuizServiceImpl(QuestionService questionService, IOService console) {
        this.questionService = questionService;
        this.ioService = console;
    }

    @Override
    public void showAllQuestions() {
        final var allQuestions = questionService.getAllQuestions();
        for (Question question : allQuestions) {
            ioService.outString(String.format("%s %s%n", question.getId(), question.getQuestion()));
            for (int i = 0; i < question.getOptions().size(); i++) {
                ioService.outString(String.format("   %s %s%n", i + 1, question.getOptions().get(i)));
            }
        }
    }
}
