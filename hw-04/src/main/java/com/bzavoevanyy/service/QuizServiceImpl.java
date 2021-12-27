package com.bzavoevanyy.service;

import com.bzavoevanyy.config.AppProps;
import com.bzavoevanyy.domain.Participant;
import com.bzavoevanyy.domain.Question;
import com.bzavoevanyy.domain.QuizResult;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuestionService questionService;
    private final IOService ioService;
    private final IOMessageSourceService ioMessageSourceService;
    private final AppProps props;

    @Override
    public void runQuiz(Participant participant) {
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
                val answer = ioMessageSourceService.readWithMessage("quiz.get-answer");
                checkAnswerResult = answer.matches("\\d") && Integer.parseInt(answer) <= optionsCount;
                if (checkAnswerResult) {
                    val answerToInt = Integer.parseInt(answer);
                    if (question.getOptions().get(answerToInt - 1).isRight()) {
                        rightAnswerCounter = rightAnswerCounter + 1;
                    }
                } else {
                    ioMessageSourceService.writeMessage("quiz.wrong-input", optionsCount);
                    ioService.outString(System.lineSeparator());
                }
            } while (!checkAnswerResult);
        }
        return new QuizResult(participant, rightAnswerCounter);
    }

    private boolean checkResults(int score) {
        return props.getMinScore() <= score;
    }


    private void showResultMessage(QuizResult quizResult) {
        if (checkResults(quizResult.getScore())) {
            ioMessageSourceService.writeMessage("quiz.test-pass", quizResult.getParticipantName());
        } else {
            ioMessageSourceService.writeMessage("quiz.test-fail", quizResult.getParticipantName());
        }
        ioMessageSourceService.writeMessage("quiz.test-scores", quizResult.getScore());
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
