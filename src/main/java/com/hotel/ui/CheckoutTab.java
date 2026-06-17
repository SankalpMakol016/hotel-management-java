package com.hotel.ui;

import com.hotel.model.Booking;
import com.hotel.model.Room;
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
 * Checkout Tab - checkout guests, view/export bills.
 * Billing management feature (extra marks).
 */
public class CheckoutTab {

    private final HotelService service;
    private final Runnable refreshCallback;
    private TableView<Room> occupiedTable;
    private Label statusLabel;

    public CheckoutTab(HotelService service, Runnable refreshCallback) {
        this.service = service;
        this.refreshCallback = refreshCallback;
    }

    public javafx.scene.Node buildPanel() {
        return buildContent();
    }

    @SuppressWarnings("unchecked")
    private BorderPane buildContent() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + UIHelper.COLOR_BG + ";");
        root.setPadding(new Insets(20));

        Label title = UIHelper.sectionTitle("Guest Checkout & Billing");
        Label hint  = UIHelper.bodyLabel("Select an occupied room to check out the guest and generate their invoice.");
        hint.setStyle(UIHelper.STYLE_LABEL_MUTED);
        VBox topBox = new VBox(8, title, hint);
        topBox.setPadding(new Insets(0, 0, 14, 0));
        root.setTop(topBox);

        occupiedTable = new TableView<>();
        occupiedTable.setStyle(UIHelper.STYLE_TABLE);
        occupiedTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        occupiedTable.setPlaceholder(new Label("No rooms are currently occupied."));

        TableColumn<Room, Integer> colNum = new TableColumn<>("Room #");
        colNum.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Room, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        TableColumn<Room, Double> colPrice = new TableColumn<>("Rate/Night");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colPrice.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : String.format("Rs.%.0f", v));
            }
        });

        TableColumn<Room, String> colGuest = new TableColumn<>("Guest Name");
        colGuest.setCellValueFactory(c ->
            new SimpleStringProperty(
                c.getValue().getGuestName() != null ? c.getValue().getGuestName() : "-"));

        occupiedTable.getColumns().addAll(colNum, colType, colPrice, colGuest);

        occupiedTable.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty)
                    setStyle(UIHelper.rowStyle(UIHelper.COLOR_CARD, false));
                else if (isSelected())
                    setStyle(UIHelper.rowStyle(UIHelper.COLOR_CARD, true));
                else
                    setStyle(UIHelper.rowStyle("#FFF3F2", false));
            }
        });
        occupiedTable.getSelectionModel().selectedItemProperty().addListener((obs, o, n) ->
            occupiedTable.refresh());

        Button btnCheckout   = UIHelper.dangerButton("Checkout Guest");
        Button btnViewBill   = UIHelper.primaryButton("View Bill");
        Button btnExportBill = UIHelper.neutralButton("Export Bill (.txt)");
        Button btnRefresh    = UIHelper.neutralButton("Refresh");
        statusLabel = new Label("");
        statusLabel.setStyle(UIHelper.STYLE_LABEL_MUTED);

        btnCheckout  .setOnAction(e -> handleCheckout());
        btnViewBill  .setOnAction(e -> handleViewBill());
        btnExportBill.setOnAction(e -> handleExportBill());
        btnRefresh   .setOnAction(e -> refreshTable());

        HBox btnRow = new HBox(14, btnCheckout, btnViewBill, btnExportBill, btnRefresh);
        btnRow.setAlignment(Pos.CENTER_LEFT);
        btnRow.setPadding(new Insets(12, 0, 0, 0));

        VBox center = new VBox(occupiedTable, btnRow, statusLabel);
        VBox.setVgrow(occupiedTable, Priority.ALWAYS);
        root.setCenter(center);

        refreshTable();
        return root;
    }

    private void handleCheckout() {
        Room selected = occupiedTable.getSelectionModel().getSelectedItem();
        if (selected == null) { UIHelper.showError("No Selection", "Select an occupied room."); return; }
        if (!UIHelper.showConfirm("Confirm Checkout",
                "Check out " + selected.getGuestName() + " from Room " + selected.getRoomNumber() + "?"))
            return;

        Booking b = service.checkoutRoom(selected.getRoomNumber());
        if (b != null) {
            statusLabel.setText("Checked out: " + b.getGuestName() + " | Room " + b.getRoomNumber());
            statusLabel.setStyle("-fx-text-fill: " + UIHelper.COLOR_SUCCESS + ";");
            UIHelper.showBillDialog(b.generateBill());
            refreshTable();
            refreshCallback.run();
        } else {
            UIHelper.showError("Error", "Could not find active booking for this room.");
        }
    }

    private Booking getActiveBookingForSelected() {
        Room selected = occupiedTable.getSelectionModel().getSelectedItem();
        if (selected == null) return null;
        for (Booking b : service.getActiveBookings())
            if (b.getRoomNumber() == selected.getRoomNumber()) return b;
        return null;
    }

    private void handleViewBill() {
        Booking b = getActiveBookingForSelected();
        if (b == null) { UIHelper.showError("No Selection", "Select an occupied room first."); return; }
        UIHelper.showBillDialog(b.generateBill());
    }

    private void handleExportBill() {
        Booking b = getActiveBookingForSelected();
        if (b == null) { UIHelper.showError("No Selection", "Select an occupied room first."); return; }
        service.exportBill(b);
        UIHelper.showInfo("Exported", "Bill saved to data/Bill_" + b.getBookingId() + ".txt");
    }

    public void refreshTable() {
        occupiedTable.setItems(FXCollections.observableArrayList(service.getOccupiedRooms()));
    }
}
