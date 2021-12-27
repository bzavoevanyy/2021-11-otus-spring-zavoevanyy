package com.bzavoevanyy.shell.service;

import com.bzavoevanyy.config.AppProps;
import com.bzavoevanyy.shell.utils.ShellHelper;
import com.bzavoevanyy.shell.utils.ShellInputReader;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class ShellLangServiceImpl implements ShellLangService {
    private final AppProps appProps;
    private final ShellHelper shellHelper;
    private final ShellInputReader inputReader;

    @Override
    public void setLocaleProp(String language) {
        val options = appProps.getLanguages();
        var lang = language;
        if (lang.isEmpty() || !options.containsKey(language)) {
            shellHelper.printInfo("Please choose your language");

            lang = inputReader.selectFromList("Languages: ",
                    "Please enter one of the [] values",
                    options, true, options.get("en"));
        }
        appProps.setLocale(Locale.forLanguageTag(options.get(lang)));
    }
}
