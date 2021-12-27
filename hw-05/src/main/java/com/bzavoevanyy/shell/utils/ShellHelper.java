package com.bzavoevanyy.shell.utils;

public interface ShellHelper {
    String getColored(String message, PromptColor color);

    String getInfoMessage(String message);

    String getSuccessMessage(String message);

    String getWarningMessage(String message);

    String getErrorMessage(String message);

    void print(String Message);

    void printSuccess(String Message);

    void printInfo(String Message);

    void printWarning(String Message);

    void printError(String Message);

    void print(String Message, PromptColor color);
}
