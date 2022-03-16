package com.bzavoevanyy.rest.route;

import com.bzavoevanyy.domain.Comment;
import com.bzavoevanyy.repository.CommentRepository;
import com.bzavoevanyy.rest.route.dto.CommentDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest(CommentRouteConfig.class)
@DisplayName("CommentRouteConfigTest")
class CommentRouteConfigTest {
    @MockBean
    private CommentRepository repository;
    @Autowired
    private WebTestClient client;
    @Autowired
    private ObjectMapper mapper;

    private final static Comment COMMENT = new Comment("1", "1", "test comment", LocalDateTime.now());

    @Test
    @DisplayName(" should return list of all comments by bookId")
    public void should_return_list_of_all_comments_by_book_id() {
        given(repository.findAllByBookId("1")).willReturn(Flux.just(COMMENT));
        client
                .get()
                .uri("/api/comment?bookId=1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class);
    }

    @Test
    @DisplayName(" should save comment and return")
    public void should_save_comment_and_return() throws JsonProcessingException {
        given(repository.save(any())).willReturn(Mono.just(COMMENT));
        client
                .post()
                .uri("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(CommentDto.toDto(COMMENT)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDto.class);
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName(" should update comment and return")
    public void should_update_comment_and_return() throws JsonProcessingException {
        given(repository.save(any())).willReturn(Mono.just(COMMENT));
        client
                .put()
                .uri("/api/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(CommentDto.toDto(COMMENT)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CommentDto.class);
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName(" should delete comment")
    public void should_delete_comment() {
        given(repository.deleteById("1")).willReturn(Mono.empty().then());
        client
                .delete()
                .uri("/api/comment/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
        verify(repository, times(1)).deleteById("1");
    }
}