package com.bzavoevanyy.shell;

import com.bzavoevanyy.service.BookService;
import com.bzavoevanyy.shell.service.ShellBookService;
import com.bzavoevanyy.shell.utils.ShellHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommand {
    private final BookService bookService;
    private final ShellHelper shellHelper;
    private final ShellBookService shellBookService;

    @ShellMethod(value = "Get all books.", key = "get-books")
    public String getAllBooks() {
        return shellHelper.getInfoMessage(shellBookService.getBookString());
    }

    @ShellMethod(value = "Get book by id.", key = "get-book")
    public String getBookById(@ShellOption long id) {
        return shellHelper.getInfoMessage(shellBookService.getBookString(id));
    }

    @ShellMethod(value = "Delete book by id.", key="delete-book")
    public String deleteBookById(@ShellOption long id) {
        bookService.deleteBookById(id);
        return shellHelper.getSuccessMessage(String.format("Book with id %s successfully deleted", id));
    }
    @ShellMethod(value = "Create book.", key = "create-book")
    public String createBook() {
        Long id = shellBookService.updateBookWithInputArgs();
        return shellHelper.getSuccessMessage(String.format("Book successfully created with id %s", id));
    }
    @ShellMethod(value = "Update book.", key = "update-book")
    public String updateBook(@ShellOption long id) {
        int res = shellBookService.updateBookWithInputArgs(id);
        return shellHelper.getSuccessMessage("Book successfully updated");
    }
}
