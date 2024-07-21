package com.example.projectjava.getdelivery;

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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class deliverProducts_controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Integer> cBill;

    @FXML
    private ListView<String> cItems;

    @FXML
    private TextField cMobile;

    @FXML
    private ListView<Integer> cOrderId;

    @FXML
    private ListView<Integer> cStatus;

    @FXML
    private Button deliverAllButton;

    @FXML
    private TextField totalBill;
    private Connection con;
    private PreparedStatement stmt;

    @FXML
    void doDeliver(ActionEvent event) {
        try {
            String cusPhone = cMobile.getText();
            if (cusPhone == null || cusPhone.trim().isEmpty()) {
                showMyMsg("Please enter a valid Phone Number");
                return;
            }

            // Fetching bills with status = 2
            stmt = con.prepareStatement("SELECT bill FROM measurements WHERE status = 2 AND mobile = ?");
            stmt.setString(1, cusPhone);
            ResultSet records = stmt.executeQuery();

            int PaybleBill = 0;
            while (records.next()) {
                int bill = records.getInt("bill");
                PaybleBill += bill;
            }
            totalBill.setText(String.valueOf(PaybleBill));

            // Closing the ResultSet and PreparedStatement after use
            records.close();
            stmt.close();

            // Updating status to 3
            stmt = con.prepareStatement("UPDATE measurements SET status = 3 WHERE status = 2 AND mobile = ?");
            stmt.setString(1, cusPhone);
            stmt.executeUpdate();
            stmt.close();

            // Fetching updated statuses
            stmt = con.prepareStatement("SELECT * FROM measurements WHERE mobile = ?");
            stmt.setString(1, cusPhone);
            ResultSet recordsStatus = stmt.executeQuery();

            ObservableList<Integer> StatusList = FXCollections.observableArrayList();
            Integer orderId = null; // Initialize outside loop

            while (recordsStatus.next()) {
                StatusList.add(recordsStatus.getInt("status"));
                orderId = recordsStatus.getInt("orderId"); // Update this inside the loop
            }

            cStatus.getItems().clear();
            cStatus.getItems().addAll(StatusList);

            if (orderId != null) {
                showMyMsg("Order Delivered");
                sendEmail("Order with order ID : " + orderId + " just got delivered. Your Payable Amount is: " + totalBill.getText());
            } else {
                showMyMsg("No orders found for the given phone number.");
            }

            // Closing the ResultSet and PreparedStatement after use
            recordsStatus.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    void doFind(ActionEvent event) {
        try {
            String cusPhone = cMobile.getText();
            if (cusPhone == null || cusPhone.trim().isEmpty()) {
                showMyMsg("Please enter a valid Phone Number");
                return;
            }

            stmt = con.prepareStatement("SELECT orderId, bill, status, dress FROM measurements WHERE status < 3 AND mobile = ?");
            stmt.setString(1, cusPhone);
            ResultSet records = stmt.executeQuery();

            ObservableList<Integer> orderIdsList = FXCollections.observableArrayList();
            ObservableList<Integer> StatusList = FXCollections.observableArrayList();
            ObservableList<String> dressesList = FXCollections.observableArrayList();
            ObservableList<Integer> BillList = FXCollections.observableArrayList();

            boolean hasStatus2 = false;
            while (records.next()) {
                orderIdsList.add(records.getInt("orderId"));
                int status = records.getInt("status");
                StatusList.add(status);
                dressesList.add(records.getString("dress"));
                int bill = records.getInt("bill");
                BillList.add(bill);
                if (status == 2) {
                    hasStatus2 = true;
                }
            }

            cOrderId.getItems().clear();
            cOrderId.getItems().addAll(orderIdsList);

            cItems.getItems().clear();
            cItems.getItems().addAll(dressesList);

            cBill.getItems().clear();
            cBill.getItems().addAll(BillList);

            cStatus.getItems().clear();
            cStatus.getItems().addAll(StatusList);

            // Enable or disable the "Deliver All" button based on the presence of orders with status 2
            deliverAllButton.setDisable(!hasStatus2);

            // Close resources
            records.close();
            stmt.close();

        } catch (Exception e) {
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
            String emaill=getEmailAddress(cMobile.getText());
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(emaill));
            message.setSubject("Order Delivered");
            message.setText(msg);

            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
            showMyMsg("Failed to send email. Check your configuration and try again.");
        }
    }


    @FXML
    void initialize() {
        assert cBill != null : "fx:id=\"cBill\" was not injected: check your FXML file 'deliverProducts_view.fxml'.";
        assert cItems != null : "fx:id=\"cItems\" was not injected: check your FXML file 'deliverProducts_view.fxml'.";
        assert cMobile != null : "fx:id=\"cMobile\" was not injected: check your FXML file 'deliverProducts_view.fxml'.";
        assert cOrderId != null : "fx:id=\"cOrderId\" was not injected: check your FXML file 'deliverProducts_view.fxml'.";
        assert cStatus != null : "fx:id=\"cStatus\" was not injected: check your FXML file 'deliverProducts_view.fxml'.";
        assert totalBill != null : "fx:id=\"totalBill\" was not injected: check your FXML file 'deliverProducts_view.fxml'.";
        assert deliverAllButton != null : "fx:id=\"deliverAllButton\" was not injected: check your FXML file 'deliverProducts_view.fxml'.";

        con = MySqlConnection.doConnect();
        if (con == null) {
            System.out.println("Connection did not Establish");
        } else {
            System.out.println("Connection Established.");
        }
    }

    void showMyMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
