package com.bzavoevanyy.shell.service;

import com.bzavoevanyy.domain.Participant;
import com.bzavoevanyy.service.ParticipantService;
import com.bzavoevanyy.shell.utils.ShellHelper;
import com.bzavoevanyy.shell.utils.ShellInputReader;
import com.bzavoevanyy.utils.MessageSourceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ShellParticipantServiceImpl implements ShellParticipantService {
    private final ShellHelper shellHelper;
    private final MessageSourceWrapper messageSource;
    private final ShellInputReader inputReader;
    private final ParticipantService participantService;

    @Override
    public Participant signUpParticipant(String userName) {
        var name = userName;
        Participant participant = null;
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
        return participant;
    }
}
