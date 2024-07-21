package com.example.projectjava.allworkers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class allWorkers_controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CheckBox onlyAvail;

    @FXML
    private TableView<workerInfoBean> tableView;

    @FXML
    private ComboBox<String> wSpecialization;

    private Connection con;
    private PreparedStatement stmt;

    // Initialize with a given connection
    public void initData(Connection connection) {
        this.con = connection;
        initialize();
    }

    @FXML
    void exportToExcel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Export to Excel");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to export the table to Excel?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                writeExcel();
                showMyMsg("Data successfully exported to Users.csv.");
            } catch (Exception e) {
                e.printStackTrace();
                showMyMsg("An error occurred while exporting the data.");
            }
        }
    }

    public void writeExcel() throws Exception {
        Writer writer = null;
        try {
            File file = new File("Users.csv");
            writer = new BufferedWriter(new FileWriter(file));
            String header = "Worker name,Contact number,Address,Specialization\n";
            writer.write(header);
            ObservableList<workerInfoBean> records = getRecords();
            for (workerInfoBean p : records) {
                String text = String.format("\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        p.getWname().replace("\"", "\"\""), // Escape double quotes by doubling them
                        p.getMobile().replace("\"", "\"\""),
                        p.getWadd().replace("\"", "\"\""),
                        p.getSplz().replace("\"", "\"\""));
                writer.write(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    @FXML
    void getAllRecords(ActionEvent event) {
        String specializations = wSpecialization.getEditor().getText().trim();

        if (specializations.isEmpty()) {
            loadAllRecords();
        } else {
            getSpecificWorker(event);
        }
    }

    void loadAllRecords() {
        tableView.getColumns().clear();

        TableColumn<workerInfoBean, String> workerName = new TableColumn<>("Worker Name");
        workerName.setCellValueFactory(new PropertyValueFactory<>("wname"));
        workerName.setMinWidth(100);

        TableColumn<workerInfoBean, String> Wmobile = new TableColumn<>("Contact number");
        Wmobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        Wmobile.setMinWidth(100);

        TableColumn<workerInfoBean, String> Wadd = new TableColumn<>("Address");
        Wadd.setCellValueFactory(new PropertyValueFactory<>("wadd"));
        Wadd.setMinWidth(200);

        TableColumn<workerInfoBean, String> Wsplz = new TableColumn<>("Specialization");
        Wsplz.setCellValueFactory(new PropertyValueFactory<>("splz"));
        Wsplz.setMinWidth(300);

        tableView.getColumns().addAll(workerName, Wmobile, Wadd, Wsplz);
        tableView.setItems(getRecords());
    }

    ObservableList<workerInfoBean> getRecords() {
        ObservableList<workerInfoBean> ary = FXCollections.observableArrayList();

        try {
            if (onlyAvail.isSelected()) {
                stmt = con.prepareStatement("select * from worker where avail=1");
            } else {
                stmt = con.prepareStatement("select * from worker");
            }
            ResultSet records = stmt.executeQuery();
            while (records.next()) {
                String name = records.getString("wname");
                String mobile = records.getString("mobile");
                String address = records.getString("wadd");
                String splz = records.getString("splz");

                ary.add(new workerInfoBean(name, mobile, address, splz));
                System.out.println(name + "  " + mobile + "  " + address + "  " + splz);
            }

        } catch (Exception exp) {
            exp.printStackTrace();
        }
        System.out.println(ary.size());
        return ary;
    }

    @FXML
    void getSpecificWorker(ActionEvent event) {
        String specializations = wSpecialization.getEditor().getText();
        String[] specializationArray = specializations.split(",");

        ObservableList<workerInfoBean> filteredRecords = FXCollections.observableArrayList();

        try {
            StringBuilder query = new StringBuilder("SELECT * FROM worker WHERE ");
            for (int i = 0; i < specializationArray.length; i++) {
                if (i > 0) {
                    query.append(" AND ");
                }
                query.append("FIND_IN_SET(?, splz)");
            }

            if (onlyAvail.isSelected()) {
                query.append(" AND avail=1");
            }

            stmt = con.prepareStatement(query.toString());

            for (int i = 0; i < specializationArray.length; i++) {
                stmt.setString(i + 1, specializationArray[i].trim());
            }

            ResultSet records = stmt.executeQuery();
            while (records.next()) {
                String name = records.getString("wname");
                String mobile = records.getString("mobile");
                String address = records.getString("wadd");
                String splz = records.getString("splz");

                filteredRecords.add(new workerInfoBean(name, mobile, address, splz));
                System.out.println(name + "  " + mobile + "  " + address + "  " + splz);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        System.out.println(filteredRecords.size());
        tableView.setItems(filteredRecords);
    }

    @FXML
    void initialize() {
        assert onlyAvail != null : "fx:id=\"onlyAvail\" was not injected: check your FXML file 'allWorkers_view.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'allWorkers_view.fxml'.";
        assert wSpecialization != null : "fx:id=\"wSpecialization\" was not injected: check your FXML file 'allWorkers_view.fxml'.";

        // Initialize connection if not passed
        if (con == null) {
            con = MySqlConnection.doConnect();
            if (con == null) {
                System.out.println("Connection did not Establish");
            } else {
                System.out.println("Connection Established.");
            }
        }

        ObservableList<String> items = FXCollections.observableArrayList(
                "Coat", "Pant", "Shirt", "Tux", "Blazer", "Suit", "Dress", "Skirt",
                "Jacket", "Vest", "Trousers", "Shorts", "Overcoat", "Gown", "Waistcoat");
        wSpecialization.setItems(items);

        getRecords();  // Load initial data
    }

    void showMyMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
