package com.bzavoevanyy;

import com.bzavoevanyy.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class Hw03Application implements CommandLineRunner {

    private final QuizService quizService;

    public static void main(String[] args) {
        SpringApplication.run(Hw03Application.class, args);
    }

    @Override
    public void run(String... args) {
        quizService.runQuiz();
    }
}
