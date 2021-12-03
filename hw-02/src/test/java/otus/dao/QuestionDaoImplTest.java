package otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.dao.QuestionDao;
import ru.otus.dao.QuestionDaoImpl;
import ru.otus.domain.AnswerOption;
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
        questionDao = new QuestionDaoImpl("data/quiztest.csv");
        List<Question> allQuestions = questionDao.findAll();
        assertThat(allQuestions).hasSize(1).allMatch(Objects::nonNull)
                .allSatisfy(question -> {
                   assertThat(question.getId()).isEqualTo(1);
                   assertThat(question.getQuestion()).isEqualTo("question1");
                   assertThat(question.getOptions()).map(AnswerOption::getOption)
                           .containsOnly("option1", "option2", "option3", "option4");
                   assertThat(question.getOptions().get(0).isRight()).isEqualTo(true);
                });
    }

    @Test
    @DisplayName("Method findAll should throw QuestionLoadingException")
    void check_findAll_throw_exception_with_wrong_csv() {
        questionDao = new QuestionDaoImpl("data/wrong.csv");
        assertThatThrownBy(() -> questionDao.findAll()).hasMessageContaining("Wrong CSV file");
    }
}