package com.bzavoevanyy.services.Impl;

import com.bzavoevanyy.entities.User;
import com.bzavoevanyy.entities.enums.UserRoles;
import com.bzavoevanyy.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryUserDetailServiceTest {

    private final static String USERNAME = "test";
    private final static String PASSWORD = "password";
    private final static User USER = new User(1L, "admin@google.com", "test", PASSWORD, UserRoles.ADMIN);
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private LibraryUserDetailService userDetailsService;

    @Test
    @DisplayName("should create UserDetails Object")
    public void shouldCreateUserForUserName() {

        when(userRepository.findUserByName(USERNAME)).thenReturn(Optional.of(USER));
        final var expectedUserDetails = org.springframework.security.core.userdetails.User
                .builder()
                .username(USERNAME)
                .password(PASSWORD)
                .roles(String.valueOf(UserRoles.ADMIN))
                .build();

        final var userDetails = userDetailsService.loadUserByUsername(USERNAME);

        assertThat(userDetails).usingRecursiveComparison().isEqualTo(expectedUserDetails);

    }

    @Test
    @DisplayName("should throw UserNotFoundException")
    public void shouldThrowUserNotFoundException() {

        when(userRepository.findUserByName(USERNAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(USERNAME))
                .isInstanceOf(UsernameNotFoundException.class);

    }
}