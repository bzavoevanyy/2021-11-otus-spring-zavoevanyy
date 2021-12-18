package com.bzavoevanyy.dao;

import com.bzavoevanyy.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
