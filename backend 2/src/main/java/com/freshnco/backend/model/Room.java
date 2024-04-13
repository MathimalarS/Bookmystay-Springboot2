
package com.freshnco.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue
    private Long id;

    private String checkin;
    private String checkout;
    private String foodorder;

  
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_id")
    @JsonIgnore
    private Details details;

    public Room() {
    }

    public Room(Long id, String checkin, String checkout, String foodorder) {
        this.id = id;
        this.checkin = checkin;
        this.checkout = checkout;
        this.foodorder = foodorder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getFoodorder() {
        return foodorder;
    }

    public void setFoodorder(String foodorder) {
        this.foodorder = foodorder;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    
}
