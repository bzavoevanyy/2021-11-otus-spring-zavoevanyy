package com.bzavoevanyy.repository;

import com.bzavoevanyy.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, Long> {
    @Query("{ 'bookId' : :#{#bookId}}")
    List<Comment> findAllByBookId(@Param("bookId") Long bookId);
}
