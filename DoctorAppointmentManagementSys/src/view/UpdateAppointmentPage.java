package view;

import model.Appointment;
import model.DBConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class UpdateAppointmentPage {

    private final Appointment appointment;

    public UpdateAppointmentPage(Appointment appointment) {
        this.appointment = appointment;
    }

    public void start(Stage stage) {

        HBox navbar = PatientDashboard.buildNavbar("Update Appointment", stage);

        Label heading = new Label("Update Appointment");
        heading.setStyle(Styles.HEADING);
        Label sub = new Label("Modify your scheduled visit");
        sub.setStyle(Styles.SUB_HEADING);

        // Pre-fill fields
        DatePicker datePicker = new DatePicker();
        try { datePicker.setValue(LocalDate.parse(appointment.getDate())); } catch (Exception ignored) {}
        datePicker.setStyle(Styles.DATE_PICKER);
        datePicker.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> timeCombo = new ComboBox<>();
        timeCombo.getItems().addAll("09:00","10:00","11:00","12:00","14:00","15:00","16:00");
        timeCombo.setValue(appointment.getTime());
        timeCombo.setStyle(Styles.COMBO_BOX);
        timeCombo.setMaxWidth(Double.MAX_VALUE);

        TextArea reason = new TextArea(appointment.getReason());
        reason.setPrefRowCount(3);
        reason.setStyle(Styles.TEXT_AREA);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill:" + Styles.DANGER + "; -fx-font-size:12px;");

        Button updateBtn = new Button("💾  Save Changes");
        Styles.addHover(updateBtn, Styles.primaryButton(),
            Styles.primaryButton().replace(Styles.PRIMARY, Styles.PRIMARY_DARK));

        Button cancelBtn = new Button("Cancel");
        Styles.addHover(cancelBtn, Styles.outlineButton(), Styles.outlineButton());

        updateBtn.setOnAction(e -> {
            errorLabel.setText("");
            if (datePicker.getValue() == null || timeCombo.getValue() == null) {
                errorLabel.setText("⚠  Date and time are required.");
                return;
            }
            updateAppointment(
                appointment.getId(),
                datePicker.getValue().toString(),
                timeCombo.getValue(),
                reason.getText()
            );
            showAlert("Updated", "Appointment updated successfully!");
            new ViewAppointmentsPage().start(stage);
        });

        cancelBtn.setOnAction(e -> new ViewAppointmentsPage().start(stage));

        HBox buttons = new HBox(12, cancelBtn, updateBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox leftCol = new VBox(10, lbl("Date"), datePicker, lbl("Time"), timeCombo);
        VBox rightCol = new VBox(10, lbl("Reason for Visit"), reason);
        HBox cols = new HBox(20, leftCol, rightCol);
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        HBox.setHgrow(rightCol, Priority.ALWAYS);

        // Appointment info chip
        HBox infoChip = new HBox(8);
        infoChip.setPadding(new Insets(12, 16, 12, 16));
        infoChip.setStyle(
            "-fx-background-color: " + Styles.PRIMARY_LIGHT + ";" +
            "-fx-background-radius: 8;"
        );
        Label chipLabel = new Label("📋 Appointment with Dr. " + appointment.getDoctor());
        chipLabel.setStyle("-fx-font-size:13px; -fx-text-fill:" + Styles.PRIMARY + "; -fx-font-weight:bold;");
        infoChip.getChildren().add(chipLabel);

        VBox card = new VBox(20, heading, sub, infoChip, cols, errorLabel, buttons);
        card.setPadding(new Insets(32));
        card.setStyle(Styles.CARD);

        VBox content = new VBox(card);
        content.setPadding(new Insets(24, 32, 32, 32));
        content.setStyle(Styles.SCENE_BG);

        VBox root = new VBox(navbar, content);
        root.setStyle(Styles.SCENE_BG);

        stage.setScene(new Scene(root, 680, 500));
        stage.setTitle("MediCare — Update Appointment");
        stage.show();
    }

    private void updateAppointment(int id, String date, String time, String reason) {
        DBConnection db = new DBConnection();
        try (Connection conn = db.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE appointment SET date=?, time=?, reason=? WHERE id=?");
            ps.setString(1, date);
            ps.setString(2, time);
            ps.setString(3, reason);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Label lbl(String text) {
        Label l = new Label(text);
        l.setStyle(Styles.LABEL);
        return l;
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title); a.setContentText(msg); a.showAndWait();
    }
}