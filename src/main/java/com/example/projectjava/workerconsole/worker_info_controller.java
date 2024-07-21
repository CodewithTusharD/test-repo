package com.example.projectjava.workerconsole;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.projectjava.enrollcustomer.MySqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class worker_info_controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox availChk;

    @FXML
    private ListView<String> specialTypes;

    @FXML
    private TextField wAdd;

    @FXML
    private TextField wName;

    @FXML
    private TextField wPhone;

    @FXML
    private TextField wSpecial;

    private Connection con; // Correctly declare the connection variable
    private PreparedStatement stmt;

    @FXML
    void clearAll(ActionEvent event) {
        wName.clear();
        wPhone.clear();
        wAdd.clear();
        wSpecial.clear();
        availChk.setSelected(false);
        specialTypes.getSelectionModel().clearSelection();
    }

    @FXML
    void doSave(ActionEvent event) {
        String name = wName.getText();
        String phone = wPhone.getText();
        String address = wAdd.getText();
        String specialization = wSpecial.getText();
        int availability = availChk.isSelected() ? 1 : 0;

        try {
            String query = "SELECT COUNT(*) FROM worker WHERE wname = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                query = "UPDATE worker SET mobile = ?, wadd = ?, splz = ?, avail = ? WHERE wname = ?";
                stmt = con.prepareStatement(query);
                stmt.setString(1, phone);
                stmt.setString(2, address);
                stmt.setString(3, specialization);
                stmt.setInt(4, availability);
                stmt.setString(5, name);
            } else {
                query = "INSERT INTO worker (wname, mobile, wadd, splz, avail) VALUES (?, ?, ?, ?, ?)";
                stmt = con.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, address);
                stmt.setString(4, specialization);
                stmt.setInt(5, availability);
            }
            stmt.executeUpdate();
            showMyMsg("Record saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doSearch(ActionEvent event) {
        String name = wName.getText();
        try {
            String query = "SELECT * FROM worker WHERE wname = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                wPhone.setText(rs.getString("mobile"));
                wAdd.setText(rs.getString("wadd"));
                wSpecial.setText(rs.getString("splz"));
                availChk.setSelected(rs.getInt("avail") == 1);
            } else {
                showMyMsg("Worker not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert availChk != null : "fx:id=\"availChk\" was not injected: check your FXML file 'worker_info_view.fxml'.";
        assert specialTypes != null : "fx:id=\"specialTypes\" was not injected: check your FXML file 'worker_info_view.fxml'.";
        assert wAdd != null : "fx:id=\"wAdd\" was not injected: check your FXML file 'worker_info_view.fxml'.";
        assert wName != null : "fx:id=\"wName\" was not injected: check your FXML file 'worker_info_view.fxml'.";
        assert wPhone != null : "fx:id=\"wPhone\" was not injected: check your FXML file 'worker_info_view.fxml'.";
        assert wSpecial != null : "fx:id=\"wSpecial\" was not injected: check your FXML file 'worker_info_view.fxml'.";

        con = MySqlConnection.doConnect(); // Correctly assign the connection
        if (con == null) {
            System.out.println("Connection did not Establish");
        } else {
            System.out.println("Connection Established.");
        }

        // Pre-fill ListView with types of stitching works
        ObservableList<String> items = FXCollections.observableArrayList(
                "Coat", "Pant", "Shirt", "Tux", "Blazer", "Suit", "Dress", "Skirt",
                "Jacket", "Vest", "Trousers", "Shorts", "Overcoat", "Gown", "Waistcoat");
        specialTypes.setItems(items);

        // Set ListView to allow multiple selections
        specialTypes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // Handle selection changes in ListView
        specialTypes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            ObservableList<String> selectedItems = specialTypes.getSelectionModel().getSelectedItems();
            String selectedString = String.join(",", selectedItems);
            wSpecial.setText(selectedString);
        });
    }
    void showMyMsg(String msg)
    {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information Dialog");
//        alert.setHeaderText("Its Header");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

