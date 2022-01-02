package com.bzavoevanyy.shell;

import com.bzavoevanyy.shell.service.ShellCommentService;
import com.bzavoevanyy.shell.utils.ShellHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommentCommands {
    private final ShellHelper shellHelper;
    private final ShellCommentService shellCommentService;

    @ShellMethod(value = "Get comments by book id.", key = "comment-read-all")
    public String getCommentsByBookId(@ShellOption Long bookId) {
        return shellHelper.getInfoMessage(shellCommentService.getCommentStringByBookId(bookId));
    }

    @ShellMethod(value = "Create new comment for book.", key = "comment-create")
    public String createNewComment(@ShellOption Long bookId) {
        shellCommentService.writeCommentByBookId(bookId);
        return shellHelper.getSuccessMessage("Comment successfully added");
    }

    @ShellMethod(value = "Update comment.", key = "comment-update")
    private String updateComment(@ShellOption Long commentId) {
        shellCommentService.updateCommentByCommentId(commentId);
        return shellHelper.getSuccessMessage("Comment successfully updated");
    }

    @ShellMethod(value = "Delete comment by coommentId.", key = "comment-delete")
    private String deleteComment(@ShellOption Long commentId) {
        shellCommentService.deleteCommentById(commentId);
        return shellHelper.getSuccessMessage("Comment successfully deleted");
    }
}
