package com.bzavoevanyy.services.Impl;

import com.bzavoevanyy.entities.Genre;
import com.bzavoevanyy.repositories.GenreRepository;
import com.bzavoevanyy.services.GenreService;
import com.bzavoevanyy.services.exceptions.GenreNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }

    @Override
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id).orElseThrow(GenreNotFoundException::new);
    }

    @Override
    @Transactional
    public Genre createGenre(String name) {
        return genreRepository.save(new Genre(null, name));
    }

    @Override
    @Transactional
    public void deleteGenreById(Long id) {
        genreRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Genre updateGenre(Long id, String name) {
        val genre = genreRepository.findById(id).orElseThrow(GenreNotFoundException::new);
        genre.setName(name);
        return genreRepository.save(genre);
    }
}
