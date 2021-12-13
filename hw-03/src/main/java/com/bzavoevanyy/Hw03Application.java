package com.bzavoevanyy;

import com.bzavoevanyy.config.AppProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProps.class)
public class Hw03Application {

    public static void main(String[] args) {
        SpringApplication.run(Hw03Application.class, args);
    }
}
