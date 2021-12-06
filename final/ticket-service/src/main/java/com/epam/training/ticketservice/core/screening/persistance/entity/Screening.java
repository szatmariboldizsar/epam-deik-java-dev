package com.epam.training.ticketservice.core.screening.persistance.entity;

import com.epam.training.ticketservice.core.screening.persistance.repository.ScreeningId;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@Entity
public class Screening implements Serializable {

    @EmbeddedId
    private ScreeningId id;

    public Screening(ScreeningId screeningId) {
        this.id = screeningId;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s minutes), screened in room %s, at %s",
                id.getMovie().getName(), id.getMovie().getGenre(), id.getMovie().getLength(), id.getRoom().getName(),
                id.getStartingAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}
