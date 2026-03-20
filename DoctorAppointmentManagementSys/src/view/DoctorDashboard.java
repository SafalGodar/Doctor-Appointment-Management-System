package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DoctorDashboard {

    public void start(Stage stage) {

        Label title = new Label("Doctor Dashboard");

        Button appointments = new Button("My Appointments");
        Button logout = new Button("Logout");

        logout.setOnAction(e -> new LoginPage().start(stage));

        VBox layout = new VBox(20, title, appointments, logout);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 500, 400));
        stage.setTitle("Doctor Dashboard");
    }
}