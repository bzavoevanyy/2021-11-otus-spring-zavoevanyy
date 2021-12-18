package com.bzavoevanyy.dao;

import com.bzavoevanyy.config.AppProps;
import com.bzavoevanyy.config.LocaleGetterProps;
import com.bzavoevanyy.config.QuestionDaoProps;
import com.bzavoevanyy.domain.AnswerOption;
import com.bzavoevanyy.domain.Question;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor
class QuestionDaoImplTest {


    @TestConfiguration
    static class Configuration {
        @Bean
        public LocaleGetterProps props() {
            return new AppProps();
        }
    }

    @MockBean
    private QuestionDaoProps props;

    @Autowired
    private QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        when(props.getHeaders()).thenReturn(
                new String[]{"id", "question", "option1", "option2", "option3", "option4", "rightAnswer"});

    }

    @Test
    @DisplayName("Method findAll should return right List")
    void check_returned_list_by_method_findAll_with_good_csv() {
        when(props.getFileName()).thenReturn("data/quiz-test.csv");
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
        when(props.getFileName()).thenReturn("data/wrong.csv");
        assertThatThrownBy(() -> questionDao.findAll()).hasMessageContaining("Wrong CSV file");
    }
}