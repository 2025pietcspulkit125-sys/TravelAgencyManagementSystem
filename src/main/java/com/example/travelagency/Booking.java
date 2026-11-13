package com.example.travelagency;

import java.math.BigDecimal;

/** A reservation connecting one customer to one trip. */
public class Booking {

    private int id;
    private int customerId;
    private int tripId;
    private Integer bookedByUserId;
    private int passengerCount;
    private String status;
    private BigDecimal totalAmount;
    private String notes;

    public Booking(
            int id,
            int customerId,
            int tripId,
            Integer bookedByUserId,
            int passengerCount,
            String status,
            BigDecimal totalAmount,
            String notes) {
        this.id = id;
        this.customerId = customerId;
        this.tripId = tripId;
        this.bookedByUserId = bookedByUserId;
        this.passengerCount = passengerCount;
        this.status = status;
        this.totalAmount = totalAmount;
        this.notes = notes;
    }

    public Booking(
            int customerId,
            int tripId,
            int passengerCount,
            String status,
            BigDecimal totalAmount,
            String notes) {
        this(0, customerId, tripId, null, passengerCount, status, totalAmount, notes);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getTripId() {
        return tripId;
    }

    public Integer getBookedByUserId() {
        return bookedByUserId;
    }

    public int getPassengerCount() {
        return passengerCount;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public void setBookedByUserId(Integer bookedByUserId) {
        this.bookedByUserId = bookedByUserId;
    }

    public void setPassengerCount(int passengerCount) {
        this.passengerCount = passengerCount;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
