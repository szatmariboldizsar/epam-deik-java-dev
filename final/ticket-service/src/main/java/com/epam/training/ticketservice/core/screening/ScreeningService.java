package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.movie.model.MovieDto;

import com.epam.training.ticketservice.core.room.model.RoomDto;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ScreeningService {

    Optional<ScreeningDto> getScreeningByMovieAndRoomAndDate(String movieTitle, String roomName, Date date);

    void createScreening(ScreeningDto screeningDto) throws ParseException;

    void updateScreening(ScreeningDto screeningDto) throws ParseException;

    void deleteScreening(ScreeningDto screeningDto);

    List<ScreeningDto> listScreenings();
}
