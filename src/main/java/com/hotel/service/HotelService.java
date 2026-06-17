package com.hotel.service;

import com.hotel.dao.FileDataStore;
import com.hotel.model.Booking;
import com.hotel.model.Room;

import java.util.*;

/**
 * Business logic layer.
 * Week 4: Synchronized methods (thread-safe booking/checkout)
 * Week 7: Generics - generic bounded method sumList()
 * Week 8: Collections - ArrayList, HashMap, Iterator, Collections.sort()
 */
public class HotelService {

    // Week 8: ArrayList for rooms and bookings
    private List<Room> rooms;
    private List<Booking> bookings;

    // Week 8: HashMap for O(1) room lookup by number
    private Map<Integer, Room> roomMap;

    private final FileDataStore dataStore;

    public HotelService() {
        this.dataStore = new FileDataStore();
        this.rooms     = new ArrayList<>(dataStore.loadRooms());
        this.bookings  = new ArrayList<>(dataStore.loadBookings());
        this.roomMap   = new HashMap<>();
        for (Room r : rooms) roomMap.put(r.getRoomNumber(), r);
        if (rooms.isEmpty()) seedDefaultRooms();
    }

    private void seedDefaultRooms() {
        addRoom(new Room(101, "STANDARD", 1500.0));
        addRoom(new Room(102, "STANDARD", 1500.0));
        addRoom(new Room(103, "STANDARD", 1500.0));
        addRoom(new Room(201, "DELUXE",   2800.0));
        addRoom(new Room(202, "DELUXE",   2800.0));
        addRoom(new Room(301, "SUITE",    5500.0));
        addRoom(new Room(302, "SUITE",    5500.0));
        saveAll();
    }

    // ── Room operations ───────────────────────────────────────────────────────

    public synchronized void addRoom(Room room) {
        rooms.add(room);
        roomMap.put(room.getRoomNumber(), room);
    }

    public Room getRoomByNumber(int number) { return roomMap.get(number); }

    public List<Room> getAllRooms()      { return Collections.unmodifiableList(rooms); }

    // Week 8: Iterator usage
    public List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();
        Iterator<Room> it = rooms.iterator();
        while (it.hasNext()) {
            Room r = it.next();
            if (r.isAvailable()) available.add(r);
        }
        return available;
    }

    public List<Room> getOccupiedRooms() {
        List<Room> occupied = new ArrayList<>();
        for (Room r : rooms) if (!r.isAvailable()) occupied.add(r);
        return occupied;
    }

    // Week 8: Collections.sort() with Comparator
    public List<Room> getRoomsSortedByPrice() {
        List<Room> sorted = new ArrayList<>(rooms);
        Collections.sort(sorted, Comparator.comparingDouble(Room::getPricePerNight));
        return sorted;
    }

    public List<Room> getRoomsSortedByNumber() {
        List<Room> sorted = new ArrayList<>(rooms);
        Collections.sort(sorted, Comparator.comparingInt(Room::getRoomNumber));
        return sorted;
    }

    public boolean roomNumberExists(int number) { return roomMap.containsKey(number); }

    public void deleteRoom(int roomNumber) {
        Room r = roomMap.get(roomNumber);
        if (r != null) { rooms.remove(r); roomMap.remove(roomNumber); }
    }

    // ── Booking operations ────────────────────────────────────────────────────

    // Week 4: synchronized - prevents double booking from concurrent threads
    public synchronized String bookRoom(int roomNumber, String guestName,
                                        String contact, String checkIn,
                                        String checkOut, int nights) {
        Room room = roomMap.get(roomNumber);
        if (room == null)        return "ERROR: Room " + roomNumber + " does not exist.";
        if (!room.isAvailable()) return "ERROR: Room " + roomNumber + " is already occupied.";
        if (nights <= 0)         return "ERROR: Number of nights must be at least 1.";

        room.setAvailable(false);
        room.setGuestName(guestName);

        Booking b = new Booking(roomNumber, room.getRoomType(), guestName,
                contact, checkIn, checkOut, nights, room.getPricePerNight());
        bookings.add(b);
        saveAll();
        return "SUCCESS: Room " + roomNumber + " booked for " + guestName +
               "  |  Booking ID: #" + b.getBookingId();
    }

    // Week 4: synchronized checkout
    public synchronized Booking checkoutRoom(int roomNumber) {
        Room room = roomMap.get(roomNumber);
        if (room == null || room.isAvailable()) return null;

        Booking active = null;
        for (Booking b : bookings) {
            if (b.getRoomNumber() == roomNumber && !b.isCheckedOut()) {
                active = b; break;
            }
        }
        room.setAvailable(true);
        room.setGuestName(null);
        if (active != null) active.setCheckedOut(true);
        saveAll();
        return active;
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    public List<Booking> getAllBookings()    { return Collections.unmodifiableList(bookings); }

    public List<Booking> getActiveBookings() {
        List<Booking> active = new ArrayList<>();
        for (Booking b : bookings) if (!b.isCheckedOut()) active.add(b);
        return active;
    }

    public List<Booking> getBookingHistory() {
        List<Booking> history = new ArrayList<>();
        for (Booking b : bookings) if (b.isCheckedOut()) history.add(b);
        return history;
    }

    // Week 7: Generic bounded method
    public static <T extends Number> double sumList(List<T> list) {
        double total = 0;
        for (T item : list) total += item.doubleValue();
        return total;
    }

    public double getTotalRevenue() {
        List<Double> amounts = new ArrayList<>();
        for (Booking b : bookings) if (b.isCheckedOut()) amounts.add(b.getTotalAmount() * 1.12);
        return sumList(amounts);
    }

    public double getPendingRevenue() {
        List<Double> amounts = new ArrayList<>();
        for (Booking b : bookings) if (!b.isCheckedOut()) amounts.add(b.getTotalAmount() * 1.12);
        return sumList(amounts);
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    public void saveAll() {
        dataStore.saveRooms(rooms);
        dataStore.saveBookings(bookings);
    }

    public void exportBill(Booking b) { dataStore.exportBillToFile(b); }

    // ── Stats ─────────────────────────────────────────────────────────────────

    public int getTotalRooms()     { return rooms.size(); }
    public int getAvailableCount() { return getAvailableRooms().size(); }
    public int getOccupiedCount()  { return getOccupiedRooms().size(); }
}
