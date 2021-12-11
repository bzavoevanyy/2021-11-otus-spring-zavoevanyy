package com.bzavoevanyy.service;

import com.bzavoevanyy.config.AppProps;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import com.bzavoevanyy.domain.Participant;
import com.bzavoevanyy.domain.Question;
import com.bzavoevanyy.domain.QuizResult;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuestionService questionService;
    private final IOService ioService;
    private final AppProps props;

    @Override
    public void runQuiz() {
        chooseLanguage();
        val participant = getParticipant();
        val questions = questionService.getAllQuestions();
        val quizResult = quizProcessing(participant, questions);
        showResultMessage(quizResult);
    }

    private QuizResult quizProcessing(Participant participant, List<Question> questions) {
        var rightAnswerCounter = 0;
        for (Question question : questions) {
            ioService.outString(makeQuestionMessage(List.of(question)));
            boolean checkAnswerResult;
            val optionsCount = question.getOptions().size();
            do {
                val answer = ioService.readString("quiz.get-answer");
                checkAnswerResult = answer.matches("\\d") && Integer.parseInt(answer) <= optionsCount;
                if (checkAnswerResult) {
                    val answerToInt = Integer.parseInt(answer);
                    if (question.getOptions().get(answerToInt - 1).isRight()) {
                        rightAnswerCounter = rightAnswerCounter + 1;
                    }
                } else {
                    ioService.outString("quiz.wrong-input", optionsCount);
                    ioService.outString(System.lineSeparator());
                }
            } while (!checkAnswerResult);
        }
        return new QuizResult(participant, rightAnswerCounter);
    }

    private void chooseLanguage() {
        val message = new StringBuilder();
        val lineSeparator = System.lineSeparator();
        ioService.outString("quiz.choose-lang");
        message.append(lineSeparator)
                .append("1. English")
                .append(lineSeparator)
                .append("2. Russian")
                .append(lineSeparator);
        val s = ioService.readString(message.toString());
        if (s.matches("\\d")) {
            val i = Integer.parseInt(s);
            if (i > 0 && i <= 2) {
                if (i == 1) {
                    props.setLocale(Locale.ENGLISH);
                } else {
                    props.setLocale(Locale.forLanguageTag("ru-RU"));
                }
            }
        }
    }

    private Participant getParticipant() {
        val name = ioService.readString("quiz.get-name");
        return new Participant(name);
    }

    private boolean checkResults(int score) {
        return props.getMinScore() <= score;
    }


    private void showResultMessage(QuizResult quizResult) {
        if (checkResults(quizResult.getScore())) {
            ioService.outString("quiz.test-pass", quizResult.getParticipantName());
        } else {
            ioService.outString("quiz.test-fail", quizResult.getParticipantName());
        }
        ioService.outString("quiz.test-scores", quizResult.getScore());
        ioService.outString(System.lineSeparator());
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
