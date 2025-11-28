/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "studios")
public class Studio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String facilities;
    private BigDecimal pricePerHour;
    private Integer capacity;
    private boolean active = true;
    
    @OneToMany(mappedBy = "studio", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
    
    // Default constructor
    public Studio() {}
    
    // Parameterized constructor
    public Studio(String name, String facilities, BigDecimal pricePerHour, Integer capacity) {
        this.name = name;
        this.facilities = facilities;
        this.pricePerHour = pricePerHour;
        this.capacity = capacity;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getFacilities() { return facilities; }
    public void setFacilities(String facilities) { this.facilities = facilities; }
    
    public BigDecimal getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(BigDecimal pricePerHour) { this.pricePerHour = pricePerHour; }
    
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
    
    // Di dalam class Studio.java, tambahkan:
    @Override
    public String toString() {
        return name + " - " + facilities + " (Rp " + pricePerHour + "/jam)";
    }
}
