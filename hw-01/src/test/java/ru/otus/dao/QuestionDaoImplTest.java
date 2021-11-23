package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import ru.otus.domain.Question;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

class QuestionDaoImplTest {
    private QuestionDao questionDao;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        questionDao = new QuestionDaoImpl("data/quiz.csv");
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Get all questions from csv")
    void findAll() {
        List<Question> allQuestions = questionDao.findAll();
        assertThat(allQuestions.size()).isEqualTo(5);
    }
}