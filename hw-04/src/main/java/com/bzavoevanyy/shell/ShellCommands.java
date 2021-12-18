package com.bzavoevanyy.shell;

import com.bzavoevanyy.config.AppProps;
import com.bzavoevanyy.domain.Participant;
import com.bzavoevanyy.service.ParticipantService;
import com.bzavoevanyy.service.QuizService;
import com.bzavoevanyy.shell.utils.ShellHelper;
import com.bzavoevanyy.shell.utils.ShellInputReader;
import com.bzavoevanyy.utils.MessageSourceWrapper;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Map;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {
    private final QuizService quizService;
    private final ShellHelper shellHelper;
    private final ParticipantService participantService;
    private final ShellInputReader inputReader;
    private final AppProps appProps;
    private final MessageSourceWrapper messageSource;
    private Participant participant;

    @ShellMethod("Run quiz.")
    @ShellMethodAvailability("checkRegistration")
    public void run() {
        quizService.runQuiz(participant);
    }

    @ShellMethod(value = "Register new test participant.", key = "signup")
    public String signup(@ShellOption(defaultValue = "") String userName) {
        var name = userName;
        if (name.isEmpty()) {
            shellHelper.printInfo(messageSource.getMessage("quiz.get-name"));
            do {
                name = inputReader.prompt(messageSource.getMessage("quiz.name"));
                if (StringUtils.hasText(name)) {
                    participant = participantService.create(name);
                } else {
                    shellHelper.printWarning(messageSource.getMessage("quiz.empty-name"));
                }
            } while (participant == null);
        } else {
            participant = participantService.create(name);
        }
        return shellHelper.getSuccessMessage(messageSource.getMessage("quiz.signup-user", participant.getName()));
    }

    @ShellMethod(value = "Choose language \"ru\" or \"en\".", key = "lang")
    public String chooseLang(@ShellOption(defaultValue = "") String language) {
        val options = Map.of("en", "English", "ru", "Russian");
        var lang = language;
        if (lang.isEmpty() || !options.containsKey(language)) {
            shellHelper.printInfo("Please choose your language");

            lang = inputReader.selectFromList("Languages: ",
                    "Please enter one of the [] values",
                    options, true, "en");
        }
        if (lang.equals("ru")) {
            appProps.setLocale(Locale.forLanguageTag("ru-RU"));
        } else {
            appProps.setLocale(Locale.ENGLISH);
        }
        return shellHelper.getSuccessMessage(String.format("You chose %s language", lang));
    }

    public Availability checkRegistration() {
        return participant != null ? Availability.available() :
                Availability.unavailable(messageSource.getMessage("quiz.signup"));
    }
}
