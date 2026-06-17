package com.hotel.dao;

import com.hotel.model.Booking;
import com.hotel.model.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file-based persistence.
 * Week 5: FileOutputStream, FileInputStream, FileWriter (character streams)
 * Week 6: Serialization and Deserialization
 */
public class FileDataStore {

    private static final String ROOMS_FILE    = "data/rooms.dat";
    private static final String BOOKINGS_FILE = "data/bookings.dat";

    public FileDataStore() {
        new File("data").mkdirs();
    }

    // Week 6: Serialization - save rooms
    public void saveRooms(List<Room> rooms) {
        try (FileOutputStream fos = new FileOutputStream(ROOMS_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(rooms);
        } catch (IOException e) {
            System.err.println("Error saving rooms: " + e.getMessage());
        }
    }

    // Week 6: Deserialization - load rooms
    @SuppressWarnings("unchecked")
    public List<Room> loadRooms() {
        File f = new File(ROOMS_FILE);
        if (!f.exists()) return new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(ROOMS_FILE);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Room>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading rooms: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Week 6: Serialization - save bookings
    public void saveBookings(List<Booking> bookings) {
        try (FileOutputStream fos = new FileOutputStream(BOOKINGS_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }

    // Week 6: Deserialization - load bookings
    @SuppressWarnings("unchecked")
    public List<Booking> loadBookings() {
        File f = new File(BOOKINGS_FILE);
        if (!f.exists()) return new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(BOOKINGS_FILE);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Booking>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Week 5: FileWriter (character stream) - export bill as text file
    public void exportBillToFile(Booking booking) {
        String filename = "data/Bill_" + booking.getBookingId() + ".txt";
        try (FileWriter fw = new FileWriter(filename)) {
            fw.write(booking.generateBill());
            System.out.println("Bill exported to " + filename);
        } catch (IOException e) {
            System.err.println("Error exporting bill: " + e.getMessage());
        }
    }
}
