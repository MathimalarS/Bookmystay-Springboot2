package com.freshnco.backend.controller;

import com.freshnco.backend.model.User;
import com.freshnco.backend.model.Details;
import com.freshnco.backend.model.Room;
import com.freshnco.backend.repository.Userrepo;
import com.freshnco.backend.service.RoomService;
import com.freshnco.backend.repository.DetailsRepo;
import com.freshnco.backend.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

@RestController
// @CrossOrigin("http://localhost:8080")
public class UserController {

    @Autowired
    private Userrepo userrepo;

    @Autowired
    private DetailsRepo detailsRepo;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService  roomService;

    @PostMapping("/user")
    User newUser(@RequestBody User newUser) {
        User savedUser = userrepo.save(newUser);

        // Get details from the saved user
        Details details = newUser.getDetails();

        // Set the user for the details
        details.setUser(savedUser);

        // Save details
        detailsRepo.save(details);

        // Return the saved user
        return savedUser;

    }

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Sort sortObj = Sort.by("fname").ascending()
                .and(Sort.by("lname").descending());

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<User> users = userrepo.findAll(pageable);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userrepo.findById(id);
        return userOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usertype/{type}")
    public ResponseEntity<List<User>> getUsersByType(@PathVariable String type) {
        List<User> users = userrepo.findByType(type);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }
    @GetMapping("/userdetails/{id}")
    public ResponseEntity<Details> getUserDetails(@PathVariable Long id) {
        Optional<User> userOptional = userrepo.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Details details = user.getDetails();
            if (details != null) {
                return ResponseEntity.ok(details);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/user/{id}")
    ResponseEntity<User> updateUser(@RequestBody User newUser, @PathVariable Long id) {
        Optional<User> userOptional = userrepo.findById(id);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            existingUser.setFname(newUser.getFname());
            existingUser.setLname(newUser.getLname());
            existingUser.setEmail(newUser.getEmail());
            existingUser.setPassword(newUser.getPassword());

            // Update user details
            Details existingDetails = existingUser.getDetails();
            if (existingDetails == null) {
                // Create new details if it doesn't exist
                Details newDetails = newUser.getDetails();
                newDetails.setUser(existingUser);
                existingUser.setDetails(newDetails);
            } else {
                // Update existing details
                Details newDetails = newUser.getDetails();
                existingDetails.setType(newDetails.getType());
                existingDetails.setLocation(newDetails.getLocation());
                existingDetails.setPrice(newDetails.getPrice());
            }

            User updatedUser = userrepo.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/user/{id}")
    ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userrepo.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        Details details = user.getDetails();
        if (details != null) {
            // Delete associated details
            detailsRepo.delete(details);
        }

        // Delete user
        userrepo.deleteById(id);

        return ResponseEntity.ok("User with id " + id + " has been deleted successfully.");
    }
    @PostMapping("/room")
    Room newRoom(@RequestBody Room newRoom) {
        return roomRepository.save(newRoom);
    }

    @GetMapping("/rooms")
    public ResponseEntity<Page<Room>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Sort sortObj = Sort.by("checkin").ascending()
                .and(Sort.by("checkout").descending());

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<Room> rooms = roomRepository.findAll(pageable);

        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/room/{id}")
    ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        return roomOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/room/{id}")
    ResponseEntity<Room> updateRoom(@RequestBody Room newRoom, @PathVariable Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (roomOptional.isPresent()) {
            Room existingRoom = roomOptional.get();
            existingRoom.setCheckin(newRoom.getCheckin());
            existingRoom.setCheckout(newRoom.getCheckout());
            existingRoom.setFoodorder(newRoom.getFoodorder());

            Room updatedRoom = roomRepository.save(existingRoom);
            return ResponseEntity.ok(updatedRoom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/room/{id}")
    ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (!roomOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        roomRepository.deleteById(id);

        return ResponseEntity.ok("Room with id " + id + " has been deleted successfully.");
    }

    @PostMapping("/room/{detailsId}/create")
    public ResponseEntity<Room> createOrUpdateOrder(@PathVariable Long detailsId, @RequestBody Room room) throws NotFoundException {
        Details details = roomService.getUserById(detailsId);
        if (details == null) {
            // Handle the case where the user does not exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    
        // Set the user for the order
        room.setDetails(details);
    
        Room savedOrder = roomService.createOrUpdateOrder(room);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }
    
}
