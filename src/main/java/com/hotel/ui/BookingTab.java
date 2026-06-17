package com.hotel.ui;

import com.hotel.model.Room;
import com.hotel.service.HotelService;
import com.hotel.util.UIHelper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Booking Tab - book rooms for guests.
 * Week 9: ComboBox, DatePicker, GridPane, event handling.
 * Week 4: Synchronized booking via HotelService.
 */
public class BookingTab {

    private final HotelService service;
    private final Runnable refreshCallback;

    private ComboBox<Integer> cbRoomNumber;
    private ComboBox<String> cbTypeFilter;
    private TextField tfGuestName, tfContact, tfNights;
    private DatePicker dpCheckIn;
    private Label lblRoomInfo, lblEstimate, statusLabel;

    public BookingTab(HotelService service, Runnable refreshCallback) {
        this.service = service;
        this.refreshCallback = refreshCallback;
    }

    public javafx.scene.Node buildPanel() {
        return buildContent();
    }

    private ScrollPane buildContent() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: " + UIHelper.COLOR_BG + ";");

        Label title = UIHelper.sectionTitle("New Room Booking");

        // Room selection card
        VBox roomCard = UIHelper.card(20);
        roomCard.setSpacing(12);

        Label roomTitle = UIHelper.bodyLabel("Select Room");
        roomTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;" +
                           "-fx-text-fill: " + UIHelper.COLOR_ACCENT2 + ";");

        cbTypeFilter = new ComboBox<>(FXCollections.observableArrayList(
                "All Types", "STANDARD", "DELUXE", "SUITE"));
        cbTypeFilter.setValue("All Types");
        cbTypeFilter.setStyle(UIHelper.STYLE_COMBO);
        cbTypeFilter.setOnAction(e -> refreshRoomCombo());

        cbRoomNumber = new ComboBox<>();
        cbRoomNumber.setStyle(UIHelper.STYLE_COMBO);
        cbRoomNumber.setMinWidth(130);
        cbRoomNumber.setOnAction(e -> updateRoomInfo());

        HBox roomSelRow = new HBox(12,
            UIHelper.bodyLabel("Type Filter:"), cbTypeFilter,
            UIHelper.bodyLabel("Room:"), cbRoomNumber);
        roomSelRow.setAlignment(Pos.CENTER_LEFT);

        lblRoomInfo = UIHelper.bodyLabel("Select a room to see details.");
        lblRoomInfo.setStyle(UIHelper.STYLE_LABEL_MUTED);

        roomCard.getChildren().addAll(roomTitle, roomSelRow, lblRoomInfo);
        refreshRoomCombo();

        // Guest details card
        VBox guestCard = UIHelper.card(20);
        guestCard.setSpacing(12);
        Label guestTitle = UIHelper.bodyLabel("Guest Information");
        guestTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;" +
                            "-fx-text-fill: " + UIHelper.COLOR_ACCENT2 + ";");

        tfGuestName = UIHelper.styledTextField("Full name");
        tfContact   = UIHelper.styledTextField("Mobile number");
        tfGuestName.setMinWidth(240);
        tfContact.setMinWidth(240);

        GridPane guestGrid = new GridPane();
        guestGrid.setHgap(16); guestGrid.setVgap(12);
        guestGrid.add(UIHelper.bodyLabel("Guest Name:"), 0, 0);
        guestGrid.add(tfGuestName,                       1, 0);
        guestGrid.add(UIHelper.bodyLabel("Contact:"),    0, 1);
        guestGrid.add(tfContact,                         1, 1);

        guestCard.getChildren().addAll(guestTitle, guestGrid);

        // Stay details card
        VBox stayCard = UIHelper.card(20);
        stayCard.setSpacing(12);
        Label stayTitle = UIHelper.bodyLabel("Stay Details");
        stayTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;" +
                           "-fx-text-fill: " + UIHelper.COLOR_ACCENT2 + ";");

        dpCheckIn = new DatePicker(LocalDate.now());
        dpCheckIn.setStyle(UIHelper.STYLE_COMBO);
        tfNights = UIHelper.styledTextField("e.g. 3");
        tfNights.setMinWidth(180);
        tfNights.textProperty().addListener((obs, o, n) -> updateEstimate());

        GridPane stayGrid = new GridPane();
        stayGrid.setHgap(16); stayGrid.setVgap(12);
        stayGrid.add(UIHelper.bodyLabel("Check-In Date:"),  0, 0);
        stayGrid.add(dpCheckIn,                             1, 0);
        stayGrid.add(UIHelper.bodyLabel("No. of Nights:"),  0, 1);
        stayGrid.add(tfNights,                              1, 1);

        lblEstimate = UIHelper.bodyLabel("Estimated Bill: -");
        lblEstimate.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;" +
                             "-fx-text-fill: " + UIHelper.COLOR_ACCENT + ";");

        stayCard.getChildren().addAll(stayTitle, stayGrid, lblEstimate);

        // Buttons
        Button btnBook  = UIHelper.primaryButton("Confirm Booking");
        Button btnClear = UIHelper.neutralButton("Clear Form");
        btnBook .setOnAction(e -> handleBooking());
        btnClear.setOnAction(e -> clearForm());
        HBox btnRow = new HBox(16, btnBook, btnClear);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        statusLabel = new Label("");
        statusLabel.setStyle(UIHelper.STYLE_LABEL_MUTED);

        root.getChildren().addAll(title, roomCard, guestCard, stayCard, btnRow, statusLabel);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + UIHelper.COLOR_BG + ";" +
                        "-fx-background: " + UIHelper.COLOR_BG + ";");
        return scroll;
    }

    public void refreshRoomCombo() {
        String filter = cbTypeFilter != null ? cbTypeFilter.getValue() : "All Types";
        List<Room> avail = service.getAvailableRooms();
        if (!"All Types".equals(filter))
            avail = avail.stream().filter(r -> r.getRoomType().equals(filter)).collect(Collectors.toList());
        List<Integer> nums = avail.stream().map(Room::getRoomNumber).collect(Collectors.toList());
        cbRoomNumber.setItems(FXCollections.observableArrayList(nums));
        if (!nums.isEmpty()) cbRoomNumber.setValue(nums.get(0));
        updateRoomInfo();
    }

    private void updateRoomInfo() {
        if (lblRoomInfo == null) return;
        if (cbRoomNumber.getValue() == null) { lblRoomInfo.setText("No available rooms."); return; }
        Room r = service.getRoomByNumber(cbRoomNumber.getValue());
        if (r != null) {
            lblRoomInfo.setText(String.format("Room %d  |  %s  |  Rs.%.0f / night  |  Available",
                    r.getRoomNumber(), r.getRoomType(), r.getPricePerNight()));
            lblRoomInfo.setStyle("-fx-text-fill: " + UIHelper.COLOR_SUCCESS + "; -fx-font-size: 13px;");
        }
        updateEstimate();
    }

    private void updateEstimate() {
        if (tfNights == null || lblEstimate == null) return;
        try {
            int nights = Integer.parseInt(tfNights.getText().trim());
            if (cbRoomNumber.getValue() == null) return;
            Room r = service.getRoomByNumber(cbRoomNumber.getValue());
            if (r != null && nights > 0) {
                double base = r.getPricePerNight() * nights;
                lblEstimate.setText(String.format(
                    "Base: Rs.%.0f  +  GST(12%%): Rs.%.0f  =  Total: Rs.%.0f",
                    base, base * 0.12, base * 1.12));
            }
        } catch (NumberFormatException ignored) {
            lblEstimate.setText("Estimated Bill: -");
        }
    }

    private void handleBooking() {
        try {
            if (cbRoomNumber.getValue() == null) {
                UIHelper.showError("No Room", "Select an available room."); return;
            }
            String guest   = tfGuestName.getText().trim();
            String contact = tfContact.getText().trim();
            int nights     = Integer.parseInt(tfNights.getText().trim());

            if (guest.isEmpty())   { UIHelper.showError("Missing", "Guest name is required."); return; }
            if (contact.isEmpty()) { UIHelper.showError("Missing", "Contact is required."); return; }
            if (nights <= 0)       { UIHelper.showError("Invalid", "Nights must be >= 1."); return; }
            if (dpCheckIn.getValue() == null) { UIHelper.showError("Missing", "Select check-in date."); return; }

            LocalDate checkIn  = dpCheckIn.getValue();
            LocalDate checkOut = checkIn.plusDays(nights);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            String result = service.bookRoom(cbRoomNumber.getValue(), guest, contact,
                    checkIn.format(fmt), checkOut.format(fmt), nights);

            if (result.startsWith("SUCCESS")) {
                UIHelper.showInfo("Booked!", result);
                statusLabel.setText(result);
                statusLabel.setStyle("-fx-text-fill: " + UIHelper.COLOR_SUCCESS + ";");
                clearForm();
                refreshCallback.run();
            } else {
                UIHelper.showError("Failed", result);
                statusLabel.setText(result);
                statusLabel.setStyle("-fx-text-fill: " + UIHelper.COLOR_DANGER + ";");
            }
        } catch (NumberFormatException e) {
            UIHelper.showError("Input Error", "Enter a valid number for nights.");
        }
    }

    private void clearForm() {
        tfGuestName.clear(); tfContact.clear(); tfNights.clear();
        dpCheckIn.setValue(LocalDate.now());
        lblEstimate.setText("Estimated Bill: -");
        refreshRoomCombo();
    }
}
