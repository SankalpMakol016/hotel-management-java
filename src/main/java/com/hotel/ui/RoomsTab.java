package com.hotel.ui;

import com.hotel.model.Room;
import com.hotel.model.RoomType;
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
 * Rooms Tab - add, view, filter, delete rooms.
 * Week 9: TableView, ComboBox, TextField, Button, event handling.
 * Week 2: RoomType enum used for ComboBox values.
 * Week 8: Collections sort used for table ordering.
 */
public class RoomsTab {

    private final HotelService service;
    private TableView<Room> tableView;
    private TextField tfRoomNum, tfPrice;
    private ComboBox<String> cbRoomType, cbSortBy, cbFilter;
    private Label statusLabel;

    public RoomsTab(HotelService service) { this.service = service; }

    public javafx.scene.Node buildPanel() {
        return buildContent();
    }

    private BorderPane buildContent() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + UIHelper.COLOR_BG + ";");
        root.setPadding(new Insets(20));
        root.setTop(buildTopBar());
        root.setLeft(buildAddForm());
        root.setCenter(buildTable());
        return root;
    }

    private VBox buildTopBar() {
        Label title = UIHelper.sectionTitle("Room Management");

        cbFilter = new ComboBox<>(FXCollections.observableArrayList(
                "All Rooms", "Available Only", "Occupied Only", "STANDARD", "DELUXE", "SUITE"));
        cbFilter.setValue("All Rooms");
        cbFilter.setStyle(UIHelper.STYLE_COMBO);

        cbSortBy = new ComboBox<>(FXCollections.observableArrayList(
                "Room Number", "Price (Low to High)"));
        cbSortBy.setValue("Room Number");
        cbSortBy.setStyle(UIHelper.STYLE_COMBO);

        Button btnApply = UIHelper.neutralButton("Apply");
        btnApply.setOnAction(e -> refreshTable());

        HBox filterBar = new HBox(12,
            UIHelper.bodyLabel("Filter:"), cbFilter,
            UIHelper.bodyLabel("Sort By:"), cbSortBy, btnApply);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        statusLabel = new Label("");
        statusLabel.setStyle(UIHelper.STYLE_LABEL_MUTED);

        VBox top = new VBox(10, title, filterBar, statusLabel);
        top.setPadding(new Insets(0, 0, 16, 0));
        return top;
    }

    private VBox buildAddForm() {
        VBox form = UIHelper.card(20);
        form.setSpacing(12);
        form.setMinWidth(230);
        form.setMaxWidth(230);

        Label formTitle = UIHelper.sectionTitle("Add Room");

        tfRoomNum = UIHelper.styledTextField("e.g. 305");

        cbRoomType = new ComboBox<>(FXCollections.observableArrayList("STANDARD", "DELUXE", "SUITE"));
        cbRoomType.setValue("STANDARD");
        cbRoomType.setStyle(UIHelper.STYLE_COMBO);
        cbRoomType.setMaxWidth(Double.MAX_VALUE);
        cbRoomType.setOnAction(e -> autofillPrice());

        tfPrice = UIHelper.styledTextField("Price per night");
        autofillPrice();

        Button btnAdd = UIHelper.primaryButton("Add Room");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setOnAction(e -> handleAddRoom());

        Button btnDelete = UIHelper.dangerButton("Delete Selected");
        btnDelete.setMaxWidth(Double.MAX_VALUE);
        btnDelete.setOnAction(e -> handleDeleteRoom());

        form.getChildren().addAll(
            formTitle,
            UIHelper.bodyLabel("Room Number:"), tfRoomNum,
            UIHelper.bodyLabel("Room Type:"),   cbRoomType,
            UIHelper.bodyLabel("Price / Night (Rs.):"), tfPrice,
            btnAdd, new Separator(), btnDelete
        );
        BorderPane.setMargin(form, new Insets(0, 16, 0, 0));
        return form;
    }

    @SuppressWarnings("unchecked")
    private VBox buildTable() {
        tableView = new TableView<>();
        tableView.setStyle(UIHelper.STYLE_TABLE);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No rooms found."));

        TableColumn<Room, Integer> colNum = new TableColumn<>("Room #");
        colNum.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Room, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        TableColumn<Room, Double> colPrice = new TableColumn<>("Price/Night");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
        colPrice.setCellFactory(c -> new TableCell<>() {
            @Override protected void updateItem(Double v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? null : String.format("Rs.%.0f", v));
            }
        });

        TableColumn<Room, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().isAvailable() ? "Available" : "Occupied"));

        TableColumn<Room, String> colGuest = new TableColumn<>("Guest");
        colGuest.setCellValueFactory(c ->
            new SimpleStringProperty(
                c.getValue().getGuestName() != null ? c.getValue().getGuestName() : "-"));

        tableView.getColumns().addAll(colNum, colType, colPrice, colStatus, colGuest);

        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Room item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle(UIHelper.rowStyle(UIHelper.COLOR_CARD, false));
                } else if (isSelected()) {
                    setStyle(UIHelper.rowStyle(UIHelper.COLOR_CARD, true));
                } else if (!item.isAvailable()) {
                    setStyle(UIHelper.rowStyle("#FFF3F2", false));
                } else {
                    setStyle(UIHelper.rowStyle(UIHelper.COLOR_CARD, false));
                }
            }
        });

        // Re-apply style on selection change so selected row updates immediately
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) ->
            tableView.refresh());

        refreshTable();
        VBox wrapper = new VBox(tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        return wrapper;
    }

    private void autofillPrice() {
        try {
            RoomType rt = RoomType.valueOf(cbRoomType.getValue());
            tfPrice.setText(String.valueOf((int) rt.getBasePrice()));
        } catch (Exception ignored) {}
    }

    private void handleAddRoom() {
        try {
            int num = Integer.parseInt(tfRoomNum.getText().trim());
            String type = cbRoomType.getValue();
            double price = Double.parseDouble(tfPrice.getText().trim());
            if (service.roomNumberExists(num)) {
                UIHelper.showError("Duplicate", "Room " + num + " already exists."); return;
            }
            if (price <= 0) {
                UIHelper.showError("Invalid Price", "Price must be positive."); return;
            }
            service.addRoom(new Room(num, type, price));
            service.saveAll();
            refreshTable();
            tfRoomNum.clear();
            statusLabel.setText("Room " + num + " added.");
            statusLabel.setStyle("-fx-text-fill: " + UIHelper.COLOR_SUCCESS + ";");
        } catch (NumberFormatException e) {
            UIHelper.showError("Input Error", "Enter valid numbers for Room Number and Price.");
        }
    }

    private void handleDeleteRoom() {
        Room selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) { UIHelper.showError("No Selection", "Select a room first."); return; }
        if (!selected.isAvailable()) {
            UIHelper.showError("Occupied", "Check out the guest before deleting."); return;
        }
        if (UIHelper.showConfirm("Delete Room", "Delete Room " + selected.getRoomNumber() + "?")) {
            service.deleteRoom(selected.getRoomNumber());
            service.saveAll();
            refreshTable();
            statusLabel.setText("Room " + selected.getRoomNumber() + " deleted.");
            statusLabel.setStyle("-fx-text-fill: " + UIHelper.COLOR_DANGER + ";");
        }
    }

    public void refreshTable() {
        String filter = cbFilter != null ? cbFilter.getValue() : "All Rooms";
        String sort   = cbSortBy  != null ? cbSortBy.getValue()  : "Room Number";

        List<Room> rooms = "Price (Low to High)".equals(sort)
                ? service.getRoomsSortedByPrice()
                : service.getRoomsSortedByNumber();

        List<Room> filtered = switch (filter) {
            case "Available Only" -> rooms.stream().filter(Room::isAvailable).toList();
            case "Occupied Only"  -> rooms.stream().filter(r -> !r.isAvailable()).toList();
            case "STANDARD"       -> rooms.stream().filter(r -> "STANDARD".equals(r.getRoomType())).toList();
            case "DELUXE"         -> rooms.stream().filter(r -> "DELUXE".equals(r.getRoomType())).toList();
            case "SUITE"          -> rooms.stream().filter(r -> "SUITE".equals(r.getRoomType())).toList();
            default               -> rooms;
        };

        tableView.setItems(FXCollections.observableArrayList(filtered));
    }
}
