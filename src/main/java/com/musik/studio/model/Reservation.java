/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.musik.studio.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "studio_id")
    private Studio studio;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalPrice;
    private String status; // "PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"
    private LocalDateTime bookingDate;
    private String notes;
    
    public Reservation() {}
    
    public Reservation(Studio studio, Customer customer, LocalDateTime startTime, LocalDateTime endTime) {
        this.studio = studio;
        this.customer = customer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookingDate = LocalDateTime.now();
        this.status = "PENDING";
        calculateTotalPrice();
    }
    
    private void calculateTotalPrice() {
        if (studio != null && startTime != null && endTime != null) {
            long hours = java.time.Duration.between(startTime, endTime).toHours();
            this.totalPrice = studio.getPricePerHour().multiply(BigDecimal.valueOf(hours));
        }
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Studio getStudio() { return studio; }
    public void setStudio(Studio studio) { 
        this.studio = studio; 
        calculateTotalPrice();
    }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { 
        this.startTime = startTime; 
        calculateTotalPrice();
    }
    
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { 
        this.endTime = endTime; 
        calculateTotalPrice();
    }
    
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
