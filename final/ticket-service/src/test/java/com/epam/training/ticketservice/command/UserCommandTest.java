package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.ui.command.UserCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.shell.Availability;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

class UserCommandTest {

    private final static User ADMIN = new User("admin", "admin", true);
    private final static User USER = new User("user", "user");

    private UserCommand underTest;

    @Mock
    private UserService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new UserCommand(accountService);
    }

    @Test
    void testSignInPrivilegedShouldReturnIncorrectCredentialsWhenUsernameIsNotFound() {
        // Given
        final String expected = "Login failed due to incorrect credentials";
        given(accountService.getAccountById(ADMIN.getUsername())).willReturn(Optional.empty());

        // When
        final String actual = underTest.signInPrivileged(ADMIN.getUsername(), ADMIN.getPassword());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInPrivilegedShouldReturnIncorrectCredentialsWhenPasswordIsNotCorrect() {
        // Given
        final String expected = "Login failed due to incorrect credentials";
        given(accountService.getAccountById(ADMIN.getUsername())).willReturn(Optional.of(ADMIN));

        // When
        final String actual = underTest.signInPrivileged(ADMIN.getUsername(), "wrong-password");

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInPrivilegedShouldReturnPrivilegedErrorWhenDefaultUserTriesToSignIn() {
        // Given
        final String expected = "'user' is not a privileged user";
        given(accountService.getAccountById(USER.getUsername())).willReturn(Optional.of(USER));

        // When
        final String actual = underTest.signInPrivileged(USER.getUsername(), USER.getPassword());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testSignInPrivilegedShouldReturnSuccessMessageWhenEverythingIsFine() {
        // Given
        final String expected = "Successfully signed in";
        given(accountService.getAccountById(ADMIN.getUsername())).willReturn(Optional.of(ADMIN));
        doNothing().when(accountService).signIn(ADMIN);

        // When
        final String actual = underTest.signInPrivileged(ADMIN.getUsername(), ADMIN.getPassword());

        // Then
        verify(accountService).signIn(ADMIN);
        assertEquals(expected, actual);
    }

    @Test
    void testSignOutShouldReturnSuccessMessageAndCallService() {
        // Given
        final String expected = "Successfully signed out";
        doNothing().when(accountService).signOut();

        // When
        final String actual = underTest.signOut();

        // Then
        verify(accountService).signOut();
        assertEquals(expected, actual);
    }

    @Test
    void testDescribeAccountShouldReturnErrorMessageWhenNotSignedIn() {
        // Given
        final String expected = "You are not signed in";
        given(accountService.getLoggedInUser()).willReturn(Optional.empty());

        // When
        final String actual = underTest.describeAccount();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testDescribeAccountShouldReturnFormattedMessageWhenSignedIn() {
        // Given
        final String expected = "Formatted message";
        given(accountService.getLoggedInUser()).willReturn(Optional.of(USER));
        given(accountService.formattedAccountDescription(USER)).willReturn(expected);

        // When
        final String actual = underTest.describeAccount();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testCheckLoggedInAvailabilityShouldReturnUnavailableWhenNoOneIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("you are not signed in.");
        given(accountService.getLoggedInUser()).willReturn(Optional.empty());

        // When
        final Availability actual = underTest.checkLoggedInAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    void testCheckLoggedInAvailabilityShouldReturnAvailableWhenSomeoneIsSignedIn() {
        // Given
        final Availability expected = Availability.available();
        given(accountService.getLoggedInUser()).willReturn(Optional.of(USER));

        // When
        final Availability actual = underTest.checkLoggedInAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }
}
