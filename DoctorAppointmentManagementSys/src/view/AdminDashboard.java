package view;

import model.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AdminDashboard {

    public void start(Stage stage) {

        HBox navbar = PatientDashboard.buildNavbar("Admin Panel", stage);

        VBox welcomeCard = new VBox(6);
        welcomeCard.setPadding(new Insets(24));
        welcomeCard.setStyle("-fx-background-color: #7C3AED; -fx-background-radius: 12;");
        Label welcome = new Label("Welcome, " + Session.userName + " 🔧");
        welcome.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label sub = new Label("Manage the MediCare platform");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.8);");
        welcomeCard.getChildren().addAll(welcome, sub);

        VBox docCard = actionCard("👨‍⚕️", "Manage Doctors", "Add, edit or remove doctors");
        VBox patCard = actionCard("🧑", "Manage Patients", "View and manage patient records");

        docCard.setOnMouseClicked(e -> showAlert("Coming Soon", "Doctor management coming soon!"));
        patCard.setOnMouseClicked(e -> showAlert("Coming Soon", "Patient management coming soon!"));

        HBox cards = new HBox(16, docCard, patCard);
        cards.setAlignment(Pos.CENTER);

        VBox root = new VBox(24, navbar, welcomeCard, cards);
        root.setPadding(new Insets(0, 32, 32, 32));
        root.setStyle(Styles.SCENE_BG);

        stage.setScene(new Scene(root, 620, 400));
        stage.setTitle("MediCare — Admin Dashboard");
        stage.show();
    }

    private VBox actionCard(String icon, String title, String desc) {
        Label i = new Label(icon); i.setStyle("-fx-font-size: 32px;");
        Label t = new Label(title); t.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill:" + Styles.TEXT_DARK + ";");
        Label d = new Label(desc);  d.setStyle("-fx-font-size: 12px; -fx-text-fill:" + Styles.TEXT_MUTED + ";");
        d.setWrapText(true);
        VBox card = new VBox(10, i, t, d);
        card.setPrefWidth(200); card.setPrefHeight(150);
        card.setPadding(new Insets(20));
        card.setStyle(Styles.CARD + " -fx-cursor: hand;");
        return card;
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setContentText(msg); a.showAndWait();
    }
}