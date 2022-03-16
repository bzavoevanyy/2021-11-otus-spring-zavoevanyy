package com.bzavoevanyy.rest.route;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.repository.AuthorRepository;
import com.bzavoevanyy.rest.route.dto.AuthorDto;
import com.bzavoevanyy.rest.route.dto.SuccessResponse;
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
public class AuthorRouteConfig {

    @Bean
    public RouterFunction<ServerResponse> authorComposeRoute(AuthorRepository repository) {
        val handler = new AuthorHandler(repository);
        return route()
                .GET("/api/author", handler::getAll)
                .POST("/api/author", handler::create)
                .DELETE("/api/author/{id}", handler::delete)
                .build();
    }

    static public class AuthorHandler {
        private final AuthorRepository repository;

        AuthorHandler(AuthorRepository repository) {
            this.repository = repository;
        }

        Mono<ServerResponse> getAll(ServerRequest request) {
            val authorDtoFlux = repository.findAll().map(AuthorDto::toDto);
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(authorDtoFlux, AuthorDto.class);
        }

        Mono<ServerResponse> create(ServerRequest request) {
            val authorMono = request.bodyToMono(Author.class)
                    .doOnNext(author -> author.setId(UUID.randomUUID().toString()));
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromPublisher(authorMono.flatMap(repository::save).map(AuthorDto::toDto), AuthorDto.class));
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
