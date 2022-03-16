package com.bzavoevanyy.rest.route;

import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.repository.GenreRepository;
import com.bzavoevanyy.rest.route.dto.GenreDto;
import com.bzavoevanyy.rest.route.dto.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Configuration
@RequiredArgsConstructor
public class GenreRouteConfig {

    @Bean
    public RouterFunction<ServerResponse> genreComposeRoute(GenreRepository repository) {
        val handler = new GenreHandler(repository);
        return route()
                .GET("/api/genre", handler::getAll)
                .POST("/api/genre", handler::create)
                .DELETE("/api/genre/{id}", handler::delete)
                .build();
    }
    static class GenreHandler {
        private final GenreRepository repository;

        GenreHandler(GenreRepository repository) {
            this.repository = repository;
        }

        Mono<ServerResponse> getAll(ServerRequest request) {
            val genreDtoFlux = repository.findAll().map(GenreDto::toDto);
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(genreDtoFlux, GenreDto.class);
        }

        Mono<ServerResponse> create(ServerRequest request) {
            val genreMono = request.bodyToMono(Genre.class)
                    .doOnNext(genre -> genre.setId(UUID.randomUUID().toString()));
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromPublisher(genreMono.flatMap(repository::save)
                            .map(GenreDto::toDto), GenreDto.class));
        }

        Mono<ServerResponse> delete(ServerRequest request) {
            val id = request.pathVariable("id");
            return repository.deleteById(id)
                    .then(ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(fromValue(new SuccessResponse("deleted"))));
        }
    }

}
