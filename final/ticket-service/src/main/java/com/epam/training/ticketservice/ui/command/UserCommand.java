package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.model.UserDto;
//import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Optional;

@ShellComponent
public class UserCommand {

    private final UserService userService;

    public UserCommand(UserService userService) {
        this.userService = userService;
    }

    @ShellMethod(key = "user login", value = "User login")
    public String signInPriviliged(String username, String password) {
        Optional<UserDto> user = userService.signInPriviliged(username, password);
        if (user.isEmpty()) { //|| user.get().getRole() == User.Role.ADMIN
            return "Login failed due to incorrect credentials";
        }
        return user.get() + " is logged in!";
    }

    @ShellMethod(key = "user logout", value = "User logout")
    public String signOut() {
        Optional<UserDto> user = userService.signOut();
        if (user.isEmpty()) {
            return "You are not signed in";
        }
        return user.get() + " is logged out!";
    }

    @ShellMethod(key = "user print", value = "Get user information")
    public String describeAccount() {
        Optional<UserDto> userDto = userService.describeAccount();
        if (userDto.isEmpty()) {
            return "You are not signed in";
        }
        return "Signed in with privileged account " + userDto.get().getUsername();
    }

    @ShellMethod(key = "user register", value = "User registration")
    public String registerUser(String userName, String password) {
        try {
            userService.registerUser(userName, password);
            return "Registration was successful!";
        } catch (Exception e) {
            return "Registration failed!";
        }
    }
}
