package com.bzavoevanyy.shell;

import com.bzavoevanyy.config.AppProps;
import com.bzavoevanyy.domain.Participant;

import com.bzavoevanyy.service.ParticipantService;
import com.bzavoevanyy.shell.utils.ShellHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Test shell commands")
class ShellCommandsTest {

    @MockBean
    private AppProps appProps;
    @MockBean
    private ParticipantService participantService;
    @Autowired
    private ShellHelper shellHelper;
    @Autowired
    private Shell shell;


    private static final String COMMAND_LANG_RU = "lang ru";
    private static final String COMMAND_LANG_EN = "lang en";
    private static final String COMMAND_SIGNUP = "signup John";

    @Test
    @DisplayName(" should set Locale in appProps to ru and return right message")
    public void shouldChangeLangToRussian() {
        doNothing().when(appProps).setLocale(any());

        String resultMessage = (String) shell.evaluate(() -> COMMAND_LANG_RU);
        String expectedMessage = shellHelper.getSuccessMessage("You chose ru language");

        verify(appProps, times(1)).setLocale(any());
        assertThat(resultMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName(" should set Locale in appProps to en and return right message")
    public void shouldChangeLangToEnglish() {
        doNothing().when(appProps).setLocale(any());

        String resultMessage = (String) shell.evaluate(() -> COMMAND_LANG_EN);
        String expectedMessage = shellHelper.getSuccessMessage("You chose en language");

        verify(appProps, times(1)).setLocale(any());
        assertThat(resultMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName(" should create new test participant and return right message")
    public void shouldCreateNewParticipant() {
        when(participantService.create(anyString())).thenReturn(new Participant("John"));
        when(appProps.getLocale()).thenReturn(Locale.ENGLISH);

        String resultMessage = (String) shell.evaluate(() -> COMMAND_SIGNUP);
        String expectedMessage = shellHelper.getSuccessMessage("User John signed up");

        verify(participantService, times(1)).create(anyString());
        assertThat(resultMessage).isEqualTo(expectedMessage);
    }

    @Test
    @DisplayName(" should return CommandCurrentlyNotAvailable")
    public void shouldReturnCommandNotCurrentlyAvailableException() {
        Object res = shell.evaluate(() -> "run");
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }

}