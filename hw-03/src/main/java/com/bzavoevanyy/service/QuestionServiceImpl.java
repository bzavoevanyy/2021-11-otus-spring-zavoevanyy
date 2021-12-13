package com.bzavoevanyy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.bzavoevanyy.dao.QuestionDao;
import com.bzavoevanyy.domain.Question;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;

    @Override
    public List<Question> getAllQuestions() {
        return questionDao.findAll();
    }
}
