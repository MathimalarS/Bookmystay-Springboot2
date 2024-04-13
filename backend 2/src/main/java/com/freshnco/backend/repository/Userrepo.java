package com.freshnco.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.freshnco.backend.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Userrepo extends JpaRepository<User,Long> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.details d WHERE d.location = ?1")
    List<User> findByLocation(String location);

    Page<User> findAll(Pageable pageable);

    // Assuming 'type' belongs to the Details entity
    @Query("SELECT u FROM User u JOIN u.details d WHERE d.type = ?1")
    List<User> findByType(String type);
}
