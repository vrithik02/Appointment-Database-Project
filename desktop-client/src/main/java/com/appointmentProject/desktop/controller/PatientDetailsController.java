/********************************************************************
 *  PatientDetailsController.java
 *
 *          This controller is responsible for displaying a specific
 *          Patient's profile: including every field within the
 *          database. Within this page, there is an "update" button
 *          allowing the user to edit the Patient's details.
 *
 * @author Matthew Kiyono
 * @version 1.0
 * @since 12/7/2025
 ********************************************************************/
package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PatientDetailsController {

    @FXML private Label idLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label dobLabel;
    @FXML private Label ageLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label genderLabel;
    @FXML private Label heightLabel;
    @FXML private Label weightLabel;
    @FXML private Label insuranceLabel;
    @FXML private Label emergencyContactLabel;
    @FXML private Label messageLabel;

    @FXML
    private void initialize() {
        loadPatientDetails();
    }

    private void loadPatientDetails() {
        try {
            int id = ManagePatientController.selectedPatientId;

            URL url = new URL("http://localhost:8080/patient/" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            JsonObject obj = JsonParser.parseString(sb.toString()).getAsJsonObject();

            idLabel.setText(String.valueOf(obj.get("id").getAsInt()));
            firstNameLabel.setText(obj.get("firstName").getAsString());
            lastNameLabel.setText(obj.get("lastName").getAsString());
            dobLabel.setText(obj.get("doB").getAsString());
            ageLabel.setText(String.valueOf(obj.get("age").getAsInt()));
            phoneLabel.setText(obj.get("phone").getAsString());
            emailLabel.setText(obj.get("email").isJsonNull() ? "N/A" : obj.get("email").getAsString());
            genderLabel.setText(obj.get("gender").isJsonNull() ? "N/A" : obj.get("gender").getAsString());
            heightLabel.setText(String.valueOf(obj.get("height").getAsDouble()));
            weightLabel.setText(String.valueOf(obj.get("weight").getAsDouble()));

            insuranceLabel.setText(obj.get("insuranceId").isJsonNull()
                    ? "None" : obj.get("insuranceId").getAsString());

            emergencyContactLabel.setText(obj.get("emergencyContactId").isJsonNull()
                    ? "None" : obj.get("emergencyContactId").getAsString());

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error loading patient details.");
        }
    }

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/manage_patient.fxml");
    }
}
