package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.screening.persistance.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistance.repository.ScreeningId;
import com.epam.training.ticketservice.core.screening.persistance.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.ui.exception.NoSuchItemException;
import com.epam.training.ticketservice.ui.exception.ScreeningOverlapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class ScreeningServiceTest {

    private final static int breakLength = 10;
    private final static String dateTimePattern = "yyyy-MM-dd HH:mm";

    private final static Room room1 = new Room("Room1", 10, 10);
    private final static Movie movie1 = new Movie("Movie1", "drama", 90);
    private final static Movie movie2 = new Movie("Movie2", "sci-fi", 120);
    private final static String startingAt1 = "2000-12-06 12:00";
    private final static String startingAt2 = "2000-12-06 11:55";
    private final static String startingAt3 = "2000-12-13 11:45";
    private final static LocalDateTime formattedStartingAt1 = LocalDateTime.parse(startingAt1, DateTimeFormatter.ofPattern(dateTimePattern));
    private final static LocalDateTime formattedStartingAt2 = LocalDateTime.parse(startingAt2, DateTimeFormatter.ofPattern(dateTimePattern));
    private final static LocalDateTime formattedStartingAt3 = LocalDateTime.parse(startingAt3, DateTimeFormatter.ofPattern(dateTimePattern));
    private final static Screening screening = new Screening(new ScreeningId(movie1, room1, formattedStartingAt1));
    private final static Screening overlappingScreening = new Screening(new ScreeningId(movie2, room1, formattedStartingAt2));
    private final static Screening breakOverlappingScreening = new Screening(new ScreeningId(movie2, room1, formattedStartingAt3));

    private ScreeningService underTest;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private MovieService movieService;

    @Mock
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new ScreeningService(screeningRepository, movieService, roomService, breakLength, dateTimePattern);
    }

    @Test
    void testGetAllScreeningsShouldCallRepositoryFindAll() {
        // Given + When
        underTest.getAllScreenings();

        // Then
        verify(screeningRepository).findAll();
        verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    void testCreateScreeningShouldCallRepositorySave() {
        // Given + When
        underTest.createScreening(screening);

        // Then
        verify(screeningRepository).save(screening);
        verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    void testCreateScreeningFromIdsShouldThrowNoSuchItemExceptionWhenMovieDoesNotExist() {
        // Given
        given(movieService.getMovieById(movie1.getName())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> underTest.createScreeningFromIds(movie1.getName(), room1.getName(), startingAt1));
    }

    @Test
    void testCreateScreeningFromIdsShouldThrowNoSuchItemExceptionWhenMovieExistsButRoomDoesNot() {
        // Given
        given(movieService.getMovieById(movie1.getName())).willReturn(Optional.of(movie1));
        given(roomService.getRoomById(room1.getName())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> underTest.createScreeningFromIds(movie1.getName(), room1.getName(), startingAt1));
    }

    @Test
    void testCreateScreeningFromIdsShouldThrowScreeningOverlapExceptionWhenValidScreeningIsOverlappingOtherScreening() {
        // Given
        given(movieService.getMovieById(movie1.getName())).willReturn(Optional.of(movie1));
        given(roomService.getRoomById(room1.getName())).willReturn(Optional.of(room1));
        ScreeningService screeningService = spy(underTest);
        doReturn(true).when(screeningService).isOverlappingScreening(room1.getName(), movie1.getLength(), formattedStartingAt1);

        // When + Then
        assertThrows(ScreeningOverlapException.class, () -> screeningService.createScreeningFromIds(movie1.getName(), room1.getName(), startingAt1));
    }

    @Test
    void testDeleteScreeningShouldTrowExceptionWhenIdIsNotFound() {
        // Given
        given(screeningRepository.findById(screening.getId())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> underTest.deleteScreening(screening.getId()));
    }

    @Test
    void testDeleteScreeningShouldCallDeleteByIdInRepositoryWhenIdIsFound() {
        // Given
        final ScreeningId screeningId = screening.getId();
        given(screeningRepository.findById(screeningId)).willReturn(Optional.of(screening));

        // When
        underTest.deleteScreening(screeningId);

        // Then
        verify(screeningRepository).deleteById(screeningId);
    }

    @Test
    void testFormattedScreeningListShouldReturnRequiredString() {
        // Given
        final String expected = "Movie2 (sci-fi, 120 minutes), screened in room Room1, at 2000-12-06 11:55\n" +
                "Movie1 (drama, 90 minutes), screened in room Room1, at 2000-12-06 12:00";

        // When
        final String actual = underTest.formattedScreeningList(new ArrayList<>(List.of(screening, overlappingScreening)));

        // Then
        assertEquals(expected, actual);
    }

    @Test
    void testConstructScreeningIdFromIdsShouldReturnNewScreeningIdWhenStartingAtIsLocalDateTime() {
        // Given
        given(movieService.getMovieById(movie1.getName())).willReturn(Optional.of(movie1));
        given(roomService.getRoomById(room1.getName())).willReturn(Optional.of(room1));

        // When
        final ScreeningId actual = underTest.constructScreeningIdFromIds(movie1.getName(), room1.getName(), formattedStartingAt1);

        // Then
        assertEquals(screening.getId().toString(), actual.toString());
    }

    @Test
    void testConstructScreeningIdFromIdsShouldReturnNewScreeningIdWhenStartingAtIsString() {
        // Given
        given(movieService.getMovieById(movie1.getName())).willReturn(Optional.of(movie1));
        given(roomService.getRoomById(room1.getName())).willReturn(Optional.of(room1));

        // When
        final ScreeningId actual = underTest.constructScreeningIdFromIds(movie1.getName(), room1.getName(), startingAt1);

        // Then
        assertEquals(screening.getId().toString(), actual.toString());
    }

    @Test
    void testIsOverlappingScreeningShouldReturnTrueWhenHasOverlappingScreening() {
        // Given
        given(movieService.getMovieById(movie1.getName())).willReturn(Optional.of(movie1));
        given(movieService.getMovieById(overlappingScreening.getId().getMovie().getName()))
                .willReturn(Optional.of(overlappingScreening.getId().getMovie()));
        given(screeningRepository.findScreeningsById_Room_Name(room1.getName())).willReturn(List.of(overlappingScreening));

        // When
        final boolean actual = underTest.isOverlappingScreening(room1.getName(), movie1.getLength(), formattedStartingAt1);

        // Then
        assertTrue(actual);
    }

    @Test
    void testIsOverlappingScreeningShouldReturnFalseWhenDoesNotHaveOverlappingScreening() {
        // Given
        given(movieService.getMovieById(movie1.getName())).willReturn(Optional.of(movie1));
        given(movieService.getMovieById(breakOverlappingScreening.getId().getMovie().getName()))
                .willReturn(Optional.of(breakOverlappingScreening.getId().getMovie()));
        given(screeningRepository.findScreeningsById_Room_Name(room1.getName())).willReturn(List.of(breakOverlappingScreening));

        // When
        final boolean actual = underTest.isOverlappingScreening(room1.getName(), movie1.getLength(), formattedStartingAt1);

        // Then
        assertFalse(actual);
    }
}
