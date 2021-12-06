package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.model.MovieDto;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Optional<MovieDto> getMovieByTitle(String movieTitle);

    void createMovie(MovieDto movieDto);

    void updateMovie(MovieDto movieDto);

    void deleteMovie(MovieDto movieDto);

    List<MovieDto> listMovies();
}
