package com.hotel.model;

import java.io.Serializable;

/**
 * Booking model.
 * Week 6: Serializable for file persistence.
 * Includes bill generation for billing management feature.
 */
public class Booking implements Serializable {
    private static final long serialVersionUID = 2L;

    private static int counter = 1000;

    private int bookingId;
    private int roomNumber;
    private String roomType;
    private String guestName;
    private String contactNumber;
    private String checkInDate;
    private String checkOutDate;
    private int numberOfNights;
    private double pricePerNight;
    private double totalAmount;
    private boolean checkedOut;

    public Booking(int roomNumber, String roomType, String guestName,
                   String contactNumber, String checkInDate, String checkOutDate,
                   int numberOfNights, double pricePerNight) {
        this.bookingId = ++counter;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.guestName = guestName;
        this.contactNumber = contactNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfNights = numberOfNights;
        this.pricePerNight = pricePerNight;
        this.totalAmount = pricePerNight * numberOfNights;
        this.checkedOut = false;
    }

    // Getters
    public int    getBookingId()        { return bookingId; }
    public int    getRoomNumber()       { return roomNumber; }
    public String getRoomType()         { return roomType; }
    public String getGuestName()        { return guestName; }
    public String getContactNumber()    { return contactNumber; }
    public String getCheckInDate()      { return checkInDate; }
    public String getCheckOutDate()     { return checkOutDate; }
    public int    getNumberOfNights()   { return numberOfNights; }
    public double getPricePerNight()    { return pricePerNight; }
    public double getTotalAmount()      { return totalAmount; }
    public boolean isCheckedOut()       { return checkedOut; }
    public void setCheckedOut(boolean v){ this.checkedOut = v; }

    /** Generate a formatted bill string - classic old-style receipt */
    public String generateBill() {
        String thick = "=".repeat(48);
        String thin  = "-".repeat(48);
        double gst   = totalAmount * 0.12;
        double grand = totalAmount + gst;
        return  "\n" +
                thick + "\n" +
                "           LA PARISIAN HOTEL           \n" +
                "         Est. 1924  -  Paris, France    \n" +
                thick + "\n" +
                "\n" +
                "                 RECEIPT                \n" +
                "\n" +
                thin + "\n" +
                String.format("  Booking Ref   : #%d%n",        bookingId) +
                String.format("  Date          : %s%n",         checkInDate) +
                thin + "\n" +
                "\n" +
                "  GUEST DETAILS\n" +
                String.format("  Name          : %s%n",         guestName) +
                String.format("  Contact       : %s%n",         contactNumber) +
                "\n" +
                "  ROOM DETAILS\n" +
                String.format("  Room No.      : %d%n",         roomNumber) +
                String.format("  Room Type     : %s%n",         roomType) +
                String.format("  Check In      : %s%n",         checkInDate) +
                String.format("  Check Out     : %s%n",         checkOutDate) +
                String.format("  No. of Nights : %d%n",         numberOfNights) +
                "\n" +
                thin + "\n" +
                "  BILLING SUMMARY\n" +
                thin + "\n" +
                String.format("  Rate / Night  : Rs. %8.2f%n",  pricePerNight) +
                String.format("  Sub Total     : Rs. %8.2f%n",  totalAmount) +
                String.format("  GST  @ 12%%    : Rs. %8.2f%n", gst) +
                thin + "\n" +
                String.format("  TOTAL PAYABLE : Rs. %8.2f%n",  grand) +
                thick + "\n" +
                "\n" +
                "    Thank you for your stay with us.\n" +
                "      We hope to welcome you again.\n" +
                "\n" +
                thick + "\n";
    }

    @Override
    public String toString() {
        return "Booking[#" + bookingId + " | Room " + roomNumber +
               " | " + guestName + " | " + numberOfNights + " nights]";
    }
}
