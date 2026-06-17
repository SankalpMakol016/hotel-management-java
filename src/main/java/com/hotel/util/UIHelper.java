package com.hotel.util;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Optional;

/**
 * UIHelper — Art Deco Hospitality theme.
 * Deep forest green sidebar + ivory content + brass gold accents.
 */
public class UIHelper {

    // ── Art Deco Palette — Warm Brown + Ivory ────────────────────────────────
    public static final String COLOR_BG           = "#F7F3EC";  // warm ivory
    public static final String COLOR_SIDEBAR      = "#3E2410";  // deep espresso brown
    public static final String COLOR_SIDEBAR_ITEM = "#5C3418";  // lighter warm brown hover
    public static final String COLOR_CARD         = "#FFFFFF";  // white cards
    public static final String COLOR_ACCENT       = "#B8962E";  // brass gold
    public static final String COLOR_ACCENT2      = "#D4AF50";  // lighter gold
    public static final String COLOR_TEXT         = "#1A1A1A";  // near-black
    public static final String COLOR_TEXT_MUTED   = "#7A7060";  // warm grey
    public static final String COLOR_TEXT_SIDEBAR = "#F0E6D3";  // warm cream on brown
    public static final String COLOR_SUCCESS      = "#2E7D4F";  // forest green
    public static final String COLOR_DANGER       = "#A93226";  // deep red
    public static final String COLOR_WARNING      = "#B7770D";  // gold-amber
    public static final String COLOR_BORDER       = "#D9D0BC";  // warm tan
    public static final String COLOR_INPUT_BG     = "#FDFAF5";  // cream input

    // ── Text styles ───────────────────────────────────────────────────────────
    public static final String STYLE_LABEL_HEADER =
        "-fx-font-family: 'Georgia'; -fx-font-size: 20px; -fx-font-weight: bold;" +
        "-fx-text-fill: " + COLOR_SIDEBAR + ";";

    public static final String STYLE_LABEL_SUBHEADER =
        "-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-font-style: italic;" +
        "-fx-text-fill: " + COLOR_ACCENT + ";";

    public static final String STYLE_LABEL_BODY =
        "-fx-font-family: 'Georgia'; -fx-font-size: 13px;" +
        "-fx-text-fill: " + COLOR_TEXT + ";";

    public static final String STYLE_LABEL_MUTED =
        "-fx-font-size: 11px; -fx-text-fill: " + COLOR_TEXT_MUTED + ";";

    // ── Input styles ──────────────────────────────────────────────────────────
    public static final String STYLE_TEXT_FIELD =
        "-fx-background-color: " + COLOR_INPUT_BG + ";" +
        "-fx-text-fill: " + COLOR_TEXT + ";" +
        "-fx-border-color: " + COLOR_BORDER + ";" +
        "-fx-border-radius: 4; -fx-background-radius: 4;" +
        "-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-padding: 8 12;";

    public static final String STYLE_COMBO =
        "-fx-background-color: " + COLOR_INPUT_BG + ";" +
        "-fx-text-fill: " + COLOR_TEXT + ";" +
        "-fx-border-color: " + COLOR_BORDER + ";" +
        "-fx-border-radius: 4; -fx-background-radius: 4;" +
        "-fx-font-size: 13px;";

    // ── Component styles ──────────────────────────────────────────────────────
    public static final String STYLE_TABLE =
        "-fx-background-color: " + COLOR_CARD + ";" +
        "-fx-text-fill: " + COLOR_TEXT + ";" +
        "-fx-font-family: 'Georgia'; -fx-font-size: 13px;" +
        "-fx-table-cell-border-color: " + COLOR_BORDER + ";" +
        "-fx-border-color: " + COLOR_BORDER + "; -fx-border-width: 1;";

    public static final String STYLE_CARD =
        "-fx-background-color: " + COLOR_CARD + ";" +
        "-fx-background-radius: 6;" +
        "-fx-border-color: " + COLOR_BORDER + ";" +
        "-fx-border-radius: 6;" +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 8, 0, 0, 2);";

    // ── Button styles ─────────────────────────────────────────────────────────
    public static final String STYLE_BTN_PRIMARY =
        "-fx-background-color: " + COLOR_SIDEBAR + ";" +
        "-fx-text-fill: " + COLOR_TEXT_SIDEBAR + "; -fx-font-weight: bold;" +
        "-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-cursor: hand;" +
        "-fx-background-radius: 4; -fx-padding: 9 22;" +
        "-fx-border-color: " + COLOR_ACCENT + "; -fx-border-radius: 4; -fx-border-width: 1;";

    public static final String STYLE_BTN_GOLD =
        "-fx-background-color: " + COLOR_ACCENT + ";" +
        "-fx-text-fill: #FFFFFF; -fx-font-weight: bold;" +
        "-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-cursor: hand;" +
        "-fx-background-radius: 4; -fx-padding: 9 22;";

    public static final String STYLE_BTN_DANGER =
        "-fx-background-color: " + COLOR_DANGER + ";" +
        "-fx-text-fill: white; -fx-font-weight: bold;" +
        "-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-cursor: hand;" +
        "-fx-background-radius: 4; -fx-padding: 9 22;";

    public static final String STYLE_BTN_NEUTRAL =
        "-fx-background-color: " + COLOR_BG + ";" +
        "-fx-text-fill: " + COLOR_TEXT + "; -fx-font-size: 13px;" +
        "-fx-cursor: hand; -fx-background-radius: 4; -fx-padding: 9 22;" +
        "-fx-border-color: " + COLOR_BORDER + "; -fx-border-radius: 4; -fx-border-width: 1;";

    /**
     * Returns inline CSS to apply to a TableView via setStyle().
     * Enables visible grid lines and fixes selection color so rows don't disappear.
     */
    public static String tableCSS() {
        return "-fx-background-color: " + COLOR_CARD + ";" +
               "-fx-text-fill: " + COLOR_TEXT + ";" +
               "-fx-font-family: 'Georgia'; -fx-font-size: 13px;" +
               "-fx-table-cell-border-color: " + COLOR_BORDER + ";" +
               "-fx-border-color: " + COLOR_BORDER + "; -fx-border-width: 1;";
    }

    /**
     * Row style: normal, occupied/checkedOut variant, and selected state.
     * Pass selected=true when the row is selected so text stays visible.
     */
    public static String rowStyle(String bgColor, boolean selected) {
        if (selected) {
            return "-fx-background-color: " + COLOR_ACCENT + ";" +
                   "-fx-text-fill: #FFFFFF;";
        }
        return "-fx-background-color: " + bgColor + ";" +
               "-fx-text-fill: " + COLOR_TEXT + ";" +
               "-fx-border-color: transparent transparent " + COLOR_BORDER + " transparent;" +
               "-fx-border-width: 0 0 1 0;";
    }

    public static Label sectionTitle(String text) {
        Label l = new Label(text);
        l.setStyle(STYLE_LABEL_HEADER);
        return l;
    }

    public static Label subTitle(String text) {
        Label l = new Label(text);
        l.setStyle(STYLE_LABEL_SUBHEADER);
        return l;
    }

    public static Label bodyLabel(String text) {
        Label l = new Label(text);
        l.setStyle(STYLE_LABEL_BODY);
        return l;
    }

    /** Brass gold gradient divider */
    public static Region goldDivider() {
        Region r = new Region();
        r.setPrefHeight(2);
        r.setMaxWidth(Double.MAX_VALUE);
        r.setStyle("-fx-background-color: linear-gradient(to right, transparent, " +
                   COLOR_ACCENT + ", transparent);");
        return r;
    }

    public static TextField styledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(STYLE_TEXT_FIELD);
        return tf;
    }

    public static Button primaryButton(String text) {
        Button b = new Button(text);
        b.setStyle(STYLE_BTN_PRIMARY);
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited(e  -> b.setOpacity(1.0));
        return b;
    }

    public static Button goldButton(String text) {
        Button b = new Button(text);
        b.setStyle(STYLE_BTN_GOLD);
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited(e  -> b.setOpacity(1.0));
        return b;
    }

    public static Button dangerButton(String text) {
        Button b = new Button(text);
        b.setStyle(STYLE_BTN_DANGER);
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited(e  -> b.setOpacity(1.0));
        return b;
    }

    public static Button neutralButton(String text) {
        Button b = new Button(text);
        b.setStyle(STYLE_BTN_NEUTRAL);
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited(e  -> b.setOpacity(1.0));
        return b;
    }

    public static VBox card(double padding) {
        VBox v = new VBox(10);
        v.setPadding(new Insets(padding));
        v.setStyle(STYLE_CARD);
        return v;
    }

    // ── Alerts ────────────────────────────────────────────────────────────────

    public static void showInfo(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(message);
        styleAlert(a); a.showAndWait();
    }

    public static void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(message);
        styleAlert(a); a.showAndWait();
    }

    public static boolean showConfirm(String title, String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title); a.setHeaderText(null); a.setContentText(message);
        styleAlert(a);
        Optional<ButtonType> result = a.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void showBillDialog(String bill) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Invoice / Receipt");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        TextArea ta = new TextArea(bill);
        ta.setEditable(false);
        ta.setFont(Font.font("Monospaced", 13));
        ta.setStyle("-fx-control-inner-background: " + COLOR_INPUT_BG + ";" +
                    "-fx-text-fill: " + COLOR_TEXT + ";");
        ta.setPrefSize(460, 420);
        dialog.getDialogPane().setContent(ta);
        dialog.getDialogPane().setStyle("-fx-background-color: " + COLOR_BG + ";");
        dialog.showAndWait();
    }

    private static void styleAlert(Alert a) {
        try {
            a.getDialogPane().setStyle(
                "-fx-background-color: " + COLOR_BG + ";" +
                "-fx-border-color: " + COLOR_ACCENT + "; -fx-border-width: 1;");
        } catch (Exception ignored) {}
    }
}
