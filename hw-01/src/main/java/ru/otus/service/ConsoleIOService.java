package ru.otus.service;

import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleIOService implements IOService {
    private final PrintStream out;
    private final Scanner scanner;

    public ConsoleIOService(PrintStream out, BufferedInputStream inputStream) {
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
}
