package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.ui.exception.NoSuchItemException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class MovieCommand {

    private final MovieService movieService;
    private final UserService userService;

    public MovieCommand(final MovieService movieService, final UserService userService) {
        this.userService = userService;
        this.movieService = movieService;
    }

    @ShellMethod(value = "Add a movie to database", key = {"create movie", "cm"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createMovie(final String name, final String genre, final int length) {
        this.movieService.createMovie(new Movie(name, genre, length));
        return String.format("Movie with name '%s' successfully created.", name);
    }

    @ShellMethod(value = "Update a movie in the database", key = {"update movie", "um"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String updateMovie(final String name, final String genre, final int length) {
        try {
            this.movieService.updateMovie(new Movie(name, genre, length));
            return String.format("Movie with name '%s' successfully updated.", name);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete a movie from the database", key = {"delete movie", "dm"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String deleteMovie(final String name) {
        try {
            this.movieService.deleteMovie(name);
            return String.format("Movie with name '%s' successfully deleted.", name);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "List the movies", key = {"list movies", "lm"})
    public String listMovies() {
        final List<Movie> movies = this.movieService.getAllMovies();

        if (!movies.isEmpty()) {
            return movieService.formattedMovieList(movies);
        }
        return "There are no movies at the moment";
    }

    public Availability checkAdminAvailability() {
        return this.userService.getLoggedInUser().filter(User::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
