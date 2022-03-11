package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.AuthorDto;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.services.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
    private final BookRepository bookRepository;

    @GetMapping("/author")
    public String getAllAuthors(ModelMap model) {
        val authorDtos = authorService.getAll()
                .stream().map(AuthorDto::toDto).collect(Collectors.toList());
        model.addAttribute("authors", authorDtos);
        return "author";
    }
    @PostMapping("/author")
    public RedirectView createAuthor(@Valid AuthorDto authorDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errMsg", "Input data is incorrect");
            return new RedirectView("/author");
        }
        authorService.createAuthor(authorDto.getAuthorName());
        return new RedirectView("/author");
    }
    @PostMapping("/author/{id}")
    public RedirectView updateAuthor(@PathVariable long id, @Valid AuthorDto authorDto,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errMsg", "Input data is incorrect");
            return new RedirectView("/author");
        }
        authorService.updateAuthor(id, authorDto.getAuthorName());
        return new RedirectView("/author");
    }
    @PostMapping("/author/delete/{id}")
    public RedirectView deleteAuthor(@PathVariable long id, RedirectAttributes attributes) {

        val booksWithAuthorId = bookRepository.countAllByAuthorId(id);

        if (booksWithAuthorId > 0) {
            attributes.addFlashAttribute("errMsg",
                    String.format("There is/are %s book(s) by this author. Please firstly remove it", booksWithAuthorId));
            return new RedirectView("/author");
        }
        authorService.deleteAuthorById(id);
        return new RedirectView("/author");
    }
}
