package com.bzavoevanyy.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AppProps {
    private int minScore;
    private String[] headers;
    private Locale locale;
}
