package com.epam.training.ticketservice.core.user.persistence.repository;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
