package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.user.persistence.entity.User;

import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private Optional<User> loggedInUser = Optional.empty();

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signIn(User user) {
        this.setLoggedInUser(Optional.of(user));
    }

    public void signOut() {
        this.setLoggedInUser(Optional.empty());
    }

    public Optional<User> getLoggedInUser() {
        return loggedInUser;
    }

    public Optional<User> getAccountById(final String username) {
        return this.userRepository.findById(username);
    }

    public String formattedAccountDescription(final User user) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.format("Signed in with%s account '%s'\n",
                user.getAdmin() ? " privileged" : "",
                user.getUsername()));
        if (user.getAdmin()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        }
        return stringBuilder.toString();
    }

    private void setLoggedInUser(final Optional<User> account) {
        this.loggedInUser = account;
    }
}
