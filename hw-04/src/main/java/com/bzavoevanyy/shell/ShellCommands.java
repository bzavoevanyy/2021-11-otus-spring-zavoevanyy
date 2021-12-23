package com.bzavoevanyy.shell;

import com.bzavoevanyy.domain.Participant;
import com.bzavoevanyy.service.QuizService;
import com.bzavoevanyy.shell.service.ShellLangService;
import com.bzavoevanyy.shell.service.ShellParticipantService;
import com.bzavoevanyy.shell.utils.ShellHelper;
import com.bzavoevanyy.utils.MessageSourceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommands {
    private final QuizService quizService;
    private final ShellHelper shellHelper;
    private final MessageSourceWrapper messageSource;
    private final ShellLangService langService;
    private final ShellParticipantService shellParticipantService;
    private Participant participant;

    @ShellMethod("Run quiz.")
    @ShellMethodAvailability("checkRegistration")
    public void run() {
        quizService.runQuiz(participant);
    }

    @ShellMethod(value = "Register new test participant.", key = "signup")
    public String signup(@ShellOption(defaultValue = "") String userName) {
        this.participant = shellParticipantService.signUpParticipant(userName);
        return shellHelper.getSuccessMessage(messageSource.getMessage("quiz.signup-user", participant.getName()));
    }

    @ShellMethod(value = "Choose language.", key = "lang")
    public String chooseLang(@ShellOption(defaultValue = "") String language) {
        langService.setLocaleProp(language);
        return shellHelper.getSuccessMessage("You chose successfully language");
    }

    public Availability checkRegistration() {
        return participant != null ? Availability.available() :
                Availability.unavailable(messageSource.getMessage("quiz.signup"));
    }
}
