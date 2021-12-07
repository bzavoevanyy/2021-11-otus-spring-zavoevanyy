package com.bzavoevanyy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class IOServiceImpl implements IOService {
    private final PrintStream out;
    private final Scanner scanner;

    public IOServiceImpl(@Value("#{T(System).out}") PrintStream out, @Value("#{T(System).in}") BufferedInputStream inputStream) {
        this.out = out;
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public void outString(String message) {
        out.print(message);
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public String readString(String message) {
        outString(message);
        return readString();
    }
}
