package com.bzavoevanyy.dao;

import com.bzavoevanyy.config.AppProps;
import com.bzavoevanyy.domain.AnswerOption;
import com.bzavoevanyy.domain.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class QuestionDaoImplTest {

    private final AppProps appProps = mock(AppProps.class);
    private final MessageSource messageSource = mock(MessageSource.class);
    private QuestionDaoImpl questionDao;

    @BeforeEach
    void setUp() {
        when(appProps.getHeaders()).thenReturn(
                new String[]{"id", "question", "option1", "option2", "option3", "option4", "rightAnswer"});
    }

    @Test
    @DisplayName("Method findAll should return right List")
    void check_returned_list_by_method_findAll_with_good_csv() {
        when(messageSource.getMessage("quiz.source", null, appProps.getLocale()))
                .thenReturn("data/quiz-test.csv");
        questionDao = new QuestionDaoImpl(appProps, messageSource);
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
        when(messageSource.getMessage("quiz.source", null, appProps.getLocale()))
                .thenReturn("data/wrong.csv");
        questionDao = new QuestionDaoImpl(appProps, messageSource);
        assertThatThrownBy(() -> questionDao.findAll()).hasMessageContaining("Wrong CSV file");
    }
}