package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;

import java.util.Date;
import java.util.Objects;

public class ScreeningDto {

    private final Movie movie;
    private final Room room;
    private final Date date;

    public ScreeningDto(Movie movie, Room room, Date date) {
        this.movie = movie;
        this.room = room;
        this.date = date;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Movie getMovie() {
        return movie;
    }

    public Room getRoom() {
        return room;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScreeningDto that = (ScreeningDto) o;
        return movie.equals(that.movie) && room.equals(that.room) && date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, room, date);
    }

    @Override
    public String toString() {
        return "ScreeningDto{" +
                "movie=" + movie +
                ", room='" + room + '\'' +
                ", date=" + date +
                '}';
    }

    public static class Builder {
        private Movie movie;
        private Room room;
        private Date date;
        private ScreeningRepository screeningRepository;

        public Builder withMovie(Movie movie) {
            this.movie = movie;
            return this;
        }

        public Builder withRoom(Room room) {
            this.room = room;
            return this;
        }

        public Builder withDate(Date date) {
            this.date = date;
            return this;
        }

        public ScreeningDto build() {
            return new ScreeningDto(movie, room, date);
        }
    }
}