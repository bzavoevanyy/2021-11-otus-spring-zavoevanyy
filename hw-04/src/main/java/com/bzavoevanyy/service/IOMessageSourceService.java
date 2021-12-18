package com.bzavoevanyy.service;


public interface IOMessageSourceService {
    void writeMessage(String code, Object... args);

    String readWithMessage(String code, Object... args);
}
