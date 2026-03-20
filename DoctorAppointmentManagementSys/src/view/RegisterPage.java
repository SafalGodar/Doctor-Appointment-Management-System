package view;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterPage {

    public void start(Stage stage) {

        AuthController auth = new AuthController();

        // ── Left panel ───────────────────────────────────────────
        VBox leftPanel = new VBox(20);
        leftPanel.setMinWidth(300);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setStyle("-fx-background-color: " + Styles.PRIMARY + ";");

        Label icon    = new Label("📝");
        icon.setStyle("-fx-font-size: 56px;");
        Label title   = new Label("Join MediCare");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        Label tagline = new Label("Create your free account");
        tagline.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.75);");

        Label s1 = new Label("✔  Register as Patient, Doctor or Admin");
        Label s2 = new Label("✔  Book & manage appointments");
        Label s3 = new Label("✔  Secure & private");
        for (Label s : new Label[]{s1, s2, s3})
            s.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.85);");
        VBox steps = new VBox(8, s1, s2, s3);
        steps.setPadding(new Insets(20, 40, 0, 40));

        leftPanel.getChildren().addAll(icon, title, tagline, steps);

        // ── Right form ───────────────────────────────────────────
        VBox form = new VBox(18);
        form.setPadding(new Insets(50, 60, 50, 60));
        form.setAlignment(Pos.TOP_LEFT);
        form.setStyle("-fx-background-color: " + Styles.BG + ";");

        Label heading = new Label("Create Your Account");
        heading.setStyle(Styles.HEADING);
        Label sub = new Label("Fill in the details below to register");
        sub.setStyle(Styles.SUB_HEADING);

        // Role
        Label roleLabel = new Label("Register as");
        roleLabel.setStyle(Styles.LABEL);
        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton patientBtn = roleToggle("🧑 Patient", roleGroup);
        ToggleButton doctorBtn  = roleToggle("👨‍⚕️ Doctor",  roleGroup);
        ToggleButton adminBtn   = roleToggle("🔧 Admin",   roleGroup);
        patientBtn.setSelected(true);
        HBox roleBox = new HBox(10, patientBtn, doctorBtn, adminBtn);

        // Fields
        TextField name    = field("Full Name");
        TextField age     = field("Age");
        TextField contact = field("Contact Number (10 digits)");
        TextField address = field("Address (optional)");
        TextField email   = field("Email Address");
        PasswordField pass = new PasswordField();
        pass.setPromptText("Create a password");
        pass.setStyle(Styles.TEXT_FIELD);
        pass.setMaxWidth(Double.MAX_VALUE);

        // Two-column layout
        VBox leftCol = new VBox(10,
            lbl("Full Name"),    name,
            lbl("Age"),          age,
            lbl("Contact"),      contact
        );
        VBox rightCol = new VBox(10,
            lbl("Address"),      address,
            lbl("Email"),        email,
            lbl("Password"),     pass
        );
        HBox.setHgrow(leftCol,  Priority.ALWAYS);
        HBox.setHgrow(rightCol, Priority.ALWAYS);
        HBox columns = new HBox(24, leftCol, rightCol);

        // Error label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: " + Styles.DANGER + "; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);

        // Buttons
        Button registerBtn = new Button("Create Account  →");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setPrefHeight(46);
        Styles.addHover(registerBtn,
            Styles.greenButton(),
            Styles.greenButton().replace(Styles.SUCCESS, "#15803D")
        );

        // Back link
        HBox backRow = new HBox(8);
        backRow.setAlignment(Pos.CENTER);
        Label backLabel = new Label("Already have an account?");
        backLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: " + Styles.TEXT_MUTED + ";");
        Button backBtn = new Button("Sign In");
        backBtn.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + Styles.PRIMARY + ";" +
            "-fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"
        );
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + Styles.PRIMARY_DARK + ";" +
            "-fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand;" +
            "-fx-underline: true; -fx-padding: 0;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + Styles.PRIMARY + ";" +
            "-fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 0;"
        ));
        backRow.getChildren().addAll(backLabel, backBtn);

        // ── Assemble ─────────────────────────────────────────────
        form.getChildren().addAll(
            heading, sub,
            new VBox(6, roleLabel, roleBox),
            columns,
            errorLabel,
            registerBtn,
            new Separator(),
            backRow
        );

        // Actions
        registerBtn.setOnAction(e -> {
            errorLabel.setText("");
            String cleanRole = extractRole(((ToggleButton) roleGroup.getSelectedToggle()).getText());

            if (name.getText().isBlank() || age.getText().isBlank() ||
                contact.getText().isBlank() || email.getText().isBlank() ||
                pass.getText().isBlank()) {
                errorLabel.setText("⚠  All fields except Address are required.");
                return;
            }
            if (!age.getText().matches("\\d+")) {
                errorLabel.setText("⚠  Age must be a number.");
                return;
            }
            if (!contact.getText().matches("\\d{10}")) {
                errorLabel.setText("⚠  Contact must be exactly 10 digits.");
                return;
            }
            boolean ok = auth.register(
                name.getText().trim(), age.getText().trim(),
                contact.getText().trim(), address.getText().trim(),
                email.getText().trim(), pass.getText(), cleanRole
            );
            if (ok) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Welcome to MediCare!");
                a.setContentText("Your account has been created. Please sign in.");
                a.showAndWait();
                new LoginPage().start(stage);
            } else {
                errorLabel.setText("⚠  Registration failed. That email may already be registered.");
            }
        });

        backBtn.setOnAction(e -> new LoginPage().start(stage));

        // ── Scroll wrapper ───────────────────────────────────────
        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: " + Styles.BG + "; -fx-background: " + Styles.BG + ";");

        HBox root = new HBox(leftPanel, scroll);
        HBox.setHgrow(scroll, Priority.ALWAYS);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);              // ← full desktop window
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setTitle("MediCare — Register");
        stage.show();
    }

    private TextField field(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(Styles.TEXT_FIELD);
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private Label lbl(String text) {
        Label l = new Label(text);
        l.setStyle(Styles.LABEL);
        return l;
    }

    private ToggleButton roleToggle(String text, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        String base =
            "-fx-background-color: white; -fx-border-color: " + Styles.BORDER + ";" +
            "-fx-border-radius: 8; -fx-background-radius: 8;" +
            "-fx-padding: 9 18; -fx-cursor: hand; -fx-font-size: 13px;";
        String selected =
            "-fx-background-color: " + Styles.PRIMARY + "; -fx-text-fill: white;" +
            "-fx-border-color: " + Styles.PRIMARY + "; -fx-border-radius: 8;" +
            "-fx-background-radius: 8; -fx-padding: 9 18; -fx-cursor: hand; -fx-font-size: 13px;";
        btn.setStyle(base);
        btn.selectedProperty().addListener((obs, was, is) -> btn.setStyle(is ? selected : base));
        return btn;
    }

    private String extractRole(String text) {
        return text.replaceAll("[^a-zA-Z ]", "").trim();
    }
}