package com.freshnco.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "details")
public class Details {
    @Id
    @GeneratedValue
    private Long id;


    @OneToMany(mappedBy = "details", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Room> roomList;

    

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String type;
    private String location;
    private String price;

    public Details() {
    }

   
    

    public Details(List<Room> roomList, User user, String type, String location, String price) {
        this.roomList = roomList;
        this.user = user;
        this.type = type;
        this.location = location;
        this.price = price;
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
}
