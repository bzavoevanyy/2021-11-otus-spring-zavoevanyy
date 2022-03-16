package com.bzavoevanyy.rest.route;

import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.repository.GenreRepository;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@WebFluxTest(GenreRouteConfig.class)
@DisplayName("GenreRouteConfigTest")
class GenreRouteConfigTest {

    @MockBean
    private GenreRepository repository;
    @Autowired
    private WebTestClient client;

    private final static Genre GENRE = new Genre("1", "test genre");

    @Test
    @DisplayName(" should return list of all genres")
    public void should_return_list_of_all_genres() {
        given(repository.findAll()).willReturn(Flux.just(GENRE));
        client
                .get()
                .uri("/api/genre")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class);
    }

    @Test
    @DisplayName(" should save genre and return")
    public void should_save_genre_and_return() {
        given(repository.save(any())).willReturn(Mono.just(GENRE));
        client
                .post()
                .uri("/api/genre")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class);
    }

    @Test
    @DisplayName(" should delete genre")
    public void should_delete_genre() {
        given(repository.deleteById(anyString())).willReturn(Mono.empty().then());
        client
                .delete()
                .uri("/api/genre/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

}