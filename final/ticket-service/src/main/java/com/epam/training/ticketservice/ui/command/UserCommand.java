package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.service.UserService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

import java.util.Optional;

@ShellComponent
public class UserCommand {

    private final UserService userService;

    public UserCommand(UserService userService) {
        this.userService = userService;
    }

    @ShellMethod(value = "Sign in with credentials to admin account", key = {"sign in privileged", "sip"})
    public String signInPrivileged(final String username, final String password) {
        final Optional<User> account = this.userService.getAccountById(username);
        if (account.filter(User::getAdmin).isPresent() && password.equals(account.get().getPassword())) {
            this.userService.signIn(account.get());
            return "Successfully signed in";
        } else if (account.filter(acc -> !acc.getAdmin()).isPresent()) {
            return String.format("'%s' is not a privileged user", username);
        }
        return "Login failed due to incorrect credentials";
    }

    @ShellMethod(value = "Sign out from account", key = {"sign out", "so"})
    @ShellMethodAvailability(value = "checkLoggedInAvailability")
    public String signOut() {
        this.userService.signOut();
        return "Successfully signed out";
    }

    @ShellMethod(value = "Query signed in account info", key = {"describe account", "da"})
    public String describeAccount() {
        final Optional<User> loggedInAccount = this.userService.getLoggedInUser();
        if (loggedInAccount.isPresent()) {
            return this.userService.formattedAccountDescription(loggedInAccount.get());
        }
        return "You are not signed in";
    }

    public Availability checkLoggedInAvailability() {
        return this.userService.getLoggedInUser().isPresent()
                ? Availability.available()
                : Availability.unavailable("you are not signed in.");
    }
}
