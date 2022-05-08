package com.bzavoevanyy.rest.route;

import com.bzavoevanyy.repository.CommentRepository;
import com.bzavoevanyy.rest.route.dto.CommentDto;
import com.bzavoevanyy.rest.route.dto.CustomResponse;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class CommentRouteConfig {

    @Bean
    public RouterFunction<ServerResponse> commentComposeRoute(CommentRepository repository) {
        val handler = new CommentHandler(repository);
        return route()
                .GET("/api/comment", handler::getByBookId)
                .POST("/api/comment", handler::create)
                .PUT("/api/comment", handler::update)
                .DELETE("/api/comment/{id}", handler::delete)
                .build();
    }
    static class CommentHandler {
        private final CommentRepository repository;

        CommentHandler(CommentRepository repository) {
            this.repository = repository;
        }

        Mono<ServerResponse> getByBookId(ServerRequest request) {
            val bookId = request.queryParam("bookId").orElseThrow();
            val commentDtoFlux = repository.findAllByBookId(bookId).map(CommentDto::toDto);
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(commentDtoFlux, CommentDto.class);
        }

        Mono<ServerResponse> create(ServerRequest request) {
            val commentMono = request.bodyToMono(CommentDto.class)
                    .map(CommentDto::toDomainObject)
                    .doOnNext(comment -> comment.setId(UUID.randomUUID().toString()))
                    .doOnNext(comment -> comment.setDate(LocalDateTime.now()));
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromPublisher(commentMono.flatMap(repository::save)
                            .map(CommentDto::toDto), CommentDto.class));
        }

        Mono<ServerResponse> update(ServerRequest request) {
            val commentMono = request.bodyToMono(CommentDto.class)
                    .map(CommentDto::toDomainObject)
                    .doOnNext(comment -> comment.setDate(LocalDateTime.now()));
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromPublisher(commentMono.flatMap(repository::save)
                            .map(CommentDto::toDto), CommentDto.class));
        }

        Mono<ServerResponse> delete(ServerRequest request) {
            val commentId = request.pathVariable("id");
            return repository.deleteById(commentId)
                    .then(ok().body(fromValue(new CustomResponse("Deleted"))));
        }
    }

}
