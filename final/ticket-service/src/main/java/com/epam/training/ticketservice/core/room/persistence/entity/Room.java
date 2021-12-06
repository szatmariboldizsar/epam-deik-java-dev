package com.epam.training.ticketservice.core.room.persistence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(unique = true)
    private String name;
    private int rows;
    private int columns;

    public Room(String name, int rows, int columns) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "Room{" +
            ", name='" + name + '\'' +
            ", rows=" + rows +
            ", columns='" + columns + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return rows == room.rows && columns == room.columns && id.equals(room.id) && name.equals(room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rows, columns);
    }
}
