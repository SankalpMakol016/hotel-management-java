package com.hotel;

import com.hotel.service.HotelService;
import com.hotel.ui.*;
import com.hotel.util.UIHelper;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * MainApp — Art Deco sidebar layout.
 * Replaces TabPane with a fixed left sidebar + swappable content panel.
 */
public class MainApp extends Application {

    private HotelService hotelService;
    private DashboardTab dashboardTab;
    private RoomsTab     roomsTab;
    private BookingTab   bookingTab;
    private CheckoutTab  checkoutTab;
    private HistoryTab   historyTab;

    // Content area that swaps panels
    private StackPane contentArea;

    // Keep track of active nav button
    private VBox activeNavBtn = null;//Which sidebar button is currently selected

    @Override
    public void start(Stage primaryStage) {
        hotelService = new HotelService();

        dashboardTab = new DashboardTab(hotelService);
        roomsTab     = new RoomsTab(hotelService);
        bookingTab   = new BookingTab(hotelService, this::refreshAll);
        checkoutTab  = new CheckoutTab(hotelService, this::refreshAll);
        historyTab   = new HistoryTab(hotelService);

        // Build all panels upfront
        Node dashPanel     = dashboardTab.buildPanel();
        Node roomsPanel    = roomsTab.buildPanel();
        Node bookingPanel  = bookingTab.buildPanel();
        Node checkoutPanel = checkoutTab.buildPanel();
        Node historyPanel  = historyTab.buildPanel();

        contentArea = new StackPane(dashPanel);
        contentArea.setStyle("-fx-background-color: " + UIHelper.COLOR_BG + ";");

        // ── Sidebar ───────────────────────────────────────────────────────────
        VBox sidebar = buildSidebar(
            dashPanel, roomsPanel, bookingPanel, checkoutPanel, historyPanel);

        // ── Root layout ───────────────────────────────────────────────────────
        HBox root = new HBox(sidebar, contentArea);
        HBox.setHgrow(contentArea, Priority.ALWAYS);
        root.setStyle("-fx-background-color: " + UIHelper.COLOR_BG + ";");

        dashboardTab.refresh();
        startAutoSaveThread();

        Scene scene = new Scene(root, 1150, 720);
        primaryStage.setTitle("La Parisian — Hotel Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(950);
        primaryStage.setMinHeight(620);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> hotelService.saveAll());
    }

    private VBox buildSidebar(Node dash, Node rooms, Node booking,
                               Node checkout, Node history) {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(210);
        sidebar.setMinWidth(210);
        sidebar.setMaxWidth(210);
        sidebar.setStyle("-fx-background-color: " + UIHelper.COLOR_SIDEBAR + ";");

        // ── Hotel logo / name block ───────────────────────────────────────────
        VBox logoBlock = new VBox(4);
        logoBlock.setPadding(new Insets(28, 20, 22, 20));
        logoBlock.setAlignment(Pos.CENTER_LEFT);

        Label logoSymbol = new Label("✦");
        logoSymbol.setStyle("-fx-font-size: 22px; -fx-text-fill: " + UIHelper.COLOR_ACCENT + ";");

        Label hotelName = new Label("LA PARISIAN");
        hotelName.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 15px;" +
                           "-fx-font-weight: bold; -fx-text-fill: " + UIHelper.COLOR_TEXT_SIDEBAR + ";" +
                           "-fx-letter-spacing: 2;");

        Label hotelSub = new Label("Hotel Management");
        hotelSub.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 10px;" +
                          "-fx-font-style: italic; -fx-text-fill: " + UIHelper.COLOR_ACCENT + ";");

        logoBlock.getChildren().addAll(logoSymbol, hotelName, hotelSub);

        // Gold divider
        Region divider = new Region();
        divider.setPrefHeight(1);
        divider.setMaxWidth(Double.MAX_VALUE);
        divider.setStyle("-fx-background-color: " + UIHelper.COLOR_ACCENT + "; -fx-opacity: 0.4;");
        VBox.setMargin(divider, new Insets(0, 20, 0, 20));

        // ── Nav label ─────────────────────────────────────────────────────────
        Label navLabel = new Label("NAVIGATION");
        navLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: " + UIHelper.COLOR_ACCENT + ";" +
                          "-fx-font-weight: bold; -fx-letter-spacing: 2;");
        VBox.setMargin(navLabel, new Insets(18, 20, 6, 20));

        // ── Nav items ─────────────────────────────────────────────────────────
        VBox navDash     = navItem("⬡", "Dashboard",  dash);
        VBox navRooms    = navItem("▦", "Rooms",       rooms);
        VBox navBook     = navItem("◈", "Book Room",   booking);
        VBox navCheckout = navItem("◇", "Checkout",    checkout);
        VBox navHistory  = navItem("◉", "History",     history);

        // Set dashboard active by default
        setActive(navDash);

        navDash    .setOnMouseClicked(e -> { showPanel(dash);     setActive(navDash);     dashboardTab.refresh(); });
        navRooms   .setOnMouseClicked(e -> { showPanel(rooms);    setActive(navRooms);    roomsTab.refreshTable(); });
        navBook    .setOnMouseClicked(e -> { showPanel(booking);  setActive(navBook);     bookingTab.refreshRoomCombo(); });
        navCheckout.setOnMouseClicked(e -> { showPanel(checkout); setActive(navCheckout); checkoutTab.refreshTable(); });
        navHistory .setOnMouseClicked(e -> { showPanel(history);  setActive(navHistory);  historyTab.refresh(); });

        // ── Bottom version tag ────────────────────────────────────────────────
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Region bottomDiv = new Region();
        bottomDiv.setPrefHeight(1);
        bottomDiv.setMaxWidth(Double.MAX_VALUE);
        bottomDiv.setStyle("-fx-background-color: " + UIHelper.COLOR_ACCENT + "; -fx-opacity: 0.3;");

        Label version = new Label("v1.0  ·  OOSD Week 10");
        version.setStyle("-fx-font-size: 9px; -fx-text-fill: " + UIHelper.COLOR_ACCENT + ";" +
                         "-fx-font-style: italic;");
        VBox.setMargin(version, new Insets(10, 20, 16, 20));

        sidebar.getChildren().addAll(
            logoBlock, divider,
            navLabel,
            navDash, navRooms, navBook, navCheckout, navHistory,
            spacer
        );

        return sidebar;
    }

    private VBox navItem(String icon, String label, Node panel) {
        Label iconL  = new Label(icon);
        iconL.setStyle("-fx-font-size: 14px; -fx-text-fill: " + UIHelper.COLOR_ACCENT + ";");
        iconL.setMinWidth(20);

        Label labelL = new Label(label);
        labelL.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 13px;" +
                        "-fx-text-fill: " + UIHelper.COLOR_TEXT_SIDEBAR + ";");

        HBox row = new HBox(12, iconL, labelL);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox item = new VBox(row);
        item.setPadding(new Insets(11, 20, 11, 20));
        item.setStyle("-fx-cursor: hand;");
        item.setOnMouseEntered(e -> {
            if (item != activeNavBtn)
                item.setStyle("-fx-background-color: " + UIHelper.COLOR_SIDEBAR_ITEM +
                              "; -fx-cursor: hand;");
        });
        item.setOnMouseExited(e -> {
            if (item != activeNavBtn)
                item.setStyle("-fx-cursor: hand;");
        });
        return item;
    }

    private void setActive(VBox navBtn) {
        if (activeNavBtn != null) {
            activeNavBtn.setStyle("-fx-cursor: hand;");
        }
        activeNavBtn = navBtn;
        navBtn.setStyle(
            "-fx-background-color: " + UIHelper.COLOR_SIDEBAR_ITEM + ";" +
            "-fx-cursor: hand;" +
            "-fx-border-color: transparent transparent transparent " + UIHelper.COLOR_ACCENT + ";" +
            "-fx-border-width: 0 0 0 3;"
        );
    }

    private void showPanel(Node panel) {
        contentArea.getChildren().setAll(panel);
    }

    private void refreshAll() {
        dashboardTab.refresh();
        roomsTab.refreshTable();
        bookingTab.refreshRoomCombo();
        checkoutTab.refreshTable();
        historyTab.refresh();
    }

    /** Week 3: Background auto-save daemon thread */
    private void startAutoSaveThread() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60_000);
                    hotelService.saveAll();
                    System.out.println("[AutoSave] Saved.");
                } catch (InterruptedException e) { break; }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static void main(String[] args) { launch(args); }
}
