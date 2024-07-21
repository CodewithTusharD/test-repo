package com.example.projectjava.measurementsexplorer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.ResourceBundle;

import com.example.projectjava.allworkers.workerInfoBean;
import com.example.projectjava.enrollcustomer.MySqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class exploreMeasurements_controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField cPhone;

    @FXML
    private ComboBox<String> cStatus;

    @FXML
    private ComboBox<String> cWorker;

    @FXML
    private TableView<explorerBean> tableView;

    private Connection con;
    private PreparedStatement stmt;
    @FXML
    void doExportToExcel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Export to Excel");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to export the table to Excel?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                writeExcel();
                showMyMsg("Data successfully exported to measurements.csv.");
            } catch (Exception e) {
                e.printStackTrace();
                showMyMsg("An error occurred while exporting the data.");
            }
        }
    }

    public void writeExcel() throws Exception {
        Writer writer = null;
        try {
            File file = new File("measurements.csv");
            writer = new BufferedWriter(new FileWriter(file));

            // Write header
            StringBuilder header = new StringBuilder();
            for (TableColumn<?, ?> column : tableView.getColumns()) {
                header.append(column.getText()).append(",");
            }
            writer.write(header.toString().replaceAll(",$", "") + "\n");

            // Write data
            for (explorerBean p : tableView.getItems()) {
                StringBuilder row = new StringBuilder();
                for (TableColumn<?, ?> column : tableView.getColumns()) {
                    String columnName = column.getText();
                    switch (columnName) {
                        case "Order ID":
                            row.append(p.getOrderId()).append(",");
                            break;
                        case "Customer's Phone number":
                            row.append(p.getMobile()).append(",");
                            break;
                        case "Ordered Dress":
                            row.append(p.getDress()).append(",");
                            break;
                        case "Date for Delivery":
                            row.append(p.getDod()).append(",");
                            break;
                        case "Worker Assigned":
                            row.append(p.getWorkerass()).append(",");
                            break;
                        case "Status of work":
                            row.append(p.getStatus()).append(",");
                            break;
                        case "Quantity":
                            row.append(p.getQty()).append(",");
                            break;
                        case "Price per unit":
                            row.append(p.getPpu()).append(",");
                            break;
                        case "Total Bill":
                            row.append(p.getBill()).append(",");
                            break;
                        case "Measurements":
                            row.append(p.getMsrmnts()).append(",");
                            break;
                        default:
                            row.append(","); // In case there are any additional or unknown columns
                    }
                }
                writer.write(row.toString().replaceAll(",$", "") + "\n");
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
    void doShowAll(ActionEvent event) {
        tableView.getColumns().clear();

        TableColumn<explorerBean, String> oi=new TableColumn<explorerBean, String>("Order ID");//kuch bhi
        oi.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        oi.setMinWidth(100);

        TableColumn<explorerBean, String> mobile=new TableColumn<explorerBean, String>("Customer's Phone number");//kuch bhi
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobile.setMinWidth(100);

        TableColumn<explorerBean, String> dress=new TableColumn<explorerBean, String>("Ordered Dress");//kuch bhi
        dress.setCellValueFactory(new PropertyValueFactory<>("dress"));
        dress.setMinWidth(100);

        TableColumn<explorerBean, String> dod=new TableColumn<explorerBean, String>("Date for Delivery");//kuch bhi
        dod.setCellValueFactory(new PropertyValueFactory<>("dod"));
        dod.setMinWidth(100);

        TableColumn<explorerBean, String> assworker=new TableColumn<explorerBean, String>("Worker Assigned");//kuch bhi
        assworker.setCellValueFactory(new PropertyValueFactory<>("workerass"));
        assworker.setMinWidth(100);

        TableColumn<explorerBean, String> Status = new TableColumn<>("Status of work");
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        Status.setMinWidth(100);
//        TableColumn<explorerBean, String> picPath_name=new TableColumn<explorerBean, String>("Design file Path");//kuch bhi
//        picPath_name.setCellValueFactory(new PropertyValueFactory<>("design_filename;"));
//        picPath_name.setMinWidth(100);

        TableColumn<explorerBean, String> qty=new TableColumn<explorerBean, String>("Quantity");//kuch bhi
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qty.setMinWidth(100);

        TableColumn<explorerBean, String> ppu=new TableColumn<explorerBean, String>("Price per unit");//kuch bhi
        ppu.setCellValueFactory(new PropertyValueFactory<>("ppu"));
        ppu.setMinWidth(100);

        TableColumn<explorerBean, String> bill=new TableColumn<explorerBean, String>("Total Bill");//kuch bhi
        bill.setCellValueFactory(new PropertyValueFactory<>("bill"));
        bill.setMinWidth(100);

        TableColumn<explorerBean, String> measurements=new TableColumn<explorerBean, String>("Measurements");//kuch bhi
        measurements.setCellValueFactory(new PropertyValueFactory<>("msrmnts"));
        measurements.setMinWidth(100);

        tableView.getColumns().addAll(oi,mobile,dress,dod,assworker,Status,qty,ppu,bill,measurements);
        tableView.setItems(getRecords());
    }
    ObservableList<explorerBean> getRecords()
    {
        ObservableList<explorerBean> ary= FXCollections.observableArrayList();

        try {

                stmt = con.prepareStatement("select * from measurements ");

            ResultSet records= stmt.executeQuery();
            while(records.next())
            {
                int orderId=records.getInt("orderId");//col name
                String mobile=records.getString("mobile");//col name
                String dress=records.getString("dress");//col name
                Date dod=records.getDate("dod");//col name
                String design_filename=records.getString("design_filename");//col name
                int qty=records.getInt("qty");//col name
                int ppu=records.getInt("ppu");//col name
                int bill=records.getInt("bill");//col name
                String msrmnts=records.getString("msrmnts");//col name
                String workerass=records.getString("workerass");//col name
                int status3=records.getInt("status");


                ary.add(new explorerBean( orderId, mobile, dress, dod, design_filename, qty, ppu, bill, msrmnts, workerass,status3) );
                System.out.println(orderId+"  "+mobile+"  "+dress+"  "+dod+" "+design_filename+" "+qty+" "+ppu+" "+bill+" "+msrmnts+" "+workerass);

            }

        }
        catch(Exception exp)
        {
            exp.printStackTrace();
        }
        System.out.println(ary.size());
        return ary;
    }

    @FXML
    void getStatusRecord(ActionEvent event) {

//        cPhone.clear();
        tableView.getColumns().clear();

        TableColumn<explorerBean, String> oi=new TableColumn<explorerBean, String>("Order ID");//kuch bhi
        oi.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        oi.setMinWidth(100);

        TableColumn<explorerBean, String> mobile=new TableColumn<explorerBean, String>("Customer's Phone number");//kuch bhi
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobile.setMinWidth(100);

        TableColumn<explorerBean, String> dress=new TableColumn<explorerBean, String>("Ordered Dress");//kuch bhi
        dress.setCellValueFactory(new PropertyValueFactory<>("dress"));
        dress.setMinWidth(100);

        TableColumn<explorerBean, String> dod=new TableColumn<explorerBean, String>("Date for Delivery");//kuch bhi
        dod.setCellValueFactory(new PropertyValueFactory<>("dod"));
        dod.setMinWidth(100);

        TableColumn<explorerBean, String> assworker=new TableColumn<explorerBean, String>("Worker Assigned");//kuch bhi
        assworker.setCellValueFactory(new PropertyValueFactory<>("workerass"));
        assworker.setMinWidth(100);

//        TableColumn<explorerBean, String> picPath_name=new TableColumn<explorerBean, String>("Design file Path");//kuch bhi
//        picPath_name.setCellValueFactory(new PropertyValueFactory<>("design_filename;"));
//        picPath_name.setMinWidth(100);

        TableColumn<explorerBean, String> qty=new TableColumn<explorerBean, String>("Quantity");//kuch bhi
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qty.setMinWidth(100);

        TableColumn<explorerBean, String> ppu=new TableColumn<explorerBean, String>("Price per unit");//kuch bhi
        ppu.setCellValueFactory(new PropertyValueFactory<>("ppu"));
        ppu.setMinWidth(100);

        TableColumn<explorerBean, String> bill=new TableColumn<explorerBean, String>("Total Bill");//kuch bhi
        bill.setCellValueFactory(new PropertyValueFactory<>("bill"));
        bill.setMinWidth(100);

        TableColumn<explorerBean, String> measurements=new TableColumn<explorerBean, String>("Measurements");//kuch bhi
        measurements.setCellValueFactory(new PropertyValueFactory<>("msrmnts"));
        measurements.setMinWidth(100);

        tableView.getColumns().addAll(oi,mobile,dress,dod,assworker,qty,ppu,bill,measurements);
        tableView.setItems(getThisStatusRecords());
    }
    ObservableList<explorerBean> getThisStatusRecords() {
        ObservableList<explorerBean> ary = FXCollections.observableArrayList();

        try {
            String selectedWorker = cWorker.getValue();
            String statusText = cStatus.getValue();

//            // Check if statusText is empty
//            if (statusText == null || statusText.isEmpty()) {
//                showMyMsg("Please select a status.");
//                return ary; // Return an empty list if no status is selected
//            }

            int status = 0; // Initialize status to a default value

            switch (statusText) {
                case "Order Placed":
                    status = 1;
                    break;
                case "Received From Worker":
                    status = 2;
                    break;
                case "Orders Delivered":
                    status = 3;
                    break;
                default:
                    // Handle the case where statusText doesn't match any expected value
                    throw new IllegalArgumentException("Invalid status: " + statusText);
            }

            String query = "SELECT * FROM measurements WHERE status = ?";
            if (selectedWorker != null && !selectedWorker.isEmpty()) {
                query += " AND workerass = ?";
            }

            stmt = con.prepareStatement(query);
            stmt.setInt(1, status);

            if (selectedWorker != null && !selectedWorker.isEmpty()) {
                stmt.setString(2, selectedWorker);
            }

            ResultSet records = stmt.executeQuery();
            while (records.next()) {
                int orderId = records.getInt("orderId");
                String mobile = records.getString("mobile");
                String dress = records.getString("dress");
                Date dod = records.getDate("dod");
                String design_filename = records.getString("design_filename");
                int qty = records.getInt("qty");
                int ppu = records.getInt("ppu");
                int bill = records.getInt("bill");
                String msrmnts = records.getString("msrmnts");
                String workerass = records.getString("workerass");
                int status1=records.getInt("status");

                ary.add(new explorerBean(orderId, mobile, dress, dod, design_filename, qty, ppu, bill, msrmnts, workerass,status1));
                System.out.println(orderId + " " + mobile + " " + dress + " " + dod + " " + design_filename + " " + qty + " " + ppu + " " + bill + " " + msrmnts + " " + workerass);
            }
        } catch (IllegalArgumentException e) {
            showMyMsg(e.getMessage()); // Show a message to the user
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ary.size());
        return ary;
    }



    @FXML
    void getThisCusRecords(ActionEvent event) {
        cStatus.getSelectionModel().clearSelection();
        cWorker.getSelectionModel().clearSelection();
        tableView.getColumns().clear();

        TableColumn<explorerBean, String> oi = new TableColumn<>("Order ID");
        oi.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        oi.setMinWidth(100);

        TableColumn<explorerBean, String> mobile = new TableColumn<>("Customer's Phone number");
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobile.setMinWidth(100);

        TableColumn<explorerBean, String> dress = new TableColumn<>("Ordered Dress");
        dress.setCellValueFactory(new PropertyValueFactory<>("dress"));
        dress.setMinWidth(100);

        TableColumn<explorerBean, String> dod = new TableColumn<>("Date for Delivery");
        dod.setCellValueFactory(new PropertyValueFactory<>("dod"));
        dod.setMinWidth(100);

        TableColumn<explorerBean, String> assworker = new TableColumn<>("Worker Assigned");
        assworker.setCellValueFactory(new PropertyValueFactory<>("workerass"));
        assworker.setMinWidth(100);

        TableColumn<explorerBean, String> Status = new TableColumn<>("Status of work");
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        Status.setMinWidth(100);

        TableColumn<explorerBean, String> qty = new TableColumn<>("Quantity");
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qty.setMinWidth(100);

        TableColumn<explorerBean, String> ppu = new TableColumn<>("Price per unit");
        ppu.setCellValueFactory(new PropertyValueFactory<>("ppu"));
        ppu.setMinWidth(100);

        TableColumn<explorerBean, String> bill = new TableColumn<>("Total Bill");
        bill.setCellValueFactory(new PropertyValueFactory<>("bill"));
        bill.setMinWidth(100);

        TableColumn<explorerBean, String> measurements = new TableColumn<>("Measurements");
        measurements.setCellValueFactory(new PropertyValueFactory<>("msrmnts"));
        measurements.setMinWidth(100);

        tableView.getColumns().addAll(oi, mobile, dress, dod, assworker,Status, qty, ppu, bill, measurements);
        tableView.setItems(getCustomerSpecificRecords());
    }
    ObservableList<explorerBean> getCustomerSpecificRecords() {
        ObservableList<explorerBean> ary = FXCollections.observableArrayList();

        try {
            String customerPhone = cPhone.getText();

            if (customerPhone == null || customerPhone.isEmpty()) {
                showMyMsg("Please enter a phone number.");
                return ary;
            }

            String query = "SELECT * FROM measurements WHERE mobile = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, customerPhone);

            ResultSet records = stmt.executeQuery();
            while (records.next()) {
                int orderId = records.getInt("orderId");
                String mobile = records.getString("mobile");
                String dress = records.getString("dress");
                Date dod = records.getDate("dod");
                String design_filename = records.getString("design_filename");
                int qty = records.getInt("qty");
                int ppu = records.getInt("ppu");
                int bill = records.getInt("bill");
                String msrmnts = records.getString("msrmnts");
                String workerass = records.getString("workerass");
                int status=records.getInt("status");
                ary.add(new explorerBean(orderId, mobile, dress, dod, design_filename, qty, ppu, bill, msrmnts, workerass,status));
                System.out.println(orderId + " " + mobile + " " + dress + " " + dod + " " + design_filename + " " + qty + " " + ppu + " " + bill + " " + msrmnts + " " + workerass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ary.size());
        return ary;
    }


    @FXML
    void getWorkerRecord(ActionEvent event) {
//        cPhone.clear();
        tableView.getColumns().clear();

        TableColumn<explorerBean, String> oi = new TableColumn<>("Order ID");
        oi.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        oi.setMinWidth(100);

        TableColumn<explorerBean, String> mobile = new TableColumn<>("Customer's Phone number");
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        mobile.setMinWidth(100);

        TableColumn<explorerBean, String> dress = new TableColumn<>("Ordered Dress");
        dress.setCellValueFactory(new PropertyValueFactory<>("dress"));
        dress.setMinWidth(100);

        TableColumn<explorerBean, String> dod = new TableColumn<>("Date for Delivery");
        dod.setCellValueFactory(new PropertyValueFactory<>("dod"));
        dod.setMinWidth(100);

        TableColumn<explorerBean, String> assworker = new TableColumn<>("Worker Assigned");
        assworker.setCellValueFactory(new PropertyValueFactory<>("workerass"));
        assworker.setMinWidth(100);

        TableColumn<explorerBean, String> qty = new TableColumn<>("Quantity");
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        qty.setMinWidth(100);

        TableColumn<explorerBean, String> ppu = new TableColumn<>("Price per unit");
        ppu.setCellValueFactory(new PropertyValueFactory<>("ppu"));
        ppu.setMinWidth(100);

        TableColumn<explorerBean, String> bill = new TableColumn<>("Total Bill");
        bill.setCellValueFactory(new PropertyValueFactory<>("bill"));
        bill.setMinWidth(100);

        TableColumn<explorerBean, String> measurements = new TableColumn<>("Measurements");
        measurements.setCellValueFactory(new PropertyValueFactory<>("msrmnts"));
        measurements.setMinWidth(100);

        tableView.getColumns().addAll(oi, mobile, dress, dod, assworker, qty, ppu, bill, measurements);
        tableView.setItems(getWorkerSpecificRecords());
    }
    ObservableList<explorerBean> getWorkerSpecificRecords() {
        ObservableList<explorerBean> ary = FXCollections.observableArrayList();

        try {
            String selectedWorker = cWorker.getValue();
            String selectedStatus = cStatus.getValue();

//            if (selectedWorker == null || selectedWorker.isEmpty()) {
//                showMyMsg("Please select a worker.");
//                return ary;
//            }

            String query = "SELECT * FROM measurements WHERE workerass = ?";
            if (selectedStatus != null && !selectedStatus.isEmpty()) {
                query += " AND status = ?";
            }

            stmt = con.prepareStatement(query);
            stmt.setString(1, selectedWorker);

            if (selectedStatus != null && !selectedStatus.isEmpty()) {
                int status = 0;
                switch (selectedStatus) {
                    case "Order Placed":
                        status = 1;
                        break;
                    case "Received From Worker":
                        status = 2;
                        break;
                    case "Orders Delivered":
                        status = 3;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid status: " + selectedStatus);
                }
                stmt.setInt(2, status);
            }

            ResultSet records = stmt.executeQuery();
            while (records.next()) {
                int orderId = records.getInt("orderId");
                String mobile = records.getString("mobile");
                String dress = records.getString("dress");
                Date dod = records.getDate("dod");
                String design_filename = records.getString("design_filename");
                int qty = records.getInt("qty");
                int ppu = records.getInt("ppu");
                int bill = records.getInt("bill");
                String msrmnts = records.getString("msrmnts");
                String workerass = records.getString("workerass");
                int status2=records.getInt("status");
                ary.add(new explorerBean(orderId, mobile, dress, dod, design_filename, qty, ppu, bill, msrmnts, workerass,status2));
                System.out.println(orderId + " " + mobile + " " + dress + " " + dod + " " + design_filename + " " + qty + " " + ppu + " " + bill + " " + msrmnts + " " + workerass);
            }
        } catch (IllegalArgumentException e) {
            showMyMsg(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ary.size());
        return ary;
    }

    void FillWorkers() {
        try {

            stmt = con.prepareStatement("SELECT DISTINCT workerass FROM measurements ");


            ResultSet records = stmt.executeQuery();
            ObservableList<String> workersList = FXCollections.observableArrayList();
            while (records.next()) {
//                int avail = records.getInt("avail");
//                if (avail == 1) {
                    String worker = records.getString("workerass");
                    workersList.add(worker);
//                }
            }

            cWorker.getItems().clear();
            cWorker.getItems().addAll(workersList);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert cPhone != null : "fx:id=\"cPhone\" was not injected: check your FXML file 'exploreMeasurements_view.fxml'.";
        assert cStatus != null : "fx:id=\"cStatus\" was not injected: check your FXML file 'exploreMeasurements_view.fxml'.";
        assert cWorker != null : "fx:id=\"cWorker\" was not injected: check your FXML file 'exploreMeasurements_view.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'exploreMeasurements_view.fxml'.";

        con = MySqlConnection.doConnect();
        if (con == null) {
            System.out.println("Connection did not Establish");
        } else {
            System.out.println("Connection Established.");
        }
        ObservableList<String> items = FXCollections.observableArrayList(
                "Order Placed", "Received From Worker", "Orders Delivered");
        cStatus.setItems(items);

        FillWorkers();

        // Add a listener to clear the cPhone TextField when cStatus changes
        cStatus.valueProperty().addListener((observable, oldValue, newValue) -> {
            cPhone.clear();
        });
        cWorker.valueProperty().addListener((observable, oldValue, newValue) -> {
            cPhone.clear();
        });
    }
    void showMyMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
