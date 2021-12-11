package com.bzavoevanyy.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "quiz")
@Getter
@Setter
public class AppProps implements QuestionDaoProps, LocaleGetterProps {
    private int minScore;
    private String[] headers;
    private Locale locale;
    private String fileName;

    public String getFileName() {
        return String.format(this.fileName, "_", this.locale);
    }
}
