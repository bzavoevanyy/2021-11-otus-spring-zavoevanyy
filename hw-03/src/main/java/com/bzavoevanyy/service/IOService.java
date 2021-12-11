package com.bzavoevanyy.service;

public interface IOService {
    void outString(String message, Object ...args);
    String readString();
    String readString(String message, Object ...args);
}
