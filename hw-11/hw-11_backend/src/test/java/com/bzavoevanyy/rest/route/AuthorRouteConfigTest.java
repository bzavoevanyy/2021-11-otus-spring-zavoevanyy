package com.bzavoevanyy.rest.route;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.repository.AuthorRepository;
import com.bzavoevanyy.rest.route.dto.AuthorDto;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest(AuthorRouteConfig.class)
@DisplayName("AuthorRouteConfigTest")
class AuthorRouteConfigTest {
    @MockBean
    private AuthorRepository repository;
    @Autowired
    private WebTestClient client;
    @Autowired
    private ObjectMapper mapper;

    private final static Author AUTHOR = new Author("1", "test author");

    @Test
    @DisplayName(" should return list of all authors")
    public void should_return_list_of_all_authors() {
        given(repository.findAll()).willReturn(Flux.just(AUTHOR));
        client
                .get()
                .uri("/api/author")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName(" should save author and return")
    public void should_save_author_and_return() throws JsonProcessingException {
        given(repository.save(any())).willReturn(Mono.just(AUTHOR));
        client
                .post()
                .uri("/api/author")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(AuthorDto.toDto(AUTHOR)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AuthorDto.class);
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName(" should delete author")
    public void should_delete_author() {
        given(repository.deleteById("1")).willReturn(Mono.empty().then());
        client
                .delete()
                .uri("/api/author/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
        verify(repository, times(1)).deleteById("1");
    }
}