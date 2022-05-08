package com.bzavoevanyy.rest.route;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.repository.BookRepository;
import com.bzavoevanyy.rest.route.dto.BookDto;
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

@WebFluxTest(BookRouteConfig.class)
@DisplayName("BookRouterConfig test")
class BookRouteConfigTest {

    @Autowired
    private WebTestClient client;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookRepository repository;
    private final static Author AUTHOR = new Author("1", "test author");
    private final static Genre GENRE = new Genre("1", "test genre");
    private final static Book BOOK = new Book("1", "test book", AUTHOR, GENRE);

    @Test
    @DisplayName(" should return list of all books")
    public void should_return_list_of_all_books() {
        given(repository.findAll()).willReturn(Flux.just(BOOK));
        client
                .get()
                .uri("/api/book")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class);
    }

    @Test
    @DisplayName(" should save book and return")
    public void should_save_book_and_return() throws JsonProcessingException {
        given(repository.save(any())).willReturn(Mono.just(BOOK));
        client
                .post()
                .uri("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(BookDto.toDto(BOOK)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class);
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName(" should update book and return")
    public void should_update_book_and_return() throws JsonProcessingException {
        given(repository.save(any())).willReturn(Mono.just(BOOK));
        client
                .put()
                .uri("/api/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(BookDto.toDto(BOOK)))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookDto.class);
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName(" should delete book")
    public void should_delete_book() {
        given(repository.deleteById("1")).willReturn(Mono.empty().then());
        client
                .delete()
                .uri("/api/book/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
        verify(repository, times(1)).deleteById("1");
    }
}