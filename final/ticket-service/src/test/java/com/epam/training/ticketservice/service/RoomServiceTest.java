package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.ui.exception.NoSuchItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class RoomServiceTest {

    private final static Room room1 = new Room("Room1", 12, 16);
    private final static Room room2 = new Room("Room2", 10, 20);

    private RoomService underTest;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new RoomService(roomRepository);
    }

    @Test
    void testGetRoomByIdShouldCallRepositoryFindById() {
        // Given + When
        underTest.getRoomById(room1.getName());

        // Then
        verify(roomRepository).findById(room1.getName());
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testGetRoomAllRoomsShouldCallRepositoryFindAll() {
        // Given + When
        underTest.getAllRooms();

        // Then
        verify(roomRepository).findAll();
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testCreateRoomShouldCallRepositorySave() {
        // Given + When
        underTest.createRoom(room1);

        // Then
        verify(roomRepository).save(room1);
        verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testUpdateRoomShouldTrowExceptionWhenIdIsNotFound() {
        // Given
        given(roomRepository.findById(room1.getName())).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> underTest.updateRoom(room1));
    }

    @Test
    void testUpdateRoomShouldCallSaveInRepositoryWhenIdIsFound() {
        // Given
        given(roomRepository.findById(room1.getName())).willReturn(Optional.of(room1));

        // When
        underTest.updateRoom(room1);

        // Then
        verify(roomRepository).save(room1);
    }

    @Test
    void testDeleteRoomShouldTrowExceptionWhenIdIsNotFound() {
        // Given
        final String roomName = room1.getName();
        given(roomRepository.findById(roomName)).willReturn(Optional.empty());

        // When + Then
        assertThrows(NoSuchItemException.class, () -> underTest.deleteRoom(roomName));
    }

    @Test
    void testDeleteRoomShouldCallDeleteByIdInRepositoryWhenIdIsFound() {
        // Given
        final String roomName = room1.getName();
        given(roomRepository.findById(roomName)).willReturn(Optional.of(room1));

        // When
        underTest.deleteRoom(roomName);

        // Then
        verify(roomRepository).deleteById(roomName);
    }

    @Test
    void testFormattedRoomListShouldReturnExpectedWhenRooms() {
        // Given
        final String expected = "Room Room1 with 192 seats, 12 rows and 16 columns\n" +
                "Room Room2 with 200 seats, 10 rows and 20 columns";

        // When
        final String actual = underTest.formattedRoomList(List.of(room1, room2));

        // Then
        assertEquals(expected, actual);
    }
}
