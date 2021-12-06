package com.epam.training.ticketservice.core.room.persistence.entity;

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
public class Room {

    @Id
    private String name;

    @Column(nullable = false)
    private Integer rowCount;

    @Column(nullable = false)
    private Integer columnCount;

    @Override
    public String toString() {
        return String.format("Room %s with %s seats, %s rows and %s columns",
                name, rowCount * columnCount, rowCount, columnCount);
    }
}
