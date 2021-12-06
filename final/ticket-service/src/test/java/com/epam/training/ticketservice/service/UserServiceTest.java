package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistance.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistance.repository.ScreeningId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class UserServiceTest {

    private final static String dateTimePattern = "yyyy-MM-dd HH:mm";

    private final static User admin = new User("admin", "admin", true);
    private final static Room room1 = new Room("Room1", 10, 10);
    private final static Room room2 = new Room("Room2", 10, 10);
    private final static Movie movie1 = new Movie("Movie1", "action", 100);
    private final static Movie movie2 = new Movie("Movie2", "animation", 110);
    private final static Screening screening1 = new Screening(new ScreeningId(movie1, room1,
            LocalDateTime.parse("2000-12-13 10:10", DateTimeFormatter.ofPattern(dateTimePattern))));
    private final static Screening screening2 = new Screening(new ScreeningId(movie2, room2,
            LocalDateTime.parse("2000-12-15 15:00", DateTimeFormatter.ofPattern(dateTimePattern))));

    private UserService underTest;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new UserService(userRepository);
    }

    @Test
    void testSignInShouldSetLoggedInAccountToGivenAccount() {
        // Given + When
        underTest.signIn(admin);

        // Then
        assertEquals(Optional.of(admin), underTest.getLoggedInUser());
    }

    @Test
    void testSignOutShouldSetLoggedInAccountToOptionalEmpty() {
        // Given + When
        underTest.signOut();

        // Then
        assertEquals(Optional.empty(), underTest.getLoggedInUser());
    }

    @Test
    void testGetAccountByIdShouldCallFindById() {
        // Given + When
        underTest.getAccountById(admin.getUsername());

        // Then
        verify(userRepository).findById(admin.getUsername());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testFormattedAccountDescriptionShouldReturnExpectedResultWhenIsAdmin() {
        // Given
        final String expected = "Signed in with privileged account 'admin'";

        // When
        final String actual = underTest.formattedAccountDescription(admin);

        // Then
        assertEquals(expected, actual);
    }
}
