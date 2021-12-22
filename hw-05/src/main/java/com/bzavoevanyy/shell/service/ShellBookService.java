package com.bzavoevanyy.shell.service;

import java.util.Map;

public interface ShellBookService {
    Map<String, Object> getBookArgs();
    Map<String, Object> getBookArgs(long id);
    String getBookString();
    String getBookString(long id);
}
