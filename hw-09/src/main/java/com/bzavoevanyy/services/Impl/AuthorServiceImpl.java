package com.bzavoevanyy.services.Impl;

import com.bzavoevanyy.entities.Author;
import com.bzavoevanyy.repositories.AuthorRepository;
import com.bzavoevanyy.services.AuthorService;
import com.bzavoevanyy.services.exceptions.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    @Transactional
    public Author createAuthor(String name) {
        return authorRepository.save(new Author(null, name));
    }

    @Override
    @Transactional
    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Author updateAuthor(Long id, String name) {
        val author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        author.setName(name);
        return authorRepository.save(author);
    }
}
