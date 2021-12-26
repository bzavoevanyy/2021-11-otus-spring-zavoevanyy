package com.bzavoevanyy.service;

import com.bzavoevanyy.dao.BookDao;
import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookDao bookDao;

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

    @Override
    public List<Book> getBookById(long id) {
        return List.of(bookDao.getById(id));
    }

    @Override
    public Long createBook(String bookTitle, Author author, Genre genre) {
        return bookDao.insert(new Book(null, bookTitle, author, genre));
    }

    @Override
    public void deleteBookById(long id) {
        bookDao.deleteById(id);
    }

    @Override
    public int updateBook(Long bookId, String bookTitle, Author author, Genre genre) {
        return bookDao.update(new Book(bookId, bookTitle, author, genre));
    }
}
