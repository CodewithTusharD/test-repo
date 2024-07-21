package com.example.projectjava.getreadyproducts;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.example.projectjava.enrollcustomer.MySqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class getReadyProducts_controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> assignedWorkers;

    @FXML
    private ListView<PieChart.Data> dod;

    @FXML
    private ListView<String> orderDress;

    @FXML
    private ListView<String> orderId;

    private Connection con;
    private PreparedStatement stmt;

    @FXML
    void doFillInfo(ActionEvent event) {
        try {
            String selectedWorker = assignedWorkers.getSelectionModel().getSelectedItem();
            if (selectedWorker == null) {
                showMyMsg("Please select a worker.");
                return;
            }

            stmt = con.prepareStatement("SELECT orderId, dress, dod FROM measurements WHERE status = 1 AND workerass = ?");
            stmt.setString(1, selectedWorker);
            ResultSet records = stmt.executeQuery();

            ObservableList<String> orderIdsList = FXCollections.observableArrayList();
            ObservableList<String> dressesList = FXCollections.observableArrayList();
            ObservableList<PieChart.Data> dodList = FXCollections.observableArrayList();

            while (records.next()) {
                orderIdsList.add(String.valueOf(records.getInt("orderId")));
                dressesList.add(records.getString("dress"));
                dodList.add(new PieChart.Data(records.getString("dod"), 1)); // Assuming quantity of 1 for PieChart Data
            }

            orderId.getItems().clear();
            orderId.getItems().addAll(orderIdsList);

            orderDress.getItems().clear();
            orderDress.getItems().addAll(dressesList);

            dod.getItems().clear();
            dod.getItems().addAll(dodList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void doRecieve(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedOrderId = orderId.getSelectionModel().getSelectedItem();
            if (selectedOrderId == null) {
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirm Update");
            alert.setContentText("Are you sure you want to mark this order as received?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        int orderIdInt = Integer.parseInt(selectedOrderId);

                        // Update the status in the database
                        stmt = con.prepareStatement("UPDATE measurements SET status = 2 WHERE orderId = ?");
                        stmt.setInt(1, orderIdInt);
                        int rowsAffected = stmt.executeUpdate();

                        if (rowsAffected > 0) {
                            // Remove items from the ListView
                            int index = orderId.getSelectionModel().getSelectedIndex();
                            orderId.getItems().remove(index);
                            orderDress.getItems().remove(index);
                            dod.getItems().remove(index);
                            showMyMsg("Order marked as received.");
                        } else {
                            showMyMsg("Failed to update the order status.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showMyMsg("An error occurred while updating the order status.");
                    }
                }
            });
        }
    }

    @FXML
    void doRecieveAll(ActionEvent event) {
        try {
            ObservableList<String> orderIdsList = orderId.getItems();

            if (orderIdsList.isEmpty()) {
                showMyMsg("No orders to receive.");
                return;
            }

            // Build a comma-separated list of order IDs
            StringBuilder orderIds = new StringBuilder();
            for (String id : orderIdsList) {
                orderIds.append(id).append(",");
            }

            // Remove the trailing comma
            if (orderIds.length() > 0) {
                orderIds.setLength(orderIds.length() - 1);
            }

            // Update the status of all selected orders to 2
            String query = "UPDATE measurements SET status = 2 WHERE orderId IN (" + orderIds.toString() + ")";
            stmt = con.prepareStatement(query);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Clear all ListViews
                orderId.getItems().clear();
                orderDress.getItems().clear();
                dod.getItems().clear();
                showMyMsg("All selected orders marked as received.");
            } else {
                showMyMsg("Failed to update the order statuses.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showMyMsg("An error occurred while updating the order statuses.");
        }
    }


    void FillWorkers() {
        try {
            stmt = con.prepareStatement("SELECT DISTINCT workerass FROM measurements WHERE status = 1");
            ResultSet records = stmt.executeQuery();
            ObservableList<String> workersList = FXCollections.observableArrayList();
            while (records.next()) {
                String workerName = records.getString("workerass");
                workersList.add(workerName);
            }
            assignedWorkers.getItems().clear();
            assignedWorkers.getItems().addAll(workersList);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert assignedWorkers != null : "fx:id=\"assignedWorkers\" was not injected: check your FXML file 'getReadyProduct_view.fxml'.";
        assert dod != null : "fx:id=\"dod\" was not injected: check your FXML file 'getReadyProduct_view.fxml'.";
        assert orderDress != null : "fx:id=\"orderDress\" was not injected: check your FXML file 'getReadyProduct_view.fxml'.";
        assert orderId != null : "fx:id=\"orderId\" was not injected: check your FXML file 'getReadyProduct_view.fxml'.";
        con = MySqlConnection.doConnect();
        if (con == null) {
            System.out.println("Connection did not Establish");
        } else {
            System.out.println("Connection Established.");
        }

        FillWorkers();
    }

    void showMyMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
