package com.bzavoevanyy.shell.service;

import com.bzavoevanyy.entities.Book;

public interface ShellBookService {
    Book createBookWithInputArgs();
    Book createBookWithInputArgs(Long id);
    String getBookString();
    String getBookString(Long id);
}
