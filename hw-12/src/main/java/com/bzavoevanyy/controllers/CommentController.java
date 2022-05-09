package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.CommentDto;
import com.bzavoevanyy.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment")
    public RedirectView createComment(@Valid CommentDto commentDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errMsg", "Input data is incorrect");
            return new RedirectView("/book/" + commentDto.getBookId());
        }
        commentService.createComment(commentDto.getComment(), commentDto.getBookId());
        return new RedirectView("/book/" + commentDto.getBookId());
    }
    @PostMapping("/comment/{id}")
    public RedirectView updateComment(@PathVariable long id,
                                @Valid CommentDto commentDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errMsg", "Input data is incorrect");
            return new RedirectView("/book/" + commentDto.getBookId());
        }
        commentService.updateById(id, commentDto.getComment());
        return new RedirectView("/book/" + commentDto.getBookId());
    }
    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable long id, CommentDto commentDto) {
        commentService.deleteById(id);
        return "redirect:/book/" + commentDto.getBookId();
    }
}
