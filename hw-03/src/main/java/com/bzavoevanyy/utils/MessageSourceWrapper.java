package com.bzavoevanyy.utils;

public interface MessageSourceWrapper {
    String getMessage(String code, Object[] args);
    String getMessage(String code);
}
