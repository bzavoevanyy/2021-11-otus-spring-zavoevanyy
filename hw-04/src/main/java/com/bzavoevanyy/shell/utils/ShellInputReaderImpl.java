package com.bzavoevanyy.shell.utils;

import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class ShellInputReaderImpl implements ShellInputReader {
    public static final Character DEFAULT_MASK = '*';

    private final Character mask;
    private final LineReader lineReader;
    private final ShellHelper shellHelper;

    @Autowired
    public ShellInputReaderImpl(@Lazy LineReader lineReader, ShellHelper shellHelper) {
        this(lineReader, shellHelper, null);
    }

    public ShellInputReaderImpl(LineReader lineReader, ShellHelper shellHelper, Character mask) {
        this.lineReader = lineReader;
        this.shellHelper = shellHelper;
        this.mask = mask != null ? mask : DEFAULT_MASK;
    }

    public String prompt(String prompt) {
        return prompt(prompt, null, true);
    }

    public String prompt(String prompt, String defaultValue) {
        return prompt(prompt, defaultValue, true);
    }

    public String prompt(String prompt, String defaultValue, boolean echo) {
        String answer;
        if (echo) {
            answer = lineReader.readLine(prompt + ": ");
        } else {
            answer = lineReader.readLine(prompt + ": ", mask);
        }
        if (ObjectUtils.isEmpty(answer)) {
            return defaultValue;
        }
        return answer;
    }

    public String selectFromList(String headingMessage,
                                 String promptMessage,
                                 Map<String, String> options,
                                 boolean ignoreCase,
                                 String defaultValue) {
        String answer;
        Set<String> allowedAnswers = new HashSet<>(options.keySet());
        if (defaultValue != null && !defaultValue.equals("")) {
            allowedAnswers.add("");
        }
        shellHelper.print(String.format("%s: ", headingMessage));
        do {
            for (Map.Entry<String, String> option : options.entrySet()) {
                String defaultMarker = null;
                if (defaultValue != null) {
                    if (option.getKey().equals(defaultValue)) {
                        defaultMarker = "*";
                    }
                }
                if (defaultMarker != null) {
                    shellHelper.printInfo(String.format("%s [%s] %s ", defaultMarker, option.getKey(), option.getValue()));
                } else {
                    shellHelper.print(String.format("  [%s] %s", option.getKey(), option.getValue()));
                }
            }
            answer = lineReader.readLine(String.format("%s: ", promptMessage));
        } while (!containsString(allowedAnswers, answer, ignoreCase) && !"".equals(answer));
        if (ObjectUtils.isEmpty(answer) && allowedAnswers.contains("")) {
            return defaultValue;
        }
        return answer;
    }

    private boolean containsString(Set<String> l, String s, boolean ignoreCase) {
        if (!ignoreCase) {
            return l.contains(s);
        }
        return l.stream().map(String::toLowerCase).anyMatch(str -> str.equals(s.toLowerCase()));
    }
}
