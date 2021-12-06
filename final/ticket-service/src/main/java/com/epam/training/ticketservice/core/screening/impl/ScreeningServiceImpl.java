package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;

    public ScreeningServiceImpl(ScreeningRepository screeningRepository, MovieRepository movieRepository, RoomRepository roomRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public Optional<ScreeningDto> getScreeningByMovieAndRoomAndDate(String movieTitle, String roomName, Date date) {
        return convertEntityToDto(screeningRepository.findByMovieAndRoomAndDate(movieRepository.findByTitle(movieTitle).get(), roomRepository.findByName(roomName).get(), date));
    }

    @Override
    public void createScreening(ScreeningDto screeningDto) throws ParseException {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovie(), "Screening movie cannot be null");
        Objects.requireNonNull(screeningDto.getRoom(), "Screening room cannot be null");
        Objects.requireNonNull(screeningDto.getDate(), "Screening date cannot be null");
        Screening screening = new Screening(screeningDto.getMovie(),
            screeningDto.getRoom(),
            screeningDto.getDate());
        screeningRepository.save(screening);
    }

    @Override
    public void updateScreening(ScreeningDto screeningDto) throws ParseException {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovie(), "Screening movie cannot be null");
        Objects.requireNonNull(screeningDto.getRoom(), "Screening room cannot be null");
        Objects.requireNonNull(screeningDto.getDate(), "Screening date cannot be null");
        Screening screening = new Screening(screeningDto.getMovie(),
                screeningDto.getRoom(),
                screeningDto.getDate());
        screeningRepository.save(screening);
    }

    @Override
    public void deleteScreening(ScreeningDto screeningDto) {
        Objects.requireNonNull(screeningDto, "Screening cannot be null");
        Objects.requireNonNull(screeningDto.getMovie(), "Screening movie cannot be null");
        Objects.requireNonNull(screeningDto.getRoom(), "Screening room cannot be null");
        Objects.requireNonNull(screeningDto.getDate(), "Screening date cannot be null");
        Screening screening = screeningRepository.findByMovieAndRoomAndDate(screeningDto.getMovie(), screeningDto.getRoom(), screeningDto.getDate()).get();
        screeningRepository.delete(screening);
    }

    @Override
    public List<ScreeningDto> listScreenings() {
        return screeningRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    private ScreeningDto convertEntityToDto(Screening screening) {
        return ScreeningDto.builder()
            .withMovie(screening.getMovie())
            .withRoom(screening.getRoom())
            .withDate(screening.getDate()).build();
    }

    private Optional<ScreeningDto> convertEntityToDto(Optional<Screening> product) {
        return product.isEmpty() ? Optional.empty() : Optional.of(convertEntityToDto(product.get()));
    }
}
