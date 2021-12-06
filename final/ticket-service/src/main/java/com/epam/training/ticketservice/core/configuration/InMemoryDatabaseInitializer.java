package com.epam.training.ticketservice.core.configuration;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class InMemoryDatabaseInitializer {

    private static final User ADMIN_USER = new User("admin", "admin", true);

    private final UserRepository userRepository;
    private final Environment environment;

    public InMemoryDatabaseInitializer(final UserRepository userRepository, final Environment environment) {
        this.userRepository = userRepository;
        this.environment = environment;
    }

    @PostConstruct
    public void initProducts() {
        if (this.isProfileCiActive() || userRepository.findById(ADMIN_USER.getUsername()).isEmpty()) {
            userRepository.save(ADMIN_USER);
        }
    }

    public boolean isProfileCiActive() {
        return Arrays.asList(this.environment.getActiveProfiles()).contains("ci");
    }
}

/*@Component
public class InMemoryDatabaseInitializer {

    private final UserRepository userRepository;

    public InMemoryDatabaseInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        User admin = new User("admin", "admin", User.Role.ADMIN);
        userRepository.save(admin);
    }
}*/
