package com.bzavoevanyy.service;

import com.bzavoevanyy.utils.MessageSourceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IOMessageSourceServiceImpl implements IOMessageSourceService {
    private final IOService ioService;
    private final MessageSourceWrapper messageSource;
    @Override
    public void writeMessage(String code, Object... args) {
        ioService.outString(messageSource.getMessage(code, args));
    }

    @Override
    public String readWithMessage(String code, Object... args) {
        return ioService.readString(messageSource.getMessage(code, args));
    }
}
