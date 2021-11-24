package ru.otus;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.service.QuizService;
import ru.otus.service.QuizServiceImpl;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        final QuizService quizService = context.getBean(QuizServiceImpl.class);
        quizService.showAllQuestions();
    }
}
