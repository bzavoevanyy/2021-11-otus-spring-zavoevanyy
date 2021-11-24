package ru.otus.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.ByteArrayOutputStream;

import static org.assertj.core.api.Assertions.*;
@DisplayName("QuizServiceImpl test")
class QuizServiceImplTest {
    private final ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
    @Test
    @DisplayName("Method fillUpBaos should fill up output stream")
    void should_fillUp_Baos() {
        QuizServiceImpl quizService = context.getBean(QuizServiceImpl.class);
        ByteArrayOutputStream baos = context.getBean(ByteArrayOutputStream.class);
        quizService.fillUpBaos();
        assertThat(baos.toString()).contains("Comparator", "Map", "collection");
    }
}