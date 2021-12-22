package com.bzavoevanyy.service;

import com.bzavoevanyy.dao.BookDao;
import com.bzavoevanyy.domain.Author;
import com.bzavoevanyy.domain.Book;
import com.bzavoevanyy.domain.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public void createBook(Map<String, Object> bookArgs) {
        bookDao.insert(new Book(null,
                (String) bookArgs.get("book_title"),
                (Author) bookArgs.get("author"),
                (Genre) bookArgs.get("genre")));
    }

    @Override
    public void deleteBookById(long id) {
        bookDao.deleteById(id);
    }

    @Override
    public void updateBook(Map<String, Object> bookArgs) {
        bookDao.update(new Book(
                (Long) bookArgs.get("book_id"),
                (String) bookArgs.get("book_title"),
                (Author) bookArgs.get("author"),
                (Genre) bookArgs.get("genre")));
    }
}
