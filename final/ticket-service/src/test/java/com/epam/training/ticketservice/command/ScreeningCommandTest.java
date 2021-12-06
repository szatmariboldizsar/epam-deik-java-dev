package com.epam.training.ticketservice.cli;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.core.screening.persistance.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistance.repository.ScreeningId;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.ui.command.ScreeningCommand;
import com.epam.training.ticketservice.ui.exception.NoSuchItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.shell.Availability;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class ScreeningCommandTest {

    private final static User ADMIN = new User("admin", "admin", true);
    private final static User USER = new User("user", "user");
    private final static String movieName = "Movie";
    private final static String roomName = "Room";
    private final static String startingAt = "2000-20-13 12:30";

    private ScreeningCommand underTest;

    @Mock
    private UserService accountService;

    @Mock
    private ScreeningService screeningService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new ScreeningCommand(screeningService, accountService);
    }

    @Test
    void testCreateScreeningShouldReturnErrorMessageWhenNoSuchItemExceptionOccurs() {
        // Given
        final String expected = "Error";
        doThrow(new NoSuchItemException(expected)).when(screeningService).createScreeningFromIds(movieName, roomName, startingAt);

        // When
        final String actual = underTest.createScreening(movieName, roomName, startingAt);

        // Then
        verify(screeningService).createScreeningFromIds(movieName, roomName, startingAt);
        assertEquals(expected, actual);
    }

    @Test
    void testCreateScreeningShouldCallServiceAndReturnSuccessMessage() {
        // Given
        final String expected = "Screening to 'Movie' in Room at 2000-20-13 12:30 successfully created.";
        doNothing().when(screeningService).createScreeningFromIds(movieName, roomName, startingAt);

        // When
        final String actual = underTest.createScreening(movieName, roomName, startingAt);

        // Then
        verify(screeningService).createScreeningFromIds(movieName, roomName, startingAt);
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteScreeningShouldReturnErrorMessageWhenExceptionOccurs() {
        // Given
        final String expected = "Error";
        given(screeningService.constructScreeningIdFromIds(movieName, roomName, startingAt)).willReturn(new ScreeningId());
        doThrow(new NoSuchItemException(expected)).when(screeningService).deleteScreening(any(ScreeningId.class));

        // When
        final String actual = underTest.deleteScreening(movieName, roomName, startingAt);

        // Then
        verify(screeningService).deleteScreening(any(ScreeningId.class));
        assertEquals(expected, actual);
    }

    @Test
    void testDeleteScreeningShouldCallServiceReturnSuccessMessage() {
        // Given
        final String expected = "Screening to 'Movie' in Room at 2000-20-13 12:30 successfully deleted.";
        given(screeningService.constructScreeningIdFromIds(movieName, roomName, startingAt)).willReturn(new ScreeningId());
        doNothing().when(screeningService).deleteScreening(any(ScreeningId.class));

        // When
        final String actual = underTest.deleteScreening(movieName, roomName, startingAt);

        // Then
        verify(screeningService).deleteScreening(any(ScreeningId.class));
        assertEquals(expected, actual);
    }

    @Test
    void testListScreeningsShouldReturnStringWhenNoScreeningFound() {
        // Given
        final String expected = "There are no screenings";
        given(screeningService.getAllScreenings()).willReturn(List.of());

        // When
        final String actual = underTest.listScreenings();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testListScreeningsShouldReturnFormattedStringWhenScreeningsFound() {
        // Given
        final String expected = "Formatted message";
        given(screeningService.getAllScreenings()).willReturn(List.of(new Screening(new ScreeningId())));
        given(screeningService.formattedScreeningList(anyList())).willReturn(expected);

        // When
        final String actual = underTest.listScreenings();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testCheckAdminAvailabilityShouldReturnUnavailableWhenNoOneIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("this command requires admin privileges.");
        given(accountService.getLoggedInUser()).willReturn(Optional.empty());

        // When
        final Availability actual = underTest.checkAdminAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }


    @Test
    void testCheckAdminAvailabilityShouldReturnUnavailableWhenDefaultUserIsSignedIn() {
        // Given
        final Availability expected = Availability.unavailable("this command requires admin privileges.");
        given(accountService.getLoggedInUser()).willReturn(Optional.of(USER));

        // When
        final Availability actual = underTest.checkAdminAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }

    @Test
    void testCheckAdminAvailabilityShouldReturnAvailableWhenAdminIsSignedIn() {
        // Given
        final Availability expected = Availability.available();
        given(accountService.getLoggedInUser()).willReturn(Optional.of(ADMIN));

        // When
        final Availability actual = underTest.checkAdminAvailability();

        // Then
        assertEquals(expected.isAvailable(), actual.isAvailable());
    }
}
