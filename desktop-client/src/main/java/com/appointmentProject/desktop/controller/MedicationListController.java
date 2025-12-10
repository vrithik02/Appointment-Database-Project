package com.appointmentProject.desktop.controller;

import com.appointmentProject.desktop.SceneNavigator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MedicationListController {

    @FXML private TableView<Row> table;
    @FXML private TableColumn<Row, Integer> idCol;
    @FXML private TableColumn<Row, String> nameCol;
    @FXML private TableColumn<Row, String> strengthCol;
    @FXML private TableColumn<Row, String> typeCol;
    @FXML private TableColumn<Row, String> consumptionCol;

    @FXML private TextField searchField;
    @FXML private Label messageLabel;

    @FXML private Button createButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private final ObservableList<Row> masterList = FXCollections.observableArrayList();

    public static class Row {
        private final int id;
        private final String name;
        private final String strength;
        private final String type;
        private final String consumption;

        public Row(int id, String name, String strength, String type, String consumption) {
            this.id = id;
            this.name = name;
            this.strength = strength;
            this.type = type;
            this.consumption = consumption;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getStrength() { return strength; }
        public String getType() { return type; }
        public String getConsumption() { return consumption; }
    }

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        strengthCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStrength()));
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType()));
        consumptionCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getConsumption()));

        loadData();

        if (searchField != null) {
            searchField.textProperty().addListener((o, oldV, newV) -> filter(newV));
        }

        applyAdminPermissions();
    }

    private void applyAdminPermissions() {
        boolean admin = UserSession.isAdmin();
        if (createButton != null) createButton.setDisable(!admin);
        if (editButton != null) editButton.setDisable(!admin);
        if (deleteButton != null) deleteButton.setDisable(!admin);
    }

    private void loadData() {
        try {
            URL url = new URL("http://localhost:8080/medication/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();

            JsonArray arr = JsonParser.parseString(sb.toString()).getAsJsonArray();

            masterList.clear();
            for (JsonElement e : arr) {
                JsonObject o = e.getAsJsonObject();
                int id = o.get("id").getAsInt();
                String name = o.get("medName").getAsString();
                String strength = o.get("strength").getAsString();
                String type = o.get("type").getAsString();
                String consumption = o.get("consumption").getAsString();
                masterList.add(new Row(id, name, strength, type, consumption));
            }

            table.setItems(masterList);
            messageLabel.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Failed to load medications.");
        }
    }

    private void filter(String q) {
        if (q == null || q.isBlank()) {
            table.setItems(masterList);
            return;
        }

        String lower = q.toLowerCase();
        table.setItems(masterList.filtered(x ->
                x.getName().toLowerCase().contains(lower)
                        || x.getStrength().toLowerCase().contains(lower)
                        || x.getType().toLowerCase().contains(lower)
                        || x.getConsumption().toLowerCase().contains(lower)
                        || String.valueOf(x.getId()).contains(lower)
        ));
    }

    // ---------- CRUD HANDLERS (ADMIN ONLY) ----------

    @FXML
    private void handleCreate() {
        if (!UserSession.isAdmin()) {
            messageLabel.setText("Only admin can create medications.");
            return;
        }

        MedicationFormData data = showMedicationForm(null);
        if (data == null) {
            return; // cancelled
        }

        try {
            JsonObject body = new JsonObject();
            body.addProperty("medName", data.name());
            body.addProperty("strength", data.strength());
            body.addProperty("type", data.type());
            body.addProperty("consumption", data.consumption());

            int code = sendJsonRequest("http://localhost:8080/medication", "POST", body);
            if (code >= 200 && code < 300) {
                messageLabel.setText("Medication created.");
                loadData();
            } else {
                messageLabel.setText("Failed to create medication. HTTP " + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error creating medication.");
        }
    }

    @FXML
    private void handleEdit() {
        if (!UserSession.isAdmin()) {
            messageLabel.setText("Only admin can edit medications.");
            return;
        }

        Row selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Select a medication to edit.");
            return;
        }

        MedicationFormData data = showMedicationForm(selected);
        if (data == null) return;

        try {
            JsonObject body = new JsonObject();
            body.addProperty("medName", data.name());
            body.addProperty("strength", data.strength());
            body.addProperty("type", data.type());
            body.addProperty("consumption", data.consumption());

            int code = sendJsonRequest("http://localhost:8080/medication/" + selected.getId(), "PUT", body);
            if (code >= 200 && code < 300) {
                messageLabel.setText("Medication updated.");
                loadData();
            } else {
                messageLabel.setText("Failed to update medication. HTTP " + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error updating medication.");
        }
    }

    @FXML
    private void handleDelete() {
        if (!UserSession.isAdmin()) {
            messageLabel.setText("Only admin can delete medications.");
            return;
        }

        Row selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setText("Select a medication to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Medication");
        confirm.setHeaderText("Delete medication: " + selected.getName());
        confirm.setContentText("Are you sure? This cannot be undone.");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            URL url = new URL("http://localhost:8080/medication/" + selected.getId());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");

            int code = con.getResponseCode();
            if (code >= 200 && code < 300) {
                messageLabel.setText("Medication deleted.");
                loadData();
            } else {
                messageLabel.setText("Failed to delete. HTTP " + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error deleting medication.");
        }
    }

    // ---------- SUPPORT METHODS ----------

    private int sendJsonRequest(String urlStr, String method, JsonObject body) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = body.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        return con.getResponseCode();
    }

    private record MedicationFormData(String name, String strength, String type, String consumption) {}

    /**
     * Shows a simple dialog to create or edit medication.
     * @param existing null for create, or existing Row for edit
     */
    private MedicationFormData showMedicationForm(Row existing) {
        Dialog<MedicationFormData> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Create Medication" : "Edit Medication");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField();
        TextField strengthField = new TextField();
        TextField typeField = new TextField();
        TextField consumptionField = new TextField();

        if (existing != null) {
            nameField.setText(existing.getName());
            strengthField.setText(existing.getStrength());
            typeField.setText(existing.getType());
            consumptionField.setText(existing.getConsumption());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, new Label("Name:"), nameField);
        grid.addRow(1, new Label("Strength:"), strengthField);
        grid.addRow(2, new Label("Type:"), typeField);
        grid.addRow(3, new Label("Consumption:"), consumptionField);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String name = nameField.getText().trim();
                String strength = strengthField.getText().trim();
                String type = typeField.getText().trim();
                String consumption = consumptionField.getText().trim();

                if (name.isEmpty() || strength.isEmpty() || type.isEmpty() || consumption.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.", ButtonType.OK);
                    alert.showAndWait();
                    return null;
                }
                return new MedicationFormData(name, strength, type, consumption);
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    // ---------- BACK BUTTON ----------

    @FXML
    public void handleBack() {
        SceneNavigator.switchTo("/fxml/admin_dashboard.fxml");
    }
}
