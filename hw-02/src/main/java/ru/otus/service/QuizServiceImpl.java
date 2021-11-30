package ru.otus.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.domain.Participant;
import ru.otus.domain.Question;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuestionService questionService;
    private final IOService ioService;
    private final Participant participant;
    private final int minScores;

    public QuizServiceImpl(QuestionService questionService, IOService ioService, Participant participant,
                           @Value("#{T(Integer).parseInt(${quiz.minScore})}") int minScores) {
        this.questionService = questionService;
        this.ioService = ioService;
        this.participant = participant;
        this.minScores = minScores;
    }


    @Override
    public void runQuiz() {
        val userName = getUserInput("Please, write your name: ");
        participant.setName(userName);
        val questions = questionService.getAllQuestions();
        var rightAnswerCounter = 0;
        for (Question question : questions) {
            ioService.outString(makeQuestionMessage(List.of(question)));
            boolean checkAnswer;
            val optionsCount = question.getOptions().size();
            do {
                val answer = getUserInput("Your answer: ");
                checkAnswer = answer.matches("\\d") && Integer.parseInt(answer) <= optionsCount;
                if (checkAnswer) {
                    if (Integer.parseInt(answer) == question.getRightAnswerIndex()) {
                        rightAnswerCounter = rightAnswerCounter + 1;
                    }
                } else {
                    ioService.outString(String.format("Only 1 digit allowed from 1 to %s, please, try again\n",
                            optionsCount));
                }
            } while (!checkAnswer);

        }
        participant.setScore(rightAnswerCounter);

        ioService.outString(makeResultMessage(participant));

    }

    @Override
    public void showAllQuestions() {
        val s = makeQuestionMessage(questionService.getAllQuestions());
        ioService.outString(s);
    }

    private boolean checkResults(int score) {
        return this.minScores <= score;
    }

    private String getUserInput(String message) {
        ioService.outString(message);
        return ioService.readString();
    }

    private String makeResultMessage(Participant participant) {
        var resultMessage = new StringBuilder(participant.getName());
        val score = participant.getScore();
        if (checkResults(score)) {
            resultMessage.append("\nYou successfully passed the test");
        } else {
            resultMessage.append("\nYou failed the test");
        }
        resultMessage.append(", your score is ");
        resultMessage.append(participant.getScore());
        resultMessage.append("\n");
        return resultMessage.toString();
    }

    private String makeQuestionMessage(List<Question> questions) {
        return questions.stream().map(question -> {
            val result = new StringBuilder();
            result.append(String.format("%s %s%n", question.getId(), question.getQuestion()));
            for (int i = 0; i < question.getOptions().size(); i++) {
                result.append(String.format("   %s %s%n", i + 1, question.getOptions().get(i)));
            }
            return result.toString();
        }).reduce(String::concat).orElse("Default string");
    }
}
