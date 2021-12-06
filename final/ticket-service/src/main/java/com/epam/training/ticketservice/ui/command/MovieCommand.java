package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.model.MovieDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

public class MovieCommand {
    private final MovieService movieService;
    private final UserService userService;

    public MovieCommand(MovieService movieService, UserService userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "admin create movie", value = "Create new movie")
    public MovieDto createMovie(String title, String genre, int length) {
        MovieDto movie = MovieDto.builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length).build();
        movieService.createMovie(movie);
        return movie;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "admin update movie", value = "Update existing movie")
    public MovieDto updateMovie(String title, String genre, int length) {
        MovieDto movie = MovieDto.builder()
                .withTitle(title)
                .withGenre(genre)
                .withLength(length).build();
        movieService.updateMovie(movie);
        return movie;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "admin delete movie", value = "Delete existing movie")
    public void deleteMovie(String title) {
        MovieDto movie = MovieDto.builder()
                .withTitle(title).build();
        movieService.deleteMovie(movie);
    }

    @ShellMethod(key = "list movies", value = "List movies")
    public String listMovies() {
        List<MovieDto> movieDtoList = movieService.listMovies();
        if (!movieDtoList.isEmpty()) {
            return movieDtoList.toString();
        }
        return "There are no movies at the moment";
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describeAccount();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }
}
