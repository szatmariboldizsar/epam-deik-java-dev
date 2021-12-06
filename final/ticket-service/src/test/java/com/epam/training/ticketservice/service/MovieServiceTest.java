package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.ui.exception.NoSuchItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class MovieServiceTest {

    private final static Movie movie1 = new Movie("Movie1", "drama", 90);
    private final static Movie movie2 = new Movie("Movie2", "sci-fi", 120);

    private MovieService underTest;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new MovieService(movieRepository);
    }

    @Test
    void testGetMovieByIdShouldCallRepositoryFindById() {
        // Given + When
        underTest.getMovieById(movie1.getName());

        // Then
        verify(movieRepository).findById(movie1.getName());
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void testGetMovieAllMoviesShouldCallRepositoryFindAll() {
        // Given + When
        underTest.getAllMovies();

        // Then
        verify(movieRepository).findAll();
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void testCreateMovieShouldCallRepositorySave() {
        // Given + When
        underTest.createMovie(movie1);

        // Then
        verify(movieRepository).save(movie1);
        verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void testUpdateMovieShouldTrowExceptionWhenIdIsNotFound() {
        // Given
        given(movieRepository.findById(movie1.getName())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> underTest.updateMovie(movie1));
    }

    @Test
    void testUpdateMovieShouldCallSaveInRepositoryWhenIdIsFound() {
        // Given
        given(movieRepository.findById(movie1.getName())).willReturn(Optional.of(movie1));

        // When
        underTest.updateMovie(movie1);

        // Then
        verify(movieRepository).save(movie1);
    }

    @Test
    void testDeleteMovieShouldTrowExceptionWhenIdIsNotFound() {
        // Given
        final String movieName = movie1.getName();
        given(movieRepository.findById(movieName)).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> underTest.deleteMovie(movieName));
    }

    @Test
    void testDeleteMovieShouldCallDeleteByIdInRepositoryWhenIdIsFound() {
        // Given
        final String movieName = movie1.getName();
        given(movieRepository.findById(movieName)).willReturn(Optional.of(movie1));

        // When
        underTest.deleteMovie(movieName);

        // Then
        verify(movieRepository).deleteById(movieName);
    }

    @Test
    void testFormattedMovieListShouldReturnExpectedWhenMovies() {
        // Given
        final String expected = "Movie1 (drama, 90 minutes)\n" +
                "Movie2 (sci-fi, 120 minutes)";

        // When
        final String actual = underTest.formattedMovieList(List.of(movie1, movie2));

        // Then
        assertEquals(expected, actual);
    }
}
