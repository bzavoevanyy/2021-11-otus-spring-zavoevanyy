package com.bzavoevanyy.controllers;

import com.bzavoevanyy.controllers.dto.GenreDto;
import com.bzavoevanyy.repositories.BookRepository;
import com.bzavoevanyy.services.GenreService;
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
public class GenreController {
    private final GenreService genreService;
    private final BookRepository bookRepository;

    @GetMapping("/genre")
    public String getAllGenres(ModelMap model) {
        val genreDtos = genreService.getAll()
                .stream().map(GenreDto::toDto).collect(Collectors.toList());
        model.addAttribute("genres", genreDtos);
        return "genre";
    }
    @PostMapping("/genre")
    public RedirectView createGenre(@Valid GenreDto genreDto,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errMsg", "Input data is incorrect");
            return new RedirectView("/genre");
        }
        genreService.createGenre(genreDto.getGenreName());
        return new RedirectView("/genre");
    }
    @PostMapping("/genre/{id}")
    public RedirectView updateGenre(@PathVariable long id, @Valid GenreDto genreDto,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errMsg", "Input data is incorrect");
            return new RedirectView("/genre");
        }
        genreService.updateGenre(id, genreDto.getGenreName());
        return new RedirectView("/genre");
    }
    @PostMapping("/genre/delete/{id}")
    public RedirectView deleteGenre(@PathVariable long id, RedirectAttributes attributes) {

        val booksWithGenreId = bookRepository.countAllByGenreId(id);

        if (booksWithGenreId > 0) {
            attributes.addFlashAttribute("errMsg",
                    String.format("There is/are %s book(s) by this genre. Please firstly remove it", booksWithGenreId));
            return new RedirectView("/genre");
        }
        genreService.deleteGenreById(id);
        return new RedirectView("/genre");
    }
}
