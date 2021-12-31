package com.bzavoevanyy.services.Impl;

import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.entities.Book;
import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.repositories.exceptions.BookNotFoundException;
import com.bzavoevanyy.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    @Override
    @Transactional
    public Book createBook(String bookTitle, Author author, Genre genre) {
        return bookRepository.save(new Book(null, bookTitle, author, genre, new ArrayList<>()));
    }

    @Override
    @Transactional
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Book updateBook(Long bookId, String bookTitle, Author author, Genre genre) {
        val book = bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        book.setTitle(bookTitle);
        book.setAuthor(author);
        book.setGenre(genre);
        return bookRepository.save(book);
    }
}
