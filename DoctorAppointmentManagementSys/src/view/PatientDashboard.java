package view;

import model.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class PatientDashboard {

    public void start(Stage stage) {

        // ── Top nav bar ─────────────────────────────────────────
        HBox navbar = buildNavbar("Patient Portal", stage);

        // ── Welcome card ─────────────────────────────────────────
        VBox welcomeCard = new VBox(6);
        welcomeCard.setPadding(new Insets(24));
        welcomeCard.setStyle(
            "-fx-background-color: " + Styles.PRIMARY + ";" +
            "-fx-background-radius: 12;"
        );
        Label welcome = new Label("Hello, " + Session.userName + " 👋");
        welcome.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label welcomeSub = new Label("Manage your appointments easily");
        welcomeSub.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.8);");
        welcomeCard.getChildren().addAll(welcome, welcomeSub);

        // ── Action cards ────────────────────────────────────────
        VBox bookCard   = actionCard("📅", "Book Appointment",
                "Schedule a new appointment with a doctor");
        VBox viewCard   = actionCard("📋", "View Appointments",
                "See and manage your upcoming visits");

        bookCard.setOnMouseClicked(e -> new BookAppointmentPage().start(stage));
        viewCard.setOnMouseClicked(e -> new ViewAppointmentsPage().start(stage));

        HBox cards = new HBox(16, bookCard, viewCard);
        cards.setAlignment(Pos.CENTER);

        // ── Root ────────────────────────────────────────────────
        VBox root = new VBox(24, navbar, welcomeCard, cards);
        root.setPadding(new Insets(0, 32, 32, 32));
        root.setStyle(Styles.SCENE_BG);

        stage.setScene(new Scene(root, 620, 420));
        stage.setTitle("MediCare — Patient Dashboard");
        stage.show();
    }

    private VBox actionCard(String icon, String title, String desc) {
        Label iconLabel  = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 32px;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");
        Label descLabel  = new Label(desc);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_MUTED + ";");
        descLabel.setWrapText(true);

        VBox card = new VBox(10, iconLabel, titleLabel, descLabel);
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle(Styles.CARD + " -fx-cursor: hand;");

        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: " + Styles.PRIMARY_LIGHT + ";" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian,rgba(37,99,235,0.18),16,0,0,6);" +
            "-fx-cursor: hand;"
        ));
        card.setOnMouseExited(e -> card.setStyle(Styles.CARD + " -fx-cursor: hand;"));

        return card;
    }

    static HBox buildNavbar(String title, Stage stage) {
        Label logo = new Label("🏥 MediCare");
        logo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + Styles.PRIMARY + ";");

        Label pageTitle = new Label(title);
        pageTitle.setStyle("-fx-font-size: 14px; -fx-text-fill: " + Styles.TEXT_MUTED + ";");

        Button logoutBtn = new Button("Logout");
        Styles.addHover(logoutBtn, Styles.outlineButton(),
            "-fx-background-color: #FEF2F2; -fx-border-color: " + Styles.DANGER + ";" +
            "-fx-border-radius: 8; -fx-background-radius: 8;" +
            "-fx-text-fill: " + Styles.DANGER + "; -fx-font-size: 13px;" +
            "-fx-padding: 10 24; -fx-cursor: hand;"
        );
        logoutBtn.setOnAction(e -> new LoginPage().start(stage));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox nav = new HBox(12, logo, pageTitle, spacer, logoutBtn);
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setPadding(new Insets(16, 32, 16, 32));
        nav.setStyle(
            "-fx-background-color: white;" +
            "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.06),8,0,0,2);"
        );
        return nav;
    }
}