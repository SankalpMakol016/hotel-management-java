package com.hotel.ui;

import com.hotel.model.Booking;
import com.hotel.service.HotelService;
import com.hotel.util.UIHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

import java.util.List;

/**
 * History Tab - all bookings, revenue summary.
 * Week 8: Collections, Iterator, TableView.
 */
public class HistoryTab {

    private final HotelService service;
    private TableView<Booking> historyTable;
    private Label revenueLabel, pendingLabel, totalBookingsLabel;

    public HistoryTab(HotelService service) { this.service = service; }

    public javafx.scene.Node buildPanel() {
        return buildContent();
    }

    @SuppressWarnings("unchecked")
    private BorderPane buildContent() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + UIHelper.COLOR_BG + ";");
        root.setPadding(new Insets(20));

        Label title = UIHelper.sectionTitle("Booking History & Revenue");
        VBox topBox = new VBox(8, title);
        topBox.setPadding(new Insets(0, 0, 14, 0));
        root.setTop(topBox);

        // Stats bar
        revenueLabel       = new Label("-");
        pendingLabel       = new Label("-");
        totalBookingsLabel = new Label("-");

        HBox statsBar = new HBox(16,
            miniStat("Revenue Collected", revenueLabel,       UIHelper.COLOR_SUCCESS),
            miniStat("Pending Revenue",   pendingLabel,        UIHelper.COLOR_WARNING),
            miniStat("Total Bookings",    totalBookingsLabel,  UIHelper.COLOR_ACCENT));
        statsBar.setAlignment(Pos.CENTER_LEFT);
        statsBar.setPadding(new Insets(0, 0, 14, 0));

        // Table
        historyTable = new TableView<>();
        historyTable.setStyle(UIHelper.STYLE_TABLE);
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        historyTable.setPlaceholder(new Label("No booking records yet."));

        TableColumn<Booking, Integer> colId = new TableColumn<>("Booking #");
        colId.setCellValueFactory(new PropertyValueFactory<>("bookingId"));

        TableColumn<Booking, Integer> colRoom = new TableColumn<>("Room");
        colRoom.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Booking, String> colGuest = new TableColumn<>("Guest");
        colGuest.setCellValueFactory(new PropertyValueFactory<>("guestName"));

        TableColumn<Booking, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        TableColumn<Booking, String> colIn = new TableColumn<>("Check-In");
        colIn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));

        TableColumn<Booking, String> colOut = new TableColumn<>("Check-Out");
        colOut.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));

        TableColumn<Booking, Integer> colNights = new TableColumn<>("Nights");
        colNights.setCellValueFactory(new PropertyValueFactory<>("numberOfNights"));

        TableColumn<Booking, Double> colAmount = new TableColumn<>("Total (incl. GST)");
        colAmount.setCellValueFactory(c ->
            new javafx.beans.property.SimpleDoubleProperty(
                c.getValue().getTotalAmount() * 1.12).asObject());
        colAmount.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : String.format("Rs.%.2f", v));
            }
        });

        TableColumn<Booking, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().isCheckedOut() ? "Checked Out" : "Active"));

        historyTable.getColumns().addAll(colId, colRoom, colGuest, colType,
                                         colIn, colOut, colNights, colAmount, colStatus);

        historyTable.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Booking item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setStyle(UIHelper.rowStyle(UIHelper.COLOR_CARD, false));
                else if (isSelected())
                    setStyle(UIHelper.rowStyle(UIHelper.COLOR_CARD, true));
                else if (item.isCheckedOut())
                    setStyle(UIHelper.rowStyle("#F0F9F3", false));
                else
                    setStyle(UIHelper.rowStyle(UIHelper.COLOR_CARD, false));
            }
        });
        historyTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) ->
            historyTable.refresh());

        // Buttons
        Button btnViewBill = UIHelper.primaryButton("View Invoice");
        Button btnExport   = UIHelper.neutralButton("Export Invoice");
        Button btnRefresh  = UIHelper.neutralButton("Refresh");

        btnViewBill.setOnAction(e -> {
            Booking b = historyTable.getSelectionModel().getSelectedItem();
            if (b == null) { UIHelper.showError("No Selection", "Select a booking first."); return; }
            UIHelper.showBillDialog(b.generateBill());
        });
        btnExport.setOnAction(e -> {
            Booking b = historyTable.getSelectionModel().getSelectedItem();
            if (b == null) { UIHelper.showError("No Selection", "Select a booking first."); return; }
            service.exportBill(b);
            UIHelper.showInfo("Exported", "Bill saved to data/Bill_" + b.getBookingId() + ".txt");
        });
        btnRefresh.setOnAction(e -> refresh());

        HBox btnRow = new HBox(14, btnViewBill, btnExport, btnRefresh);
        btnRow.setAlignment(Pos.CENTER_LEFT);
        btnRow.setPadding(new Insets(12, 0, 0, 0));

        VBox center = new VBox(statsBar, historyTable, btnRow);
        VBox.setVgrow(historyTable, Priority.ALWAYS);
        root.setCenter(center);

        refresh();
        return root;
    }

    private VBox miniStat(String label, Label valueLabel, String color) {
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label lbl = UIHelper.bodyLabel(label);
        lbl.setStyle(UIHelper.STYLE_LABEL_MUTED);
        VBox card = UIHelper.card(14);
        card.setMinWidth(200);
        card.getChildren().addAll(valueLabel, lbl);
        return card;
    }

    public void refresh() {
        List<Booking> all = service.getAllBookings();
        historyTable.setItems(FXCollections.observableArrayList(all));
        revenueLabel      .setText(String.format("Rs.%.2f", service.getTotalRevenue()));
        pendingLabel      .setText(String.format("Rs.%.2f", service.getPendingRevenue()));
        totalBookingsLabel.setText(String.valueOf(all.size()));
    }
}
