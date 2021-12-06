package com.epam.training.ticketservice.core.room.model;

import java.util.Objects;

public class RoomDto {

    private final String name;
    private final int rows;
    private final int columns;

    public RoomDto(String name, int rows, int columns) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDto roomDto = (RoomDto) o;
        return rows == roomDto.rows && columns == roomDto.columns && name.equals(roomDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, rows, columns);
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "title='" + name + '\'' +
                ", rows=" + rows +
                ", columns=" + columns +
                '}';
    }

    public static class Builder {
        private String name;
        private int rows;
        private int columns;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withRows(int rows) {
            this.rows = rows;
            return this;
        }

        public Builder withColumns(int columns) {
            this.columns = columns;
            return this;
        }

        public RoomDto build() {
            return new RoomDto(name, rows, columns);
        }
    }
}