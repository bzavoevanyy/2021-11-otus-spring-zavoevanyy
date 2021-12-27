package com.bzavoevanyy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "quiz")
@Getter
@Setter
public class AppProps implements QuestionDaoProps, LocaleGetterProps {
    private int minScore;
    private String[] headers;
    private Locale locale;
    private String fileName;
    private Map<String, String> languages;

    public String getFileName() {
        return String.format(this.fileName, "_", this.locale);
    }
}
