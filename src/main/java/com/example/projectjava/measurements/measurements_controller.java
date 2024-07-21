package com.example.projectjava.measurements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

import com.example.projectjava.enrollcustomer.MySqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.net.PasswordAuthentication;
//import java.net.URL;
//import java.sql.*;
//import java.util.Optional;
//import java.util.Properties;
//import java.util.ResourceBundle;
//
//import com.example.projectjava.enrollcustomer.MySqlConnection;
//import com.mysql.cj.Session;
//import com.mysql.cj.protocol.Message;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//import javafx.scene.control.TextField;
//import javax.mail.*;
//import javax.mail.internet.*;
//
//import static com.mysql.cj.Session.*;

public class measurements_controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField cBill;

    @FXML
    private DatePicker cDod;

    @FXML
    private ComboBox<String> cDress;

    @FXML
    private TextArea cMeasurements;

    @FXML
    private TextField cPhone;

    @FXML
    private TextField cPpu;

    @FXML
    private ComboBox<Integer> cQty;

    @FXML
    private ComboBox<String> cWorkers;

    @FXML
    private ImageView imgPrev;

    private Connection con;
    private PreparedStatement stmt;

    @FXML
    void clearAll(ActionEvent event) {
        cPhone.clear();
        cMeasurements.clear();
        cDress.getSelectionModel().clearSelection();
        cBill.clear();
        cQty.getSelectionModel().clearSelection();
        cQty.getEditor().clear();
        cPpu.clear();
        cDod.setValue(null);
        imgPrev.setImage(null);
        cWorkers.getSelectionModel().clearSelection();
    }

    @FXML
    void doClose(ActionEvent event) {
        // Get a reference to the stage and close it
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

//    @FXML
//    void doSave(ActionEvent event) {
//        String phone = cPhone.getText();
//        String dress = cDress.getSelectionModel().getSelectedItem();
//        Date dod = cDod.getValue() != null ? Date.valueOf(cDod.getValue()) : null;
//        String measurement = cMeasurements.getText();
//        String worker = cWorkers.getSelectionModel().getSelectedItem();
//        String qtyText = cQty.getEditor().getText(); // Get text from editor
//        String ppu = cPpu.getText();
//        String bill = cBill.getText();
//
//        if (phone.isEmpty() || dress == null || dod == null || measurement.isEmpty() || worker == null || qtyText.isEmpty() || ppu.isEmpty() || bill.isEmpty()) {
//            showMyMsg("Please fill in all fields.");
//            return;
//        }
//
//        int qty;
//        try {
//            qty = Integer.parseInt(qtyText);
//        } catch (NumberFormatException e) {
//            showMyMsg("Quantity must be a valid number.");
//            return;
//        }
//
//        try {
//            String query = "SELECT COUNT(*) FROM cus_Info WHERE mobile = ?";
//            stmt = con.prepareStatement(query);
//            stmt.setString(1, phone);
//            ResultSet rs = stmt.executeQuery();
//            rs.next();
//            int count = rs.getInt(1);
//
//            if (count > 0) {
//                String insertQuery = "INSERT INTO measurements (mobile, dress, dod, design_filename, qty, ppu, bill, msrmnts, workerass) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//                stmt = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
//                stmt.setString(1, phone);
//                stmt.setString(2, dress);
//                stmt.setDate(3, dod);
//                stmt.setString(4, filePath);
//                stmt.setInt(5, qty);
//                stmt.setString(6, ppu);
//                stmt.setString(7, bill);
//                stmt.setString(8, measurement);
//                stmt.setString(9, worker);
//
//                stmt.executeUpdate();
//
//                // Retrieve the generated orderId
//                ResultSet generatedKeys = stmt.getGeneratedKeys();
//                int orderId = -1;
//                if (generatedKeys.next()) {
//                    orderId = generatedKeys.getInt(1);
//                }
//
//                showMyMsg("Order received successfully. Your order ID is: " + orderId);
//            } else {
//                showMyMsg("Invalid phone number. Please register with us first.");
//            }
//        } catch (Exception exp) {
//            exp.printStackTrace();
//            showMyMsg("Error occurred while saving the data.");
//        }
//    }

    @FXML
    void doSave(ActionEvent event) {
        String phone = cPhone.getText();
        String dress = cDress.getSelectionModel().getSelectedItem();
        Date dod = cDod.getValue() != null ? Date.valueOf(cDod.getValue()) : null;
        String measurement = cMeasurements.getText();
        String worker = cWorkers.getSelectionModel().getSelectedItem();
        String qtyText = cQty.getEditor().getText(); // Get text from editor
        String ppu = cPpu.getText();
        String bill = cBill.getText();

        if (phone.isEmpty() || dress == null || dod == null || measurement.isEmpty() || worker == null || qtyText.isEmpty() || ppu.isEmpty() || bill.isEmpty()) {
            showMyMsg("Please fill in all fields.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            showMyMsg("Quantity must be a valid number.");
            return;
        }

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = MySqlConnection.doConnect();

            // Check if the mobile number already exists in cus_Info
            String query = "SELECT COUNT(*) FROM cus_Info WHERE mobile = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, phone);
            rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                showMyMsg("No record found for the given phone number. Please register with us first.");
            } else {
                // Insert new record since mobile number exists in cus_Info
                String insertQuery = "INSERT INTO measurements (mobile, dress, dod, design_filename, qty, ppu, bill, msrmnts, workerass,status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
                stmt = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, phone);
                stmt.setString(2, dress);
                stmt.setDate(3, dod);
                stmt.setString(4, filePath); // Ensure filePath is set correctly before this
                stmt.setInt(5, qty);
                stmt.setString(6, ppu);
                stmt.setString(7, bill);
                stmt.setString(8, measurement);
                stmt.setString(9, worker);
                stmt.setInt(10, 1);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Retrieve the generated orderId
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    int orderId = -1;
                    if (generatedKeys.next()) {
                        orderId = generatedKeys.getInt(1);
                    }

                    showMyMsg("Order received successfully & an email is sent to you registered Email ID. Your order ID is: " + orderId);
                    sendEmail("Order of "+dress+" Quantity: "+ qty+ " from phone number: "+phone+" is received successfully. Your order ID is: " + orderId);
                } else {
                    showMyMsg("Failed to insert record.");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            showMyMsg("Error occurred while saving the data.");
        } finally {
            // Close resources in finally block
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @FXML
    void doUpdate(ActionEvent event) {
        String phone = cPhone.getText();
        String dress = cDress.getSelectionModel().getSelectedItem();
        Date dod = cDod.getValue() != null ? Date.valueOf(cDod.getValue()) : null;
        String measurement = cMeasurements.getText();
        String worker = cWorkers.getSelectionModel().getSelectedItem();
        String qtyText = cQty.getEditor().getText(); // Get text from editor
        String ppu = cPpu.getText();
        String bill = cBill.getText();

        if (phone.isEmpty() || dress == null || dod == null || measurement.isEmpty() || worker == null || qtyText.isEmpty() || ppu.isEmpty() || bill.isEmpty()) {
            showMyMsg("Please fill in all fields.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            showMyMsg("Quantity must be a valid number.");
            return;
        }

        try {
            String query = "SELECT COUNT(*) FROM measurements WHERE orderId = ?";
            stmt = con.prepareStatement(query);
            stmt.setInt(1, oi); // Use oi from the doSearch function
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count > 0) {
                String updateQuery = "UPDATE measurements SET mobile = ?, dress = ?, dod = ?, design_filename = ?, qty = ?, ppu = ?, bill = ?, msrmnts = ?, workerass = ? WHERE orderId = ?";
                stmt = con.prepareStatement(updateQuery);
                stmt.setString(1, phone);
                stmt.setString(2, dress);
                stmt.setDate(3, dod);
                stmt.setString(4, filePath);
                stmt.setInt(5, qty);
                stmt.setString(6, ppu);
                stmt.setString(7, bill);
                stmt.setString(8, measurement);
                stmt.setString(9, worker);
                stmt.setInt(10, oi); // Use oi from the doSearch function

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    showMyMsg("Order updated successfully.");
                    sendEmail("Order with order ID: "+oi+" updated successfully.");
                } else {
                    showMyMsg("No order found for the given order ID.");
                }
            } else {
                showMyMsg("Invalid order ID.");
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            showMyMsg("Error occurred while updating the data.");
        }
    }



    int oi;
    @FXML
    void doSearch(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Order");
        dialog.setHeaderText("Enter Order ID:");
        dialog.setContentText("Order ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(orderId -> {
            try {
                String query = "SELECT * FROM measurements WHERE orderId = ?";
                stmt = con.prepareStatement(query);
                stmt.setInt(1, Integer.parseInt(orderId));
                ResultSet rs = stmt.executeQuery();
                oi=Integer.parseInt(orderId);
                if (rs.next()) {
                    String phone = rs.getString("mobile");
                    String dress = rs.getString("dress");
                    Date dod = rs.getDate("dod");
                    String designFilename = rs.getString("design_filename");
                    int qty = rs.getInt("qty");
                    String ppu = rs.getString("ppu");
                    String bill = rs.getString("bill");
                    String measurement = rs.getString("msrmnts");
                    String worker = rs.getString("workerass");

                    cPhone.setText(phone);
                    cDress.getSelectionModel().select(dress);
                    cDod.setValue(dod.toLocalDate());
                    filePath = designFilename;

                    // Check if the file exists before setting the image
                    File file = new File(filePath);
                    if (file.exists()) {
                        imgPrev.setImage(new Image(new FileInputStream(file)));
                    } else {
                        imgPrev.setImage(null);
                        showMyMsg("Design image not found. Default image is shown.");
                    }

                    cQty.getEditor().setText(String.valueOf(qty));
                    cPpu.setText(ppu);
                    cBill.setText(bill);
                    cMeasurements.setText(measurement);
                    cWorkers.getSelectionModel().select(worker);

                    showMyMsg("Record found and populated.");
                } else {
                    showMyMsg("No record found for the given order ID.");
                }
            } catch (Exception exp) {
                exp.printStackTrace();
                showMyMsg("Error occurred while searching for the record.");
            }
        });
    }


    String filePath="nopic.jpg";
    @FXML
    void doUploadPic(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Profile Pic: ");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("*.*", "*.*")
        );
        File file=chooser.showOpenDialog(null);
        filePath=file.getAbsolutePath();
        try {
            imgPrev.setImage(new Image(new FileInputStream(file)));
        }
        catch (FileNotFoundException e) {	e.printStackTrace();}
    }

    void FillWorkers() {
        try {
            String specialization = cDress.getSelectionModel().getSelectedItem() != null ? cDress.getSelectionModel().getSelectedItem().toString() : "Unknown";
            stmt = con.prepareStatement("SELECT wname, avail FROM worker WHERE splz LIKE ?");
            stmt.setString(1, "%" + specialization + "%");

            ResultSet records = stmt.executeQuery();
            ObservableList<String> workersList = FXCollections.observableArrayList();
            while (records.next()) {
                int avail = records.getInt("avail");
                if (avail == 1) {
                    String worker = records.getString("wname");
                    workersList.add(worker);
                }
            }

            cWorkers.getItems().clear();
            cWorkers.getItems().addAll(workersList);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @FXML
    void doBill(ActionEvent event) {
        try {
            String quantityText = cQty.getEditor().getText();
            System.out.println("Quantity: " + quantityText);

            String priceText = cPpu.getText();
            System.out.println("Price per Unit: " + priceText);

            if (quantityText == null || quantityText.isEmpty() || priceText == null || priceText.isEmpty()) {
                cBill.setText("Invalid Input");
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityText);
                int pricePerUnit = Integer.parseInt(priceText);
                int totalBill = quantity * pricePerUnit;
                System.out.println("Total Bill: " + totalBill);
                cBill.setText(String.valueOf(totalBill));
            } catch (NumberFormatException e) {
                cBill.setText("Invalid Input");
                e.printStackTrace();
            }
        } catch (Exception e) {
            cBill.setText("Error occurred");
            e.printStackTrace();
        }
    }

    private String getEmailAddress(String phone) {
        String email = null;
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = MySqlConnection.doConnect();
            String query = "SELECT email FROM cus_Info WHERE mobile = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, phone);
            rs = stmt.executeQuery();

            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return email;
    }


    private void sendEmail(String msg) {
        String from = "stitchworksbm6007@gmail.com"; // sender's email
        final String username = "stitchworksbm6007@gmail.com"; // your Gmail address
        final String password = "xbnm qkno vwpg smav";
//        String to = "kundan.goyal@geminisolutions.com";
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(from));
            message.setFrom(new InternetAddress(from));
            String emaill=getEmailAddress(cPhone.getText());
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(emaill));
            message.setSubject("Order received");
            message.setText(msg);

            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void initialize() {
        assert cBill != null : "fx:id=\"cBill\" was not injected: check your FXML file 'measurements_view.fxml'.";
        assert cDod != null : "fx:id=\"cDod\" was not injected: check your FXML file 'measurements_view.fxml'.";
        assert cDress != null : "fx:id=\"cDress\" was not injected: check your FXML file 'measurements_view.fxml'.";
        assert cMeasurements != null : "fx:id=\"cMeasurements\" was not injected: check your FXML file 'measurements_view.fxml'.";
        assert cPhone != null : "fx:id=\"cPhone\" was not injected: check your FXML file 'measurements_view.fxml'.";
        assert cPpu != null : "fx:id=\"cPpu\" was not injected: check your FXML file 'measurements_view.fxml'.";
        assert cQty != null : "fx:id=\"cQty\" was not injected: check your FXML file 'measurements_view.fxml'.";
        assert cWorkers != null : "fx:id=\"cWorkers\" was not injected: check your FXML file 'measurements_view.fxml'.";
        assert imgPrev != null : "fx:id=\"imgPrev\" was not injected: check your FXML file 'measurements_view.fxml'.";

        con = MySqlConnection.doConnect();
        if (con == null) {
            System.out.println("Connection did not Establish");
        } else {
            System.out.println("Connection Established.");
        }

        ObservableList<String> items = FXCollections.observableArrayList(
                "Coat", "Pant", "Shirt", "Tux", "Blazer", "Suit", "Dress", "Skirt",
                "Jacket", "Vest", "Trousers", "Shorts", "Overcoat", "Gown", "Waistcoat");
        cDress.setItems(items);

        cDress.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            FillWorkers();
        });

        ObservableList<Integer> quantities = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        cQty.setItems(quantities);
        cQty.setEditable(true);
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