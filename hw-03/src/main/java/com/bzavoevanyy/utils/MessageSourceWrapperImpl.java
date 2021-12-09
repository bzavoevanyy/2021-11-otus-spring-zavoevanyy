package com.bzavoevanyy.utils;

import com.bzavoevanyy.config.LocaleGetterProps;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSourceWrapperImpl implements MessageSourceWrapper {
    private final MessageSource messageSource;
    private final LocaleGetterProps localeProps;

    @Override
    public String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, localeProps.getLocale());
    }

    @Override
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, localeProps.getLocale());
    }
}
