package view;

public class Styles {

    // ── Colour palette ──────────────────────────────────────────
    public static final String PRIMARY        = "#2563EB";   // blue
    public static final String PRIMARY_DARK   = "#1D4ED8";
    public static final String PRIMARY_LIGHT  = "#EFF6FF";
    public static final String DANGER         = "#DC2626";
    public static final String DANGER_DARK    = "#B91C1C";
    public static final String SUCCESS        = "#16A34A";
    public static final String NEUTRAL        = "#6B7280";
    public static final String BG             = "#F8FAFC";
    public static final String CARD_BG        = "#FFFFFF";
    public static final String TEXT_DARK      = "#1E293B";
    public static final String TEXT_MUTED     = "#64748B";
    public static final String BORDER         = "#E2E8F0";

    // ── Reusable inline CSS snippets ────────────────────────────
    public static final String SCENE_BG =
            "-fx-background-color: " + BG + ";";

    public static final String CARD =
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 12, 0, 0, 4);";

    public static final String HEADING =
            "-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_DARK + ";";

    public static final String SUB_HEADING =
            "-fx-font-size: 13px; -fx-text-fill: " + TEXT_MUTED + ";";

    public static final String LABEL =
            "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: " + TEXT_DARK + ";";

    public static final String TEXT_FIELD =
            "-fx-background-color: white;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 14;" +
            "-fx-font-size: 13px;" +
            "-fx-pref-height: 40;";

    public static final String COMBO_BOX =
            "-fx-background-color: white;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 13px;" +
            "-fx-pref-height: 40;";

    public static final String TEXT_AREA =
            "-fx-background-color: white;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 10 14;" +
            "-fx-font-size: 13px;";

    public static final String DATE_PICKER =
            "-fx-background-color: white;" +
            "-fx-border-color: " + BORDER + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-pref-height: 40;";

    public static String primaryButton() {
        return "-fx-background-color: " + PRIMARY + ";" +
               "-fx-text-fill: white;" +
               "-fx-font-size: 13px;" +
               "-fx-font-weight: bold;" +
               "-fx-background-radius: 8;" +
               "-fx-padding: 10 24;" +
               "-fx-cursor: hand;";
    }

    public static String dangerButton() {
        return "-fx-background-color: " + DANGER + ";" +
               "-fx-text-fill: white;" +
               "-fx-font-size: 13px;" +
               "-fx-font-weight: bold;" +
               "-fx-background-radius: 8;" +
               "-fx-padding: 10 24;" +
               "-fx-cursor: hand;";
    }

    public static String outlineButton() {
        return "-fx-background-color: white;" +
               "-fx-border-color: " + BORDER + ";" +
               "-fx-border-radius: 8;" +
               "-fx-background-radius: 8;" +
               "-fx-text-fill: " + TEXT_DARK + ";" +
               "-fx-font-size: 13px;" +
               "-fx-padding: 10 24;" +
               "-fx-cursor: hand;";
    }

    public static String greenButton() {
        return "-fx-background-color: " + SUCCESS + ";" +
               "-fx-text-fill: white;" +
               "-fx-font-size: 13px;" +
               "-fx-font-weight: bold;" +
               "-fx-background-radius: 8;" +
               "-fx-padding: 10 24;" +
               "-fx-cursor: hand;";
    }

    /** Adds a simple hover effect programmatically */
    public static void addHover(javafx.scene.control.Button btn, String normal, String hover) {
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
    }
}