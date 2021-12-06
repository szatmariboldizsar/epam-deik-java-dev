package com.epam.training.ticketservice.core.screening.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;

import javax.annotation.Resource;
import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    @Resource(name = "Movie")
    private transient Movie movie;
    @Resource(name = "Room")
    private transient Room room;
    private Date date;

    public Screening(Movie movie, Room room, Date date) {
        this.movie = movie;
        this.room = room;
        this.date = date; //new SimpleDateFormat("YYYY-MM-DD hh:mm").parse(date);;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Screening{" +
                "movie=" + movie +
                ", room='" + room + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Screening screening = (Screening) o;
        return id.equals(screening.id) && movie.equals(screening.movie) && room.equals(screening.room) && date.equals(screening.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movie, room, date);
    }
}
