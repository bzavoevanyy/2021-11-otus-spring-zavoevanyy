package com.bzavoevanyy.service;

import com.bzavoevanyy.config.AppProps;
import com.bzavoevanyy.utils.MessageSourceWrapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import com.bzavoevanyy.domain.Participant;
import com.bzavoevanyy.domain.Question;
import com.bzavoevanyy.domain.QuizResult;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class QuizRunner implements CommandLineRunner {

    private final QuestionService questionService;
    private final IOService ioService;
    private final AppProps appProps;
    private final MessageSourceWrapper messageSource;

    @Override
    public void run(String... args) {
        chooseLanguage();
        val participant = getParticipant();
        val questions = questionService.getAllQuestions();
        val quizResult = quizProcessing(participant, questions);
        val resultMessage = makeResultMessage(quizResult);
        ioService.outString(resultMessage);
    }

    private QuizResult quizProcessing(Participant participant, List<Question> questions) {
        var rightAnswerCounter = 0;
        for (Question question : questions) {
            ioService.outString(makeQuestionMessage(List.of(question)));
            boolean checkAnswerResult;
            val optionsCount = question.getOptions().size();
            do {
                val answer = ioService.readString(messageSource.getMessage("quiz.get-answer"));
                checkAnswerResult = answer.matches("\\d") && Integer.parseInt(answer) <= optionsCount;
                if (checkAnswerResult) {
                    val answerToInt = Integer.parseInt(answer);
                    if (question.getOptions().get(answerToInt - 1).isRight()) {
                        rightAnswerCounter = rightAnswerCounter + 1;
                    }
                } else {
                    val message = messageSource.getMessage("quiz.wrong-input");
                    ioService.outString(String.format(message, optionsCount));
                    ioService.outString(System.lineSeparator());
                }
            } while (!checkAnswerResult);
        }
        return new QuizResult(participant, rightAnswerCounter);
    }

    private void chooseLanguage() {
        val message = new StringBuilder();
        val lineSeparator = System.lineSeparator();
        message.append(messageSource.getMessage("quiz.choose-lang"))
                .append(lineSeparator)
                .append("1. English")
                .append(lineSeparator)
                .append("2. Russian")
                .append(lineSeparator);
        val s = ioService.readString(message.toString());
        if (s.matches("\\d")) {
            val i = Integer.parseInt(s);
            if (i > 0 && i <= 2) {
                if (i == 1) {
                    appProps.setLocale(Locale.ENGLISH);
                } else {
                    appProps.setLocale(Locale.forLanguageTag("ru-RU"));
                }
            }
        }
    }

    private Participant getParticipant() {
        val message = messageSource
                .getMessage("quiz.get-name");
        val name = ioService.readString(message);
        return new Participant(name);
    }

    private boolean checkResults(int score) {
        return appProps.getMinScore() <= score;
    }


    private String makeResultMessage(QuizResult quizResult) {
        val resultMessage = new StringBuilder(quizResult.getParticipantName());
        val score = quizResult.getScore();
        val lineSeparator = System.getProperty("line.separator");
        resultMessage.append(lineSeparator);
        if (checkResults(score)) {
            val message = messageSource.getMessage("quiz.test-pass");
            resultMessage.append(message);
        } else {
            val message = messageSource.getMessage("quiz.test-fail");
            resultMessage.append(message);
        }
        resultMessage.append(messageSource.getMessage("quiz.test-scores"));
        resultMessage.append(quizResult.getScore());
        resultMessage.append(lineSeparator);
        return resultMessage.toString();
    }

    private String makeQuestionMessage(List<Question> questions) {
        return questions.stream().map(question -> {
            val result = new StringBuilder();
            result.append(String.format("%s %s%n", question.getId(), question.getQuestion()));
            for (var i = 0; i < question.getOptions().size(); i++) {
                result.append(String.format("   %s %s%n", i + 1, question.getOption(i)));
            }
            return result.toString();
        }).reduce(String::concat).orElse("Default string");
    }
}
