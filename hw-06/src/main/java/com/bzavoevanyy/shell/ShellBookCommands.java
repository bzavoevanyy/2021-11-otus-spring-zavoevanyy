package com.bzavoevanyy.shell;

import com.bzavoevanyy.services.BookService;
import com.bzavoevanyy.shell.service.ShellBookService;
import com.bzavoevanyy.shell.utils.ShellHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class ShellBookCommands {
    private final BookService bookService;
    private final ShellHelper shellHelper;
    private final ShellBookService shellBookService;


    @ShellMethod(value = "Get all books.", key = "book-read-all")
    public String getAllBooks() {
        return shellHelper.getInfoMessage(shellBookService.getBookString());
    }

    @ShellMethod(value = "Get book by id.", key = "book-read")
    public String getBookById(@ShellOption Long bookId) {
        return shellHelper.getInfoMessage(shellBookService.getBookString(bookId));
    }

    @ShellMethod(value = "Delete book by id.", key="book-delete")
    public String deleteBookById(@ShellOption Long bookId) {
        bookService.deleteBookById(bookId);
        return shellHelper.getSuccessMessage(String.format("Book with id %s successfully deleted", bookId));
    }
    @ShellMethod(value = "Create book.", key = "book-create")
    public String createBook() {
        Long id = shellBookService.createBookWithInputArgs().getId();
        return shellHelper.getSuccessMessage(String.format("Book successfully created with id %s", id));
    }
    @ShellMethod(value = "Update book.", key = "book-update")
    public String updateBook(@ShellOption Long bookId) {
        shellBookService.createBookWithInputArgs(bookId);
        return shellHelper.getSuccessMessage("Book successfully updated");
    }

}
