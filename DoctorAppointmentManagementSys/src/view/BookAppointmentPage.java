package view;

import model.DBConnection;
import model.Session;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class BookAppointmentPage {

    private ComboBox<Doctor> doctorCombo;

    public void start(Stage stage) {

        HBox navbar = PatientDashboard.buildNavbar("Book Appointment", stage);

        // ── Form card ────────────────────────────────────────────
        Label heading = new Label("Schedule New Appointment");
        heading.setStyle(Styles.HEADING);
        Label sub = new Label("Fill in the details below");
        sub.setStyle(Styles.SUB_HEADING);

        doctorCombo = new ComboBox<>();
        doctorCombo.setPromptText("Select a doctor");
        doctorCombo.setStyle(Styles.COMBO_BOX);
        doctorCombo.setMaxWidth(Double.MAX_VALUE);
        loadDoctors();

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date");
        datePicker.setStyle(Styles.DATE_PICKER);
        datePicker.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> timeCombo = new ComboBox<>();
        timeCombo.setPromptText("Select time slot");
        timeCombo.setStyle(Styles.COMBO_BOX);
        timeCombo.setMaxWidth(Double.MAX_VALUE);
        timeCombo.getItems().addAll("09:00", "10:00", "11:00", "12:00", "14:00", "15:00", "16:00");

        TextArea reason = new TextArea();
        reason.setPromptText("Reason for visit (optional)");
        reason.setPrefRowCount(3);
        reason.setStyle(Styles.TEXT_AREA);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill:" + Styles.DANGER + "; -fx-font-size:12px;");

        Button confirmBtn = new Button("✅  Confirm Booking");
        Styles.addHover(confirmBtn, Styles.primaryButton(),
            Styles.primaryButton().replace(Styles.PRIMARY, Styles.PRIMARY_DARK));

        Button cancelBtn = new Button("Cancel");
        Styles.addHover(cancelBtn, Styles.outlineButton(), Styles.outlineButton());

        confirmBtn.setOnAction(e -> {
            errorLabel.setText("");
            Doctor doc = doctorCombo.getValue();
            String date = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
            String time = timeCombo.getValue();

            if (doc == null || date.isEmpty() || time == null) {
                errorLabel.setText("⚠  Please select doctor, date and time.");
                return;
            }
            saveAppointment(doc.getId(), date, time, reason.getText());
            showAlert("Success", "Appointment booked successfully!");
            new PatientDashboard().start(stage);
        });

        cancelBtn.setOnAction(e -> new PatientDashboard().start(stage));

        HBox buttons = new HBox(12, cancelBtn, confirmBtn);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        // ── Left / right columns ────────────────────────────────
        VBox leftCol = new VBox(10,
            lbl("Doctor"), doctorCombo,
            lbl("Date"), datePicker
        );
        VBox rightCol = new VBox(10,
            lbl("Time Slot"), timeCombo,
            lbl("Reason"), reason
        );
        HBox cols = new HBox(20, leftCol, rightCol);
        HBox.setHgrow(leftCol, Priority.ALWAYS);
        HBox.setHgrow(rightCol, Priority.ALWAYS);

        VBox card = new VBox(20, heading, sub, cols, errorLabel, buttons);
        card.setPadding(new Insets(32));
        card.setStyle(Styles.CARD);

        VBox contentArea = new VBox(card);
        contentArea.setPadding(new Insets(24, 32, 32, 32));
        contentArea.setStyle(Styles.SCENE_BG);

        VBox root = new VBox(navbar, contentArea);
        VBox.setVgrow(contentArea, Priority.ALWAYS);
        root.setStyle(Styles.SCENE_BG);

        stage.setScene(new Scene(root, 680, 520));
        stage.setTitle("MediCare — Book Appointment");
        stage.show();
    }

    private void loadDoctors() {
        ObservableList<Doctor> list = FXCollections.observableArrayList();
        DBConnection db = new DBConnection();
        try (Connection conn = db.getConnection()) {
            ResultSet rs = conn.createStatement()
                               .executeQuery("SELECT id, name, specialty FROM doctor");
            while (rs.next())
                list.add(new Doctor(rs.getInt("id"), rs.getString("name"), rs.getString("specialty")));
            doctorCombo.setItems(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAppointment(int doctorId, String date, String time, String reason) {
        DBConnection db = new DBConnection();
        try (Connection conn = db.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO appointment (patient_id, doctor_id, date, time, reason, status) VALUES (?,?,?,?,?,?)");
            ps.setInt(1, Session.userId);
            ps.setInt(2, doctorId);
            ps.setString(3, date);
            ps.setString(4, time);
            ps.setString(5, reason);
            ps.setString(6, "Pending");
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

    public static class Doctor {
        private final int id;
        private final String name;
        private final String specialty;

        public Doctor(int id, String name, String specialty) {
            this.id = id; this.name = name; this.specialty = specialty;
        }

        public int getId() { return id; }

        @Override
        public String toString() { return name + "  —  " + specialty; }
    }
}