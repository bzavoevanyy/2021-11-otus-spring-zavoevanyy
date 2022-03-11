package com.bzavoevanyy.mongock.changelog;

import com.bzavoevanyy.domain.Comment;
import com.bzavoevanyy.repository.CommentRepository;
import com.bzavoevanyy.service.SequenceGeneratorService;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import lombok.val;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
@ChangeLog(order = "004")
public class CommentInitializerChangeLog {

    @ChangeSet(author = "bzavoevanyy", id = "insert-comments", order = "001")
    public void insertComments(SequenceGeneratorService generatorService, CommentRepository commentRepository) {
        val comments = LongStream
                .generate(() -> generatorService.generateSequence(Comment.SEQUENCE_NAME))
                .limit(5).mapToObj(CommentInitializerChangeLog::getComment).collect(Collectors.toList());
        commentRepository.saveAll(comments);
    }

    private static Comment getComment(Long id) {
        return new Comment(id, 1L, "comment" + id, LocalDateTime.now());
    }
}
