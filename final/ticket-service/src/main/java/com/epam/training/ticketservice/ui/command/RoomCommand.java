package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.ui.exception.NoSuchItemException;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.List;

@ShellComponent
public class RoomCommand {

    private final RoomService roomService;
    private final UserService userService;

    public RoomCommand(final RoomService roomService, final UserService userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @ShellMethod(value = "Add a room to database", key = {"create room", "cr"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String createRoom(final String name, final Integer rowCount, final Integer columnCount) {
        this.roomService.createRoom(new Room(name, rowCount, columnCount));
        return String.format("Room with name '%s' successfully created.", name);
    }

    @ShellMethod(value = "Update a room in the database", key = {"update room", "ur"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String updateRoom(final String name, final Integer rowCount, final Integer columnCount) {
        try {
            this.roomService.updateRoom(new Room(name, rowCount, columnCount));
            return String.format("Room with name '%s' successfully updated.", name);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete a room from the database", key = {"delete room", "dr"})
    @ShellMethodAvailability(value = "checkAdminAvailability")
    public String deleteRoom(final String name) {
        try {
            this.roomService.deleteRoom(name);
            return String.format("Room with name '%s' successfully deleted.", name);
        } catch (NoSuchItemException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "List the rooms", key = {"list rooms", "lr"})
    public String listRooms() {
        final List<Room> rooms = this.roomService.getAllRooms();

        if (!rooms.isEmpty()) {
            return roomService.formattedRoomList(rooms);
        }
        return "There are no rooms at the moment";
    }

    public Availability checkAdminAvailability() {
        return this.userService.getLoggedInUser().filter(User::getAdmin).isPresent()
                ? Availability.available()
                : Availability.unavailable("this command requires admin privileges.");
    }
}
