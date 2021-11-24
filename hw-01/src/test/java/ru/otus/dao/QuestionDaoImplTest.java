package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.domain.Question;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;

@DisplayName("QuestionDaoImpl test")
class QuestionDaoImplTest {
    private QuestionDao questionDao;

    @Test
    @DisplayName("Method findAll should return right List")
    void check_returned_list_by_method_findAll_with_good_csv() {
        questionDao = new QuestionDaoImpl("data/quiz.csv");
        List<Question> allQuestions = questionDao.findAll();
        assertThat(allQuestions).hasSize(5).allMatch(Objects::nonNull)
                .map(Question::getId).contains(1, 2, 3, 4, 5);
    }

    @Test
    @DisplayName("Method findAll should throw QuestionLoadingException")
    void check_findAll_throw_exception_with_wrong_csv() {
        questionDao = new QuestionDaoImpl("data/wrong.csv");
        assertThatThrownBy(() -> questionDao.findAll()).hasMessageContaining("Wrong CSV file");
    }
}