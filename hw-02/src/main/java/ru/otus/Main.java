package ru.otus;

import org.springframework.context.annotation.*;
import ru.otus.service.QuizService;

@Configuration
@ComponentScan("ru.otus")
@PropertySource("classpath:application.properties")
public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        QuizService quizService = context.getBean(QuizService.class);
        quizService.runQuiz();
    }
}
