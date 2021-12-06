package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ScreeningCommand {
    private final ScreeningService screeningService;
    private final UserService userService;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public ScreeningCommand(ScreeningService screeningService, UserService userService, MovieRepository movieRepository, RoomRepository roomRepository) {
        this.screeningService = screeningService;
        this.userService = userService;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "admin create screening", value = "Create new screening")
    public ScreeningDto createScreening(String movieTitle, String roomName, Date date) throws ParseException {
        ScreeningDto screening = ScreeningDto.builder()
                .withMovie(movieRepository.findByTitle(movieTitle).get())
                .withRoom(roomRepository.findByName(roomName).get())
                .withDate(date).build();
        screeningService.createScreening(screening);
        return screening;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "admin update screening", value = "Update existing screening")
    public ScreeningDto updateScreening(String movieTitle, String roomName, Date date) throws ParseException {
        ScreeningDto screening = ScreeningDto.builder()
                .withMovie(movieRepository.findByTitle(movieTitle).get())
                .withRoom(roomRepository.findByName(roomName).get())
                .withDate(date).build();
        screeningService.updateScreening(screening);
        return screening;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "admin delete screening", value = "Delete existing screening")
    public void deleteScreening(String movieTitle, String roomName, Date date) {
        ScreeningDto screening = ScreeningDto.builder()
                .withMovie(movieRepository.findByTitle(movieTitle).get()).build();
        screeningService.deleteScreening(screening);
    }

    @ShellMethod(key = "list screening", value = "List screening")
    public String listMovies() {
        List<ScreeningDto> screeningDtoList = screeningService.listScreenings();
        if (!screeningDtoList.isEmpty()) {
            return screeningDtoList.toString();
        }
        return "There are no screenings";
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describeAccount();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }
}
