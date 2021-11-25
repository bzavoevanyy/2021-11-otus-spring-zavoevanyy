package ru.otus.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.domain.Question;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
@DisplayName("QuizService test")
class QuizServiceImplTest {
    private QuizService quizService;
    private ByteArrayOutputStream bos;

    @BeforeEach
    void setUp() {
        bos = new ByteArrayOutputStream();
        IOService ioService = new ConsoleIOService(new PrintStream(bos), new BufferedInputStream(System.in));
        QuestionService questionService = mock(QuestionService.class);
        when(questionService.getAllQuestions()).thenReturn(List.of(
                new Question(1, "question1", List.of("option1", "option2", "option3", "option4"), 1)));
        quizService = new QuizServiceImpl(questionService, ioService);
    }
    @DisplayName("Method showAllQuestions should print right string")
    @Test
    void showAllQuestions() {
        quizService.showAllQuestions();
        assertThat(bos.toString()).isEqualTo("1 question1\n" +
                "   1 option1\n" +
                "   2 option2\n" +
                "   3 option3\n" +
                "   4 option4\n");
    }
}