package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MedicationEditController {

    @FXML private TextField nameField;
    @FXML private TextField strengthField;
    @FXML private TextField typeField;
    @FXML private TextField consumptionField;
    @FXML private Label messageLabel;

    // Values filled by MedicationListController before switching to this screen
    public static String existingName;
    public static String existingStrength;
    public static String existingType;
    public static String existingConsumption;

    @FXML
    private void initialize() {
        // Pre-fill fields when editing (front-end only)
        if (existingName != null)           nameField.setText(existingName);
        if (existingStrength != null)       strengthField.setText(existingStrength);
        if (existingType != null)           typeField.setText(existingType);
        if (existingConsumption != null)    consumptionField.setText(existingConsumption);
    }

    @FXML
    public void handleSave() {
        // For now, just show a message and go back to the list
        // (Later you can add a real backend update call here)
        messageLabel.setText("Medication changes saved (front-end only).");
        SceneNavigator.switchTo("/fxml/Medication_list.fxml");
    }

    @FXML
    public void handleCancel() {
        // Cancel just goes back to the medication list
        SceneNavigator.switchTo("/fxml/Medication_list.fxml");
    }
}
