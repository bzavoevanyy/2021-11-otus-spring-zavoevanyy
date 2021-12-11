package com.bzavoevanyy.service;

import com.bzavoevanyy.utils.MessageSourceWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class IOServiceImpl implements IOService {
    private final PrintStream out;
    private final Scanner scanner;
    private final MessageSourceWrapper messageSource;

    public IOServiceImpl(@Value("#{T(System).out}") PrintStream out,
                         @Value("#{T(System).in}") BufferedInputStream inputStream,
                         MessageSourceWrapper messageSource) {
        this.out = out;
        this.scanner = new Scanner(inputStream);
        this.messageSource = messageSource;
    }

    @Override
    public void outString(String message, Object... args) {
        out.print(messageSource.getMessage(message, args));
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public String readString(String message, Object... args) {
        outString(message, args);
        return readString();
    }
}
