package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;
import java.util.Optional;

public class RoomCommand {
    private final RoomService roomService;
    private final UserService userService;

    public RoomCommand(RoomService roomService, UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "admin create room", value = "Create new room")
    public RoomDto createRoom(String name, int rows, int columns) {
        RoomDto room = RoomDto.builder()
                .withName(name)
                .withRows(rows)
                .withColumns(columns).build();
        roomService.createRoom(room);
        return room;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "admin update room", value = "Update existing room")
    public RoomDto updateRoom(String name, int rows, int columns) {
        RoomDto room = RoomDto.builder()
                .withName(name)
                .withRows(rows)
                .withColumns(columns).build();
        roomService.updateRoom(room);
        return room;
    }

    @ShellMethodAvailability("isAvailable")
    @ShellMethod(key = "admin delete room", value = "Delete existing room")
    public void deleteRoom(String name) {
        RoomDto room = RoomDto.builder()
                .withName(name).build();
        roomService.deleteRoom(room);
    }

    @ShellMethod(key = "list rooms", value = "List rooms")
    public String listMovies() {
        List<RoomDto> roomDtoList = roomService.listRooms();
        if (!roomDtoList.isEmpty()) {
            return roomDtoList.toString();
        }
        return "There are no rooms at the moment";
    }

    private Availability isAvailable() {
        Optional<UserDto> user = userService.describeAccount();
        if (user.isPresent() && user.get().getRole() == User.Role.ADMIN) {
            return Availability.available();
        }
        return Availability.unavailable("You are not an admin!");
    }
}
