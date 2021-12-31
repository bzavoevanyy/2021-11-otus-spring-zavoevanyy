package com.bzavoevanyy.shell.utils;

import com.bzavoevanyy.shell.utils.config.ShellColors;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ShellHelperImpl implements ShellHelper {
    private final ShellColors colors;
    private final Terminal terminal;

    public ShellHelperImpl(ShellColors colors, @Lazy Terminal terminal) {
        this.colors = colors;

        this.terminal = terminal;
    }

    public String getColored(String message, PromptColor color) {
        return (new AttributedStringBuilder()).append(message, AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle())).toAnsi();
    }

    public String getInfoMessage(String message) {
        return getColored(message, colors.getInfo());
    }

    public String getSuccessMessage(String message) {
        return getColored(message, colors.getSuccess());
    }

    public String getWarningMessage(String message) {
        return getColored(message, colors.getWarning());
    }

    public String getErrorMessage(String message) {
        return getColored(message, colors.getError());
    }

    public void print(String message) {
        print(message, null);
    }

    public void printSuccess(String message) {
        print(message, colors.getSuccess());
    }

    public void printInfo(String message) {
        print(message, colors.getInfo());
    }

    public void printWarning(String message) {
        print(message, colors.getWarning());
    }

    public void printError(String message) {
        print(message, colors.getError());
    }

    public void print(String message, PromptColor color) {
        String toPrint = message;
        if (color != null) {
            toPrint = getColored(message, color);
        }
        terminal.writer().println(toPrint);
        terminal.flush();
    }
}
