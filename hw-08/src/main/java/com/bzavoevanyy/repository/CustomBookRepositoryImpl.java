package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Comment;
import com.bzavoevanyy.service.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CustomBookRepositoryImpl implements CustomBookRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public void deleteBookByIdWithComments(Long id) {
        val bookForDelete = mongoTemplate
                .findAndRemove(new Query().addCriteria(Criteria.where("_id").is(id)), Book.class);
        if (bookForDelete != null) {
            mongoTemplate
                    .findAllAndRemove(new Query().addCriteria(Criteria.where("bookId").is(id)), Comment.class);
        } else {
            throw new BookNotFoundException();
        }
    }
}
