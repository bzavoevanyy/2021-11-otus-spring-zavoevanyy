package com.bzavoevanyy.shell.service;

public interface ShellBookService {
    Long updateBookWithInputArgs();
    int updateBookWithInputArgs(long id);
    String getBookString();
    String getBookString(long id);
}
