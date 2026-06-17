package com.hotel.model;

/**
 * RoomType enum with constructor and methods.
 * Week 2: Enum with constructors and methods.
 */
public enum RoomType {
    STANDARD("Standard Room", 1500.0),
    DELUXE("Deluxe Room", 2800.0),
    SUITE("Luxury Suite", 5500.0);

    private final String displayName;
    private final double basePrice;

    RoomType(String displayName, double basePrice) {
        this.displayName = displayName;
        this.basePrice = basePrice;
    }

    public String getDisplayName() { return displayName; }
    public double getBasePrice()   { return basePrice; }

    public double calculateCost(int nights)        { return basePrice * nights; }
    public double calculateCostWithGST(int nights) { return calculateCost(nights) * 1.12; }

    @Override
    public String toString() { return displayName; }
}