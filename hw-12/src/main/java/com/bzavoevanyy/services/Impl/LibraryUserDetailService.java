package com.bzavoevanyy.services.Impl;

import com.bzavoevanyy.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@RequiredArgsConstructor
public class LibraryUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("Trying to find user in db, username: {}", username);

        final var user = userRepository
                .findUserByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        log.info("User {} found, building UserDetails", username);

        return User
                .withUsername(username)
                .password(user.getPassword())
                .roles(String.valueOf(user.getRole()))
                .build();
    }
}
