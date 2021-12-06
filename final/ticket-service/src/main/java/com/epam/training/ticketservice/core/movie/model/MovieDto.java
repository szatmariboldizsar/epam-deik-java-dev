package com.epam.training.ticketservice.core.movie.model;

import java.util.Objects;

public class MovieDto {

    private final String title;
    private final String genre;
    private final int length;

    public MovieDto(String title, String genre, int length) {
        this.title = title;
        this.genre = genre;
        this.length = length;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieDto movieDto = (MovieDto) o;
        return length == movieDto.length && title.equals(movieDto.title) && Objects.equals(genre, movieDto.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre, length);
    }

    @Override
    public String toString() {
        return "MovieDto{" +
                "title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", length=" + length +
                '}';
    }

    public static class Builder {
        private String title;
        private String genre;
        private int length;

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withGenre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder withLength(int length) {
            this.length = length;
            return this;
        }

        public MovieDto build() {
            return new MovieDto(title, genre, length);
        }
    }
}