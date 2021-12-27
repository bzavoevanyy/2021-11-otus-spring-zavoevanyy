package com.bzavoevanyy.shell.utils.config;

import com.bzavoevanyy.shell.utils.PromptColor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "shell.out")
@Getter
@Setter
public class ShellColors {
    private PromptColor info;
    private PromptColor success;
    private PromptColor warning;
    private PromptColor error;
}
