# 🏨 La Parisian — Hotel Management System

> A JavaFX desktop application for hotel room booking, checkout, and billing — built across a 10-week Object-Oriented Software Design course to solidify core Java and OOP fundamentals.

---

## 📌 About the Project

This was a semester project aimed at getting a strong, practical grip on Java — the industry's go-to language — and the principles behind good object-oriented design. Rather than building something trivial, the goal was a working, layered application where each new week's concept gets applied for real: not just written in a notebook, but running in code.

The result is **La Parisian**, a fully functional hotel management desktop app with an Art Deco UI, live data persistence, auto-save, and bill generation.

---

## ✨ Features

- **Dashboard** — Live stats: total rooms, occupancy rate, collected and pending revenue
- **Room Management** — View, add, and delete rooms; sortable by price or room number
- **Booking** — Book rooms for guests with check-in/check-out dates and night calculation
- **Checkout** — One-click guest checkout with automatic bill generation (PDF-style `.txt` receipt)
- **History** — Full booking history with filter support
- **Auto-Save** — Background daemon thread saves data every 60 seconds
- **File Persistence** — Rooms and bookings survive application restarts via Java serialization

---

## 🧠 OOP Concepts Practiced (Week by Week)

| Week | Concept | Where it appears |
|------|---------|-----------------|
| 1 | Encapsulation, Constructor Overloading | `Room.java` — private fields, two constructors |
| 2 | Enums with constructors & methods | `RoomType.java` — price + GST logic per type |
| 3 | Daemon Threads | `MainApp.java` — `startAutoSaveThread()` |
| 4 | Synchronization | `HotelService.java` — `synchronized bookRoom()` / `checkoutRoom()` |
| 5 | Character & Byte Streams | `FileDataStore.java` — `FileWriter` for bill export |
| 6 | Serialization / Deserialization | `FileDataStore.java` — `ObjectOutputStream` / `ObjectInputStream` |
| 7 | Generics (bounded type parameter) | `HotelService.java` — `<T extends Number> sumList()` |
| 8 | Collections — ArrayList, HashMap, Iterator, Comparator | `HotelService.java` — room lookup, sorting, iteration |
| 9 | JavaFX UI — Labels, VBox, HBox, GridPane, ComboBox, DatePicker | All `ui/` tab classes |
| 10 | Full integration, sidebar navigation, layered architecture | `MainApp.java` |

---

## 🗂️ Project Structure

```
HotelMS/
├── src/main/java/com/hotel/
│   ├── MainApp.java          # JavaFX entry point, sidebar navigation
│   ├── model/
│   │   ├── Room.java         # Room entity (Serializable)
│   │   ├── Booking.java      # Booking entity + bill generator
│   │   └── RoomType.java     # Enum: STANDARD / DELUXE / SUITE
│   ├── service/
│   │   └── HotelService.java # Business logic, thread-safe operations
│   ├── dao/
│   │   └── FileDataStore.java# File I/O and serialization
│   └── ui/
│       ├── DashboardTab.java
│       ├── RoomsTab.java
│       ├── BookingTab.java
│       ├── CheckoutTab.java
│       └── HistoryTab.java
├── data/                     # Auto-created at runtime
│   ├── rooms.dat             # Serialized room data
│   ├── bookings.dat          # Serialized booking data
│   └── Bill_XXXX.txt         # Generated receipts
└── pom.xml
```

---

## 🛠️ Tech Stack

- **Java 21**
- **JavaFX 21.0.2** — UI framework
- **Maven** — build and dependency management
- **Java Serialization** — file-based persistence (no database)

---

## 🚀 Running the Project

**Prerequisites:** JDK 21, Maven 3.8+

```bash
# Clone the repo
git clone https://github.com/your-username/hotel-management-java.git
cd hotel-management-java

# Run with Maven (JavaFX plugin handles module path automatically)
mvn javafx:run
```

The `data/` directory is created automatically on first run and pre-seeded with 7 default rooms (3 Standard, 2 Deluxe, 2 Suite).

---

## 📋 Default Room Inventory (Seeded on First Launch)

| Room | Type | Price/Night |
|------|------|-------------|
| 101, 102, 103 | Standard | ₹1,500 |
| 201, 202 | Deluxe | ₹2,800 |
| 301, 302 | Suite | ₹5,500 |

Prices shown **exclude GST**. Bills include a 12% GST calculation.

---

## 📄 Sample Bill Output

```
================================================
           LA PARISIAN HOTEL
         Est. 1924  -  Paris, France
================================================

                 RECEIPT

------------------------------------------------
  Booking Ref   : #1001
  Date          : 2026-04-13
...
  TOTAL PAYABLE : Rs.   6272.00
================================================
```

---

## 🎓 Context

Built during **Semester [X]** as part of an Object-Oriented Software Design course. The project was structured so that each weekly lecture concept — from basic encapsulation all the way to JavaFX and generics — was immediately applied in a growing, real codebase rather than isolated exercises.

---

## 📝 License

This project is for educational purposes. Feel free to fork and learn from it.