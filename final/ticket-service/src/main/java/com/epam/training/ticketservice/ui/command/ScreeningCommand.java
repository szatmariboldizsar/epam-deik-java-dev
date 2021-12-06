package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.screening.persistance.entity.Screening;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.ui.exception.NoSuchItemException;
import com.epam.training.ticketservice.ui.exception.ScreeningOverlapException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class ScreeningCommand {

    private final ScreeningService screeningService;
    private final UserService userService;

    public ScreeningCommand(final ScreeningService screeningService, final UserService userService) {
        this.screeningService = screeningService;
        this.userService = userService;
    }

    @ShellMethod(value = "Add a screening to database", key = {"create screening", "cs"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createScreening(final String movieName, final String roomName, final String startingAt) {
        try {
            this.screeningService.createScreeningFromIds(movieName, roomName, startingAt);
            return String.format("Screening to '%s' in %s at %s successfully created.",
                    movieName, roomName, startingAt);
        } catch (NoSuchItemException | ScreeningOverlapException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete a screening from the database", key = {"delete screening", "ds"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String deleteScreening(final String movieName, final String roomName, final String startingAt) {
        try {
            this.screeningService.deleteScreening(this.screeningService.constructScreeningIdFromIds(
                    movieName,
                    roomName,
                    startingAt));
            return String.format("Screening to '%s' in %s at %s successfully deleted.",
                    movieName, roomName, startingAt);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "List the screenings", key = {"list screenings", "ls"})
    public String listScreenings() {
        final List<Screening> screenings = this.screeningService.getAllScreenings();

        if (!screenings.isEmpty()) {
            return screeningService.formattedScreeningList(screenings);
        }
        return "There are no screenings";
    }

    public Availability checkAdminAvailability() {
        return this.userService.getLoggedInUser().filter(User::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
