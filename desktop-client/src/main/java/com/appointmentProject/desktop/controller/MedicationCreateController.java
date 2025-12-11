package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MedicationCreateController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField strengthField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField consumptionField;
    @FXML
    private Label messageLabel;

    @FXML
    public void handleSave() {
        if (nameField.getText().isEmpty()
                || strengthField.getText().isEmpty()
                || typeField.getText().isEmpty()
                || consumptionField.getText().isEmpty()) {

            messageLabel.setText("All fields are required.");
            return;
        }

        messageLabel.setText("Medication created successfully!");
        SceneNavigator.switchTo("/fxml/Medication_list.fxml");
    }
}
