package com.hotel.model;

import java.io.Serializable;

/**
 * Room model - demonstrates:
 * Week 1: Encapsulation (private fields, getters/setters), Constructor overloading
 * Week 6: Serializable for file persistence
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private boolean isAvailable;
    private String guestName;

    // Constructor overloading (Week 1)
    public Room(int roomNumber, String roomType, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
        this.guestName = null;
    }

    public Room(int roomNumber, String roomType, double pricePerNight,
                boolean isAvailable, String guestName) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = isAvailable;
        this.guestName = guestName;
    }

    // Getters and Setters (Encapsulation - Week 1)
    public int getRoomNumber()          { return roomNumber; }
    public void setRoomNumber(int n)    { this.roomNumber = n; }

    public String getRoomType()         { return roomType; }
    public void setRoomType(String t)   { this.roomType = t; }

    public double getPricePerNight()    { return pricePerNight; }
    public void setPricePerNight(double p) {
        if (p > 0) this.pricePerNight = p;
        else throw new IllegalArgumentException("Price must be positive.");
    }

    public boolean isAvailable()            { return isAvailable; }
    public void setAvailable(boolean a)     { this.isAvailable = a; }

    public String getGuestName()            { return guestName; }
    public void setGuestName(String name)   { this.guestName = name; }

    public double calculateTotalCost(int nights) { return pricePerNight * nights; }

    @Override
    public String toString() {
        return "Room[" + roomNumber + " | " + roomType + " | ₹" + pricePerNight +
               " | " + (isAvailable ? "Available" : "Occupied by " + guestName) + "]";
    }
}
