package com.hotel.ui;

import com.hotel.service.HotelService;
import com.hotel.util.UIHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Dashboard Tab - overview stats.
 * Week 9: JavaFX Labels, VBox, HBox, GridPane layouts.
 */
public class DashboardTab {

    private final HotelService service;
    private Label totalRoomsVal, availableVal, occupiedVal,
                  revenueVal, pendingVal, bookingsVal;

    public DashboardTab(HotelService service) { this.service = service; }

    public javafx.scene.Node buildPanel() {
        return buildContent();
    }

    private ScrollPane buildContent() {
        VBox root = new VBox(24);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: " + UIHelper.COLOR_BG + ";");

        // Header
        Label hotelName = new Label("LA PARISIAN HOTEL");
        hotelName.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 26px;" +
                           "-fx-font-weight: bold; -fx-text-fill: " + UIHelper.COLOR_SIDEBAR + ";");
        Label subtitle = new Label("Hotel Management System  -  Operations Dashboard");
        subtitle.setStyle(UIHelper.STYLE_LABEL_MUTED + " -fx-font-size: 13px;");
        VBox header = new VBox(6, hotelName, subtitle);

        // Stats cards
        Label statsTitle = UIHelper.sectionTitle("Overview");

        totalRoomsVal = new Label("--");
        availableVal  = new Label("--");
        occupiedVal   = new Label("--");
        revenueVal    = new Label("--");
        pendingVal    = new Label("--");
        bookingsVal   = new Label("--");

        GridPane grid = new GridPane();
        grid.setHgap(16); grid.setVgap(16);
        grid.add(statCard("Total Rooms",      totalRoomsVal, UIHelper.COLOR_ACCENT),       0, 0);
        grid.add(statCard("Available",         availableVal,  UIHelper.COLOR_SUCCESS),       1, 0);
        grid.add(statCard("Occupied",          occupiedVal,   UIHelper.COLOR_DANGER),        2, 0);
        grid.add(statCard("Revenue Collected", revenueVal,    UIHelper.COLOR_ACCENT),        0, 1);
        grid.add(statCard("Pending Revenue",   pendingVal,    UIHelper.COLOR_WARNING),       1, 1);
        grid.add(statCard("Total Bookings",    bookingsVal,   UIHelper.COLOR_TEXT_MUTED),    2, 1);

        // Quick guide
        Label infoTitle = UIHelper.sectionTitle("Quick Guide");
        VBox infoCard = UIHelper.card(20);
        infoCard.getChildren().addAll(
            infoRow("Rooms",    "Add, view, filter and manage all hotel rooms"),
            infoRow("Bookings", "Book rooms for guests with check-in/check-out dates"),
            infoRow("Checkout", "Process checkouts and generate guest invoices"),
            infoRow("History",  "View all past bookings and revenue summaries")
        );

        root.getChildren().addAll(header, new Separator(), statsTitle, grid,
                                   new Separator(), infoTitle, infoCard);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + UIHelper.COLOR_BG + ";" +
                        "-fx-background: " + UIHelper.COLOR_BG + ";");
        return scroll;
    }

    private VBox statCard(String label, Label valueLabel, String color) {
        valueLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;" +
                            "-fx-text-fill: " + color + ";");
        Label lbl = new Label(label);
        lbl.setStyle(UIHelper.STYLE_LABEL_MUTED);
        VBox card = UIHelper.card(18);
        card.setAlignment(Pos.CENTER);
        card.setMinWidth(180);
        card.getChildren().addAll(valueLabel, lbl);
        return card;
    }

    private HBox infoRow(String title, String desc) {
        Label titleL = new Label(title);
        titleL.setStyle("-fx-font-weight: bold; -fx-text-fill: " + UIHelper.COLOR_ACCENT2 +
                        "; -fx-font-size: 13px; -fx-min-width: 80px;");
        Label descL = new Label(desc);
        descL.setStyle(UIHelper.STYLE_LABEL_MUTED);
        HBox row = new HBox(16, titleL, descL);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    public void refresh() {
        totalRoomsVal.setText(String.valueOf(service.getTotalRooms()));
        availableVal .setText(String.valueOf(service.getAvailableCount()));
        occupiedVal  .setText(String.valueOf(service.getOccupiedCount()));
        revenueVal   .setText(String.format("Rs.%.0f", service.getTotalRevenue()));
        pendingVal   .setText(String.format("Rs.%.0f", service.getPendingRevenue()));
        bookingsVal  .setText(String.valueOf(service.getAllBookings().size()));
    }
}
