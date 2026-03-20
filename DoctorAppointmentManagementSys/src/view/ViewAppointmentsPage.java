package view;

import model.Appointment;
import model.DBConnection;
import model.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class ViewAppointmentsPage {

    private TableView<Appointment> table = new TableView<>();

    public void start(Stage stage) {

        HBox navbar = PatientDashboard.buildNavbar("My Appointments", stage);

        Label heading = new Label("My Appointments");
        heading.setStyle(Styles.HEADING);

        // ── Table styling ───────────────────────────────────────
        table.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + Styles.BORDER + ";" +
            "-fx-border-radius: 8; -fx-background-radius: 8;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No appointments found."));

        table.getColumns().addAll(
            strCol("Doctor",  a -> a.getDoctor()),
            strCol("Date",    a -> a.getDate()),
            strCol("Time",    a -> a.getTime()),
            strCol("Reason",  a -> a.getReason()),
            strCol("Status",  a -> a.getStatus()),
            actionCol("Edit",   stage, false),
            actionCol("Cancel", stage, true)
        );

        loadAppointments();

        Button backBtn = new Button("← Back");
        Styles.addHover(backBtn, Styles.outlineButton(), Styles.outlineButton());
        backBtn.setOnAction(e -> new PatientDashboard().start(stage));

        VBox card = new VBox(16, heading, table);
        card.setPadding(new Insets(24));
        card.setStyle(Styles.CARD);
        VBox.setVgrow(table, Priority.ALWAYS);

        VBox content = new VBox(24, card, backBtn);
        content.setPadding(new Insets(24, 32, 32, 32));
        content.setStyle(Styles.SCENE_BG);
        VBox.setVgrow(card, Priority.ALWAYS);

        VBox root = new VBox(navbar, content);
        VBox.setVgrow(content, Priority.ALWAYS);
        root.setStyle(Styles.SCENE_BG);

        stage.setScene(new Scene(root, 820, 520));
        stage.setTitle("MediCare — Appointments");
        stage.show();
    }

    @SuppressWarnings("unchecked")
    private TableColumn<Appointment, String> strCol(String name,
            java.util.function.Function<Appointment, String> getter) {
        TableColumn<Appointment, String> col = new TableColumn<>(name);
        col.setCellValueFactory(data -> new SimpleStringProperty(getter.apply(data.getValue())));
        return col;
    }

    private TableColumn<Appointment, Void> actionCol(String label, Stage stage, boolean isDelete) {
        TableColumn<Appointment, Void> col = new TableColumn<>(label);
        col.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button(isDelete ? "🗑 Cancel" : "✏ Edit");
            {
                if (isDelete)
                    Styles.addHover(btn, Styles.dangerButton(),
                        Styles.dangerButton().replace(Styles.DANGER, Styles.DANGER_DARK));
                else
                    Styles.addHover(btn, Styles.primaryButton(),
                        Styles.primaryButton().replace(Styles.PRIMARY, Styles.PRIMARY_DARK));

                btn.setOnAction(e -> {
                    Appointment appt = getTableView().getItems().get(getIndex());
                    if (isDelete) {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "Cancel this appointment?", ButtonType.YES, ButtonType.NO);
                        confirm.showAndWait().ifPresent(r -> {
                            if (r == ButtonType.YES) {
                                deleteAppointment(appt.getId());
                                loadAppointments();
                            }
                        });
                    } else {
                        new UpdateAppointmentPage(appt).start(stage);
                    }
                });
            }

            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
                setAlignment(Pos.CENTER);
            }
        });
        return col;
    }

    private void loadAppointments() {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        DBConnection db = new DBConnection();
        try (Connection conn = db.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT a.id, d.name as doctor, a.date, a.time, a.reason, a.status " +
                "FROM appointment a JOIN doctor d ON a.doctor_id = d.id " +
                "WHERE a.patient_id = ?");
            ps.setInt(1, Session.userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                list.add(new Appointment(
                    rs.getInt("id"),
                    rs.getString("doctor"),
                    rs.getString("date"),
                    rs.getString("time"),
                    rs.getString("reason"),
                    rs.getString("status")
                ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        table.setItems(list);
    }

    private void deleteAppointment(int id) {
        DBConnection db = new DBConnection();
        try (Connection conn = db.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM appointment WHERE id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}