package com.bzavoevanyy.service;

import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import com.bzavoevanyy.repository.BookRepository;
import com.bzavoevanyy.repository.CustomBookRepository;
import com.bzavoevanyy.service.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final SequenceGeneratorServiceImpl generatorService;
    private final CustomBookRepository customBookRepository;

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @Override
    public Book createBook(String bookTitle, Author author, Genre genre) {
        return bookRepository.save(new Book(generatorService.generateSequence(Book.SEQUENCE_NAME),
                bookTitle, author, genre));
    }

    @Override
    public void deleteBookById(Long id) {
        customBookRepository.deleteBookByIdWithComments(id);
    }

    @Override
    public Book updateBook(Long bookId, String bookTitle, Author author, Genre genre) {
        val book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        book.setBookTitle(bookTitle);
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book);
    }
}
