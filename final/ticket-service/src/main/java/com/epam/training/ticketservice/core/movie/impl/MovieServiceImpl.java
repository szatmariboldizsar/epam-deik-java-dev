package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }



    @Override
    public Optional<MovieDto> getMovieByTitle(String movieTitle) {
        return convertEntityToDto(movieRepository.findByTitle(movieTitle));
    }

    @Override
    public void createMovie(MovieDto movieDto) {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie title cannot be null");
        Objects.requireNonNull(movieDto.getGenre(), "Movie genre cannot be null");
        Objects.requireNonNull(movieDto.getLength(), "Movie length cannot be null");
        Movie movie = new Movie(movieDto.getTitle(),
            movieDto.getGenre(),
            movieDto.getLength());
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(MovieDto movieDto) {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie title cannot be null");
        Objects.requireNonNull(movieDto.getGenre(), "Movie genre cannot be null");
        Objects.requireNonNull(movieDto.getLength(), "Movie length cannot be null");
        Movie movie = new Movie(movieDto.getTitle(),
                movieDto.getGenre(),
                movieDto.getLength());
        movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(MovieDto movieDto) {
        Objects.requireNonNull(movieDto, "Movie cannot be null");
        Objects.requireNonNull(movieDto.getTitle(), "Movie title cannot be null");
        Movie movie = movieRepository.findByTitle(movieDto.getTitle()).get();
        movieRepository.delete(movie);
    }

    @Override
    public List<MovieDto> listMovies() {
        return movieRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private MovieDto convertEntityToDto(Movie movie) {
        return MovieDto.builder()
            .withTitle(movie.getTitle())
            .withGenre(movie.getGenre())
            .withLength(movie.getLength()).build();
    }

    private Optional<MovieDto> convertEntityToDto(Optional<Movie> product) {
        return product.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(product.get()));
    }
}
