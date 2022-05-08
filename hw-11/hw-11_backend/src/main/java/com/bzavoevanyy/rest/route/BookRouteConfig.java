package com.bzavoevanyy.rest.route;

import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.exceptions.BookNotFoundException;
import com.bzavoevanyy.repository.BookRepository;
import com.bzavoevanyy.repository.CommentRepository;
import com.bzavoevanyy.rest.route.dto.BookDto;
import com.bzavoevanyy.rest.route.dto.CustomResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
@RequiredArgsConstructor
public class BookRouteConfig {

    @Bean
    public RouterFunction<ServerResponse> bookComposeRoute(BookRepository repository, CommentRepository commentRepository) {
        val handler = new BookHandler(repository, commentRepository);
        return route()
                .GET("/api/book", handler::getAll)
                .GET("/api/book/{id}", handler::getById)
                .POST("/api/book", handler::create)
                .PUT("/api/book", handler::update)
                .DELETE("/api/book/{id}", handler::delete)
                .build();
    }

    static class BookHandler {
        private final BookRepository bookRepository;
        private final CommentRepository commentRepository;

        BookHandler(BookRepository bookRepository, CommentRepository commentRepository) {
            this.bookRepository = bookRepository;
            this.commentRepository = commentRepository;
        }

        Mono<ServerResponse> getAll(ServerRequest request) {
            Flux<BookDto> bookDtoFlux = bookRepository.findAll().map(BookDto::toDto);
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bookDtoFlux, BookDto.class);
        }

        Mono<ServerResponse> getById(ServerRequest request) {
            String bookId = request.pathVariable("id");
            Mono<BookDto> bookDtoMono = bookRepository.findById(bookId).map(BookDto::toDto);
            Mono<ServerResponse> notFound = notFound().build();
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bookDtoMono, BookDto.class)
                    .switchIfEmpty(notFound);
        }

        Mono<ServerResponse> create(ServerRequest request) {
            Mono<Book> bookMono = request.bodyToMono(BookDto.class)
                    .map(BookDto::toDomainObject)
                    .doOnNext(book -> book.setId(UUID.randomUUID().toString()));
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromPublisher(bookMono.flatMap(bookRepository::save)
                            .map(BookDto::toDto), BookDto.class));
        }

        Mono<ServerResponse> update(ServerRequest request) {
            Mono<Book> bookMono = request.bodyToMono(BookDto.class).map(BookDto::toDomainObject);
            return ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromPublisher(bookMono.flatMap(bookRepository::save)
                            .map(BookDto::toDto), BookDto.class));
        }

        Mono<ServerResponse> delete(ServerRequest request) {
            String bookId = request.pathVariable("id");
            return bookRepository.findById(bookId)
                    .switchIfEmpty(Mono.error(new BookNotFoundException("Book not found")))
                    .flatMap(book -> commentRepository.findAllByBookId(bookId)
                            .flatMap(commentRepository::delete)
                            .then(bookRepository.deleteById(bookId)))
                    .then(ok()
                            .body(fromValue(new CustomResponse("Deleted"))))
                    .onErrorResume(e -> Mono.just(fromValue(new CustomResponse(e.getMessage())))
                            .flatMap(s -> badRequest()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(s)));
        }
    }

}
