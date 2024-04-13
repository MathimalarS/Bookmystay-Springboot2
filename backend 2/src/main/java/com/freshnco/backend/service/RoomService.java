package com.freshnco.backend.service;

import com.freshnco.backend.model.Details;
import com.freshnco.backend.model.Room;
import com.freshnco.backend.repository.DetailsRepo;
import com.freshnco.backend.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private final RoomRepository roomRepository;

    @Autowired
    private final DetailsRepo detailsRepo;

    public RoomService(RoomRepository roomRepository,DetailsRepo detailsRepo) {
        this.roomRepository = roomRepository;
        this.detailsRepo=detailsRepo;
    }

    // Save a room
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // Get room by id
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    // Update room
    public Room updateRoom(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setCheckin(roomDetails.getCheckin());
        room.setCheckout(roomDetails.getCheckout());
        room.setFoodorder(roomDetails.getFoodorder());

        return roomRepository.save(room);
    }

    // Delete room
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

public Details getUserById(Long id) throws NotFoundException {
    Optional<Details> userOptional = detailsRepo.findById(id);
    if (userOptional.isPresent()) {
        return userOptional.get();
    } else {
        // Handle the case when the user with the given ID is not found
        throw new NotFoundException();
    }
}


    public Room createOrUpdateOrder(Room room) {
       return roomRepository.save(room);
    }
}
