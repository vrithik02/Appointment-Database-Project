/*************************************************************************
 *  ViewStaffController.java
 *
 *      This controller is responsible for allowing user types Receptionist,
 *      Nurse, and Provider to view the First Name, Last Name, Phone Number,
 *      Email, and Specialty of every staff member (Nurses or Providers). If
 *      the staff member is a Nurse, their specialty will say "Nurse".
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/7/2025
 **************************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ViewStaffController {

    @FXML private TableView<StaffRow> staffTable;
    @FXML private TableColumn<StaffRow, String> firstNameCol;
    @FXML private TableColumn<StaffRow, String> lastNameCol;
    @FXML private TableColumn<StaffRow, String> phoneCol;
    @FXML private TableColumn<StaffRow, String> emailCol;
    @FXML private TableColumn<StaffRow, String> specialtyCol;

    @FXML private Label messageLabel;

    private final Gson gson = new Gson();
    public static String previousPage = "/fxml/login.fxml"; // fallback


    // Inner row model
    public static class StaffRow {
        private final String firstName;
        private final String lastName;
        private final String phone;
        private final String email;
        private final String specialty;

        public StaffRow(String firstName,
                        String lastName,
                        String phone,
                        String email,
                        String specialty) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.email = email;
            this.specialty = specialty;
        }

        public String getFirstName() { return firstName; }
        public String getLastName()  { return lastName;  }
        public String getPhone()     { return phone;     }
        public String getEmail()     { return email;     }
        public String getSpecialty() { return specialty; }
    }

    @FXML
    private void initialize() {
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        specialtyCol.setCellValueFactory(new PropertyValueFactory<>("specialty"));

        loadStaff();
    }

    private void loadStaff() {
        try {
            URL url = new URL("http://localhost:8080/staff-view/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            String json = sb.toString();
            JsonArray arr = com.google.gson.JsonParser.parseString(json).getAsJsonArray();

            ObservableList<StaffRow> rows = FXCollections.observableArrayList();

            for (JsonElement el : arr) {
                JsonObject obj = el.getAsJsonObject();

                String firstName = obj.get("firstName").getAsString();
                String lastName  = obj.get("lastName").getAsString();
                String phone     = obj.get("phone").getAsString();
                String email     = obj.get("email").getAsString();
                String specialty = obj.get("specialty").getAsString();

                rows.add(new StaffRow(firstName, lastName, phone, email, specialty));
            }

            staffTable.setItems(rows);
            messageLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading staff.");
        }
    }

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo(previousPage);
    }

}
