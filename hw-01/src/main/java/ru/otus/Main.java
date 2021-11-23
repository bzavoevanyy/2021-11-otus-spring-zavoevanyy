package ru.otus;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.domain.Question;
import ru.otus.service.QuestionServiceImpl;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        var questionService = context.getBean(QuestionServiceImpl.class);
        var allQuestions = questionService.getAllQuestions();
        for (Question question : allQuestions) {
            System.out.printf("%s %s%n", question.getId(), question.getQuestion());
            for (int i = 0; i < question.getOptions().size(); i++) {
                System.out.printf("   %s %s%n", i + 1, question.getOptions().get(i));
            }
        }
    }
}
