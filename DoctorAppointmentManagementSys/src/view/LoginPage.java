package view;

import controller.AuthController;
import model.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginPage {

    public void start(Stage stage) {

        AuthController auth = new AuthController();

        // ── Left decorative panel ───────────────────────────────
        VBox leftPanel = new VBox(20);
        leftPanel.setMinWidth(320);
        leftPanel.setAlignment(Pos.CENTER);
        leftPanel.setStyle(
            "-fx-background-color: " + Styles.PRIMARY + ";"
        );

        Label appIcon    = new Label("🏥");
        appIcon.setStyle("-fx-font-size: 64px;");

        Label appName    = new Label("MediCare");
        appName.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label appTagline = new Label("Your Health, Our Priority");
        appTagline.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(255,255,255,0.75);");

        Label feature1   = new Label("✔  Book appointments online");
        Label feature2   = new Label("✔  Track your visit history");
        Label feature3   = new Label("✔  Trusted doctors & specialists");
        for (Label f : new Label[]{feature1, feature2, feature3})
            f.setStyle("-fx-font-size: 13px; -fx-text-fill: rgba(255,255,255,0.85);");

        VBox features = new VBox(8, feature1, feature2, feature3);
        features.setAlignment(Pos.CENTER_LEFT);
        features.setPadding(new Insets(20, 40, 0, 40));

        leftPanel.getChildren().addAll(appIcon, appName, appTagline, features);

        // ── Right form panel ────────────────────────────────────
        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox form = new VBox(18);
        form.setPadding(new Insets(60, 60, 60, 60));
        form.setAlignment(Pos.TOP_LEFT);
        form.setMaxWidth(520);
        form.setStyle("-fx-background-color: " + Styles.BG + ";");

        Label heading = new Label("Welcome Back 👋");
        heading.setStyle(Styles.HEADING);
        Label sub = new Label("Sign in to your account to continue");
        sub.setStyle(Styles.SUB_HEADING);

        // Role selector
        Label roleLabel = new Label("Sign in as");
        roleLabel.setStyle(Styles.LABEL);
        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton patientBtn = roleToggle("🧑 Patient", roleGroup);
        ToggleButton doctorBtn  = roleToggle("👨‍⚕️ Doctor",  roleGroup);
        ToggleButton adminBtn   = roleToggle("🔧 Admin",   roleGroup);
        patientBtn.setSelected(true);
        HBox roleBox = new HBox(10, patientBtn, doctorBtn, adminBtn);

        // Email
        Label emailLabel = new Label("Email Address");
        emailLabel.setStyle(Styles.LABEL);
        TextField email = new TextField();
        email.setPromptText("you@example.com");
        email.setStyle(Styles.TEXT_FIELD);
        email.setMaxWidth(Double.MAX_VALUE);

        // Password
        Label passLabel = new Label("Password");
        passLabel.setStyle(Styles.LABEL);
        PasswordField password = new PasswordField();
        password.setPromptText("Enter your password");
        password.setStyle(Styles.TEXT_FIELD);
        password.setMaxWidth(Double.MAX_VALUE);

        // Error
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: " + Styles.DANGER + "; -fx-font-size: 12px;");
        errorLabel.setWrapText(true);

        // Login button
        Button loginBtn = new Button("Sign In  →");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(46);
        Styles.addHover(loginBtn,
            Styles.primaryButton(),
            Styles.primaryButton().replace(Styles.PRIMARY, Styles.PRIMARY_DARK)
        );

        // ── Divider ─────────────────────────────────────────────
        HBox divider = new HBox(10);
        divider.setAlignment(Pos.CENTER);
        Separator sep1 = new Separator(); HBox.setHgrow(sep1, Priority.ALWAYS);
        Separator sep2 = new Separator(); HBox.setHgrow(sep2, Priority.ALWAYS);
        Label orLabel  = new Label("or");
        orLabel.setStyle("-fx-text-fill: " + Styles.TEXT_MUTED + "; -fx-font-size: 12px;");
        divider.getChildren().addAll(sep1, orLabel, sep2);

        // ── Register section ─────────────────────────────────────
        VBox registerBox = new VBox(12);
        registerBox.setPadding(new Insets(16, 24, 16, 24));
        registerBox.setStyle(
            "-fx-background-color: " + Styles.PRIMARY_LIGHT + ";" +
            "-fx-background-radius: 12;" +
            "-fx-border-color: " + Styles.BORDER + ";" +
            "-fx-border-radius: 12;"
        );

        Label noAccount = new Label("Don't have an account?");
        noAccount.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: " + Styles.TEXT_DARK + ";");

        Label noAccountSub = new Label("Register for free and start booking appointments today.");
        noAccountSub.setStyle("-fx-font-size: 12px; -fx-text-fill: " + Styles.TEXT_MUTED + ";");
        noAccountSub.setWrapText(true);

        Button registerBtn = new Button("Create New Account  →");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        registerBtn.setPrefHeight(42);
        Styles.addHover(registerBtn,
            Styles.greenButton(),
            Styles.greenButton().replace(Styles.SUCCESS, "#15803D")
        );

        registerBox.getChildren().addAll(noAccount, noAccountSub, registerBtn);

        // ── Assemble form ────────────────────────────────────────
        form.getChildren().addAll(
            heading, sub,
            new VBox(6, roleLabel, roleBox),
            new VBox(6, emailLabel, email),
            new VBox(6, passLabel, password),
            errorLabel,
            loginBtn,
            divider,
            registerBox
        );

        scroll.setContent(form);
        scroll.setStyle("-fx-background-color: " + Styles.BG + ";");

        // ── Actions ──────────────────────────────────────────────
        loginBtn.setOnAction(e -> {
            errorLabel.setText("");
            String cleanRole = extractRole(((ToggleButton) roleGroup.getSelectedToggle()).getText());
            if (email.getText().isBlank() || password.getText().isBlank()) {
                errorLabel.setText("⚠  Email and password are required.");
                return;
            }
            boolean ok = auth.login(email.getText().trim(), password.getText(), cleanRole);
            if (ok) {
                switch (cleanRole) {
                    case "Doctor"  -> new DoctorDashboard().start(stage);
                    case "Patient" -> new PatientDashboard().start(stage);
                    case "Admin"   -> new AdminDashboard().start(stage);
                }
            } else {
                errorLabel.setText("⚠  Invalid credentials. Please try again.");
            }
        });

        registerBtn.setOnAction(e -> new RegisterPage().start(stage));

        // ── Root ─────────────────────────────────────────────────
        HBox root = new HBox(leftPanel, scroll);
        HBox.setHgrow(scroll, Priority.ALWAYS);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMaximized(true);                  // ← opens maximized
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        stage.setTitle("MediCare — Login");
        stage.show();
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