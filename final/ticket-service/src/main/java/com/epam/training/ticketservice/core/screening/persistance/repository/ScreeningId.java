package com.epam.training.ticketservice.core.screening.persistance.repository;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Embeddable
public class ScreeningId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "movie_fk")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "room_fk")
    private Room room;

    @Column(name = "starting_at")
    private LocalDateTime startingAt;
}
