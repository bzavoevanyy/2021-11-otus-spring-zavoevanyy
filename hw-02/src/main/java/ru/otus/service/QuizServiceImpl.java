package ru.otus.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.domain.Participant;
import ru.otus.domain.Question;
import ru.otus.domain.QuizResult;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuestionService questionService;
    private final IOService ioService;
    private final int minScores;

    public QuizServiceImpl(QuestionService questionService, IOService ioService,
                           @Value("${quiz.minScore}") int minScores) {
        this.questionService = questionService;
        this.ioService = ioService;
        this.minScores = minScores;
    }


    @Override
    public void runQuiz() {
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
                val answer = ioService.readString("Your answer: ");
                checkAnswerResult = answer.matches("\\d") && Integer.parseInt(answer) <= optionsCount;
                if (checkAnswerResult) {
                    int answerToInt = Integer.parseInt(answer);
                    if (question.getOptions().get(answerToInt - 1).isRight()) {
                        rightAnswerCounter = rightAnswerCounter + 1;
                    }
                } else {
                    ioService.outString(String.format("Only 1 digit allowed from 1 to %s, please, try again\n",
                            optionsCount));
                }
            } while (!checkAnswerResult);
        }
        return new QuizResult(participant, rightAnswerCounter);
    }

    private Participant getParticipant() {
        String name = ioService.readString("Please, write your name: ");
        return new Participant(name);
    }

    private boolean checkResults(int score) {
        return this.minScores <= score;
    }


    private String makeResultMessage(QuizResult quizResult) {
        val resultMessage = new StringBuilder(quizResult.getParticipantName());
        val score = quizResult.getScore();
        val lineSeparator = System.getProperty("line.separator");
        resultMessage.append(lineSeparator);
        if (checkResults(score)) {
            resultMessage.append("You successfully passed the test");
        } else {
            resultMessage.append("You failed the test");
        }
        resultMessage.append(", your score is ");
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
