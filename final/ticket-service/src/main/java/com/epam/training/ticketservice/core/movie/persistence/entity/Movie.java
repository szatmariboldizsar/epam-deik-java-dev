package com.epam.training.ticketservice.core.movie.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Movie {

    @Id
    private String name;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private Integer length;

    @Override
    public String toString() {
        return String.format("%s (%s, %s minutes)",
                name, genre, length);
    }
}
