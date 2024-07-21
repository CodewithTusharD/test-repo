package com.example.projectjava.enrollcustomer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class enrollCustomer_controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField cAdd;

    @FXML
    private ComboBox<String> cCity;

    @FXML
    private DatePicker cDob;

    @FXML
    private ComboBox<String> cGender;

    @FXML
    private TextField cName;

    @FXML
    private TextField cEmail;

    @FXML
    private TextField cPhone;

    @FXML
    private ImageView imgPrev;

    private Connection con;
    private PreparedStatement stmt;

    @FXML
    void doDelete(ActionEvent event) {
        // Add your deletion logic here
        try{
            stmt=con.prepareStatement("delete from cus_Info where mobile = ?");
            stmt.setString(1,cPhone.getText());
            int count=stmt.executeUpdate();
            if (count==1)
                showMyMsg("Record Deleted Successfully");
            else
                showMyMsg("Invalid Phone Number");
        }catch(Exception exp)
        {
            exp.printStackTrace();
        }
    }

    @FXML
    void doSave(ActionEvent event) {
        try {
            stmt = con.prepareStatement("insert into cus_Info (mobile, name, address, city, dob, gender, pic_filename,email,doEnroll) values(?,?,?,?,?,?,?,?,current_date())");

            stmt.setString(1, cPhone.getText()); // Mobile
            stmt.setString(2, cName.getText()); // Name
            stmt.setString(3, cAdd.getText()); // Address

            // Check if city selection is made
            String city = cCity.getSelectionModel().getSelectedItem() != null ? cCity.getSelectionModel().getSelectedItem().toString() : "Unknown";
            stmt.setString(4, city); // City

            // Check if date of birth is selected
            Date dob = cDob.getValue() != null ? Date.valueOf(cDob.getValue()) : null;
            stmt.setDate(5, dob); // Date of Birth

            // Check if gender selection is made
            String gender = cGender.getSelectionModel().getSelectedItem() != null ? cGender.getSelectionModel().getSelectedItem().toString() : "Unknown";
            stmt.setString(6, gender); // Gender

            // Handle picture path
            stmt.setString(7,filePath);// Picture filename

            //save email
            stmt.setString(8,cEmail.getText());
            // Execute the update
            stmt.executeUpdate();

            // Inform the user
            showMyMsg("Customer data saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void fillCities()
    {
        try{
            stmt=con.prepareStatement("select distinct city from cus_Info");
            ResultSet records= stmt.executeQuery();
            ArrayList<String> ary=new ArrayList<>();
            while(records.next())
            {
                String city=records.getString("city");
                ary.add(city);
            }
            cCity.getItems().addAll(ary);
        }catch (Exception exp)
        {
            exp.printStackTrace();
        }
    }


    @FXML
    void doSearch(ActionEvent event) {
        try {
            stmt = con.prepareStatement("select * from cus_Info where mobile = ?");
            stmt.setString(1, cPhone.getText());
            ResultSet records = stmt.executeQuery();

            if (records.next()) {
                String name = records.getString("name");
                String phone = records.getString("mobile");
                String add = records.getString("address");
                String city = records.getString("city");
                String gender = records.getString("gender");
                String path = records.getString("pic_filename");
                String email = records.getString("email");
                Date dob = records.getDate("dob");

                System.out.println(name + " " + phone + " "+email+" "+ add + " " + city + " " + gender + " " + path + " " + dob);

                cName.setText(name);
                cPhone.setText(phone.toString());
                cAdd.setText(add);
                cEmail.setText(email);
                cCity.setValue(city);
                cGender.setValue(gender);
                cDob.setValue(dob.toLocalDate());


                if (!path.equals("nopic.jpg")) {
                    filePath = path;
                    imgPrev.setImage(new Image(new FileInputStream(filePath)));
                } else {

                    showMyMsg("No image found.");
                }
            } else {
                // Handle case when no record found
                showMyMsg("No customer found with this phone number.");

                cName.setText("");
                cPhone.setText("");
                cAdd.setText("");
                cEmail.setText("");
                cCity.setValue(null);
                cGender.setValue(null);
                imgPrev.setImage(null);
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void doUpdate(ActionEvent event) {
        // Add your update logic here
        try
        {
            stmt=con.prepareStatement("update cus_Info set name=?,address=?,city=? ,dob=?,gender=? ,pic_filename=?,email=? where mobile = ?");

            stmt.setString(1, cName.getText()); // Name
            stmt.setString(2, cAdd.getText()); // Address

            // Check if city selection is made
            String city = cCity.getSelectionModel().getSelectedItem() != null ? cCity.getSelectionModel().getSelectedItem().toString() : "Unknown";
            stmt.setString(3, city); // City


            Date dob = cDob.getValue() != null ? Date.valueOf(cDob.getValue()) : null;
            stmt.setDate(4, dob);


            String gender = cGender.getSelectionModel().getSelectedItem() != null ? cGender.getSelectionModel().getSelectedItem().toString() : "Unknown";
            stmt.setString(5, gender);
            stmt.setString(6,filePath);
            stmt.setString(7, cEmail.getText());
            stmt.setString(8, cPhone.getText());


            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showMyMsg("Record updated Successfully");
            } else {
                showMyMsg("Failed to update record. Mobile number not found.");
            }
        }catch (Exception exp)
        {
            exp.printStackTrace();
        }
    }
    String filePath="nopic.jpg";
    @FXML
    void uploadPic(ActionEvent event) {
        // Add your upload picture logic here
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

    @FXML
    void initialize() {
        // Ensure these are correctly used
        assert cAdd != null : "fx:id=\"cAdd\" was not injected: check your FXML file 'enrollCust_view.fxml'.";
        assert cCity != null : "fx:id=\"cCity\" was not injected: check your FXML file 'enrollCust_view.fxml'.";
        assert cDob != null : "fx:id=\"cDob\" was not injected: check your FXML file 'enrollCust_view.fxml'.";
        assert cEmail != null : "fx:id=\"cEmail\" was not injected: check your FXML file 'enrollCust_view.fxml'.";
        assert cGender != null : "fx:id=\"cGender\" was not injected: check your FXML file 'enrollCust_view.fxml'.";
        assert cName != null : "fx:id=\"cName\" was not injected: check your FXML file 'enrollCust_view.fxml'.";
        assert cPhone != null : "fx:id=\"cPhone\" was not injected: check your FXML file 'enrollCust_view.fxml'.";
        assert imgPrev != null : "fx:id=\"imgPrev\" was not injected: check your FXML file 'enrollCust_view.fxml'.";

        con = MySqlConnection.doConnect(); // Correctly assign the connection
        if (con == null) {
            System.out.println("Connection did not Establish");
        } else {
            System.out.println("Connection Established.");
        }
        ObservableList<String> genderOptions = FXCollections.observableArrayList("Male", "Female", "Others");
        cGender.setItems(genderOptions);

        fillCities();
    }
    void showMyMsg(String msg)
    {
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
//        alert.setHeaderText("Its Header");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

