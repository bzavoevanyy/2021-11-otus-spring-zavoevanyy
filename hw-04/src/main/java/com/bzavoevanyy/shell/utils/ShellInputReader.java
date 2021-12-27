package com.bzavoevanyy.shell.utils;

import java.util.Map;

public interface ShellInputReader {
    String prompt(String prompt);

    String prompt(String prompt, String defaultValue);

    String prompt(String prompt, String defaultValue, boolean echo);

    String selectFromList(String headingMessage, String promptMessage, Map<String, String> options, boolean ignoreCase, String defaultValue);
}
