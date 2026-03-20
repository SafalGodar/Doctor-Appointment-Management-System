package controller;

import model.DBConnection;
import model.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthController {

    DBConnection db = new DBConnection();

    public boolean register(String name, String age, String contact,
                            String address, String email, String password, String role) {

        String table = role.equals("Doctor") ? "doctors"
                     : role.equals("Patient") ? "patients"
                     : "admins";

        String sql = "INSERT INTO " + table +
                " (name, age, contact, address, email, password) VALUES (?,?,?,?,?,?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, Integer.parseInt(age));
            stmt.setString(3, contact);
            stmt.setString(4, address);
            stmt.setString(5, email);
            stmt.setString(6, password);
            stmt.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean login(String email, String password, String role) {

        String table = role.equals("Doctor") ? "doctors"
                     : role.equals("Patient") ? "patients"
                     : "admins";

        String sql = "SELECT * FROM " + table + " WHERE email=? AND password=?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Session.userId    = rs.getInt("id");
                Session.userName  = rs.getString("name");
                Session.userEmail = email;
                Session.role      = role;
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}