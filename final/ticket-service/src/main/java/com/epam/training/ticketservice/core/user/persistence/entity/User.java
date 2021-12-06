package com.epam.training.ticketservice.core.user.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    private String username;

    @Column(nullable = false)
    private String password;

    private Boolean admin = false;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
