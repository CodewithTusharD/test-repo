package com.example.projectjava.ownerdash;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.example.projectjava.allworkers.allWorkers_controller;
import com.example.projectjava.enrollcustomer.MySqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class OwnersDash {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button AllWorkerButton;

    @FXML
    private Button DeliverProductsButton;

    @FXML
    private TextField UserId;

    @FXML
    private PasswordField UserPwd;

    @FXML
    private Button enrollCusButton;

    @FXML
    private Button enrollWorkerButton;

    @FXML
    private Button getReadyItmsButton;

    @FXML
    private ImageView logout_icon;

    @FXML
    private Button loginButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Circle loginCircle;

    @FXML
    private Label loginEnterUid;

    @FXML
    private Label loginEnterUpwd;

    @FXML
    private Label loginWelcome;


    @FXML
    private Button msrmntsExplorButton;

    @FXML
    private Label orderstatuslabel;

    @FXML
    private PieChart statusPieChart;

    @FXML
    private Button newMsrmntsButton;

    private Connection con;
    private PreparedStatement stmt;

    @FXML
    void doLogOut(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Logout Confirmation");
        alert.setContentText("Are you sure you want to log out?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                AllWorkerButton.setDisable(true);
                DeliverProductsButton.setDisable(true);
                enrollCusButton.setDisable(true);
                enrollWorkerButton.setDisable(true);
                getReadyItmsButton.setDisable(true);
                msrmntsExplorButton.setDisable(true);
                newMsrmntsButton.setDisable(true);
                logout_icon.setDisable(true);

                loginCircle.setVisible(true);
                loginWelcome.setVisible(true);
                loginButton.setVisible(true);
                loginEnterUid.setVisible(true);
                loginEnterUpwd.setVisible(true);
                UserId.setVisible(true);
                UserId.setDisable(false);
                UserPwd.setVisible(true);
                UserPwd.setDisable(false);
                statusPieChart.setVisible(false);
                statusPieChart.setDisable(true);
                orderstatuslabel.setVisible(false);
                refreshButton.setVisible(false);
                refreshButton.setDisable(true);
            }
        });
        UserId.clear();
        UserPwd.clear();
    }

    @FXML
    void doLogin(ActionEvent event) {
        try {
            String uId = UserId.getText();
            String uPwd = UserPwd.getText();

            if (uId == null || uId.isEmpty()) {
                showMyMsg("Please Fill User ID");
                return;
            }
            if (uPwd == null || uPwd.isEmpty()) {
                showMyMsg("Please Enter the Password");
                return;
            }

            String query = "SELECT * FROM owner WHERE ownerId = ? AND ownerPwd = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, uId);
            stmt.setString(2, uPwd);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                AllWorkerButton.setDisable(false);
                DeliverProductsButton.setDisable(false);
                enrollCusButton.setDisable(false);
                enrollWorkerButton.setDisable(false);
                getReadyItmsButton.setDisable(false);
                msrmntsExplorButton.setDisable(false);
                newMsrmntsButton.setDisable(false);
                logout_icon.setDisable(false);

                loginCircle.setVisible(false);
                loginWelcome.setVisible(false);
                loginButton.setVisible(false);
                loginEnterUid.setVisible(false);
                loginEnterUpwd.setVisible(false);
                UserId.setVisible(false);
                UserId.setDisable(true);
                UserPwd.setVisible(false);
                UserPwd.setDisable(true);
                statusPieChart.setVisible(true);
                statusPieChart.setDisable(false);
                orderstatuslabel.setVisible(true);
                refreshButton.setVisible(true);
                refreshButton.setDisable(false);

                showMyMsg("Login Successful!");
            } else {
                showMyMsg("Invalid User ID or Password");
            }


            rs.close();
            stmt.close();
        } catch (Exception exp) {
            exp.printStackTrace();
            showMyMsg("Error occurred during login");
        }
    }

    @FXML
    void goOnAllWorkers(ActionEvent event) {
        openNewScene2("/com/example/projectjava/allworkerss/allWorkers_view.fxml", "All Workers");
    }

    @FXML
    void goOnDeliverProducts(ActionEvent event) {
        openNewScene("/com/example/projectjava/getdeliveryy/deliverProducts_view.fxml", "Deliver Products");
    }

    @FXML
    void goOnEnrollCus(ActionEvent event) {
        openNewScene("/com/example/projectjava/enrollcustomerr/enrollCust_view.fxml", "Enroll Customer");
    }

    @FXML
    void goOnEnrollWorker(ActionEvent event) {
        openNewScene("/com/example/projectjava/workerconsolee/worker_info_view.fxml", "Enroll Worker");
    }

    @FXML
    void goOnGetReadyItms(ActionEvent event) {
        openNewScene("/com/example/projectjava/getreadyproductss/getReadyProduct_view.fxml", "Get Ready Items");
    }

    @FXML
    void goOnMsrmnts(ActionEvent event) {
        openNewScene("/com/example/projectjava/measurementss/measurements_view.fxml", "Measurements");
    }
    @FXML
    void doRefreshStatus(ActionEvent event) {
        initialize();
    }

    @FXML
    void goOnMsrmntsExplorer(ActionEvent event) {
        openNewScene("/com/example/projectjava/measurementsexplorerr/exploreMeasurements_view.fxml", "Measurements Explorer");
    }

    private void openNewScene2(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Ensure the controller is correctly instantiated
            allWorkers_controller controller = loader.getController();
            if (controller != null) {
                controller.initData(con);  // Pass the connection to the new controller
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.resizableProperty().setValue(false);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showMyMsg("Error loading " + title + " view: " + e.getMessage());
        }
    }

    private void openNewScene(String fxmlPath, String title) {
        try {
            URL fxmlLocation = getClass().getResource(fxmlPath);
            if (fxmlLocation == null) {
                throw new IllegalStateException("Cannot find FXML file: " + fxmlPath);
            }
            Parent fxmlLoader = FXMLLoader.load(fxmlLocation);
            Scene scene = new Scene(fxmlLoader);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.resizableProperty().setValue(false);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showMyMsg("Error loading " + title + " view: " + e.getMessage());
        }
    }

    int getRecords(int s)
    {
        try {

            String query = "SELECT COUNT(*) FROM measurements WHERE status = ?";
            PreparedStatement stmt1 = con.prepareStatement(query);
            stmt1.setInt(1, s); // Use oi from the doSearch function
            ResultSet rs1 = stmt1.executeQuery();

            if(rs1.next())
            {
                int i=rs1.getInt(1);
                return i;
            }
            else return 0;
        }catch (Exception exp)
        {
            exp.printStackTrace();
        }
        return 0;
    }


    @FXML
    void initialize() {
        assert AllWorkerButton != null : "fx:id=\"AllWorkerButton\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert DeliverProductsButton != null : "fx:id=\"DeliverProductsButton\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert UserId != null : "fx:id=\"UserId\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert UserPwd != null : "fx:id=\"UserPwd\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert enrollCusButton != null : "fx:id=\"enrollCusButton\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert enrollWorkerButton != null : "fx:id=\"enrollWorkerButton\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert getReadyItmsButton != null : "fx:id=\"getReadyItmsButton\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert loginCircle != null : "fx:id=\"loginCircle\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert loginEnterUid != null : "fx:id=\"loginEnterUid\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert loginEnterUpwd != null : "fx:id=\"loginEnterUpwd\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert loginWelcome != null : "fx:id=\"loginWelcome\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert logout_icon != null : "fx:id=\"logout_icon\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert msrmntsExplorButton != null : "fx:id=\"msrmntsExplorButton\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert newMsrmntsButton != null : "fx:id=\"newMsrmntsButton\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert orderstatuslabel != null : "fx:id=\"orderstatuslabel\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert refreshButton != null : "fx:id=\"refreshButton\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        assert statusPieChart != null : "fx:id=\"statusPieChart\" was not injected: check your FXML file 'ownerDash_View.fxml'.";


        con = MySqlConnection.doConnect();
        if (con == null) {
            System.out.println("Connection did not Establish");
        } else {
            System.out.println("Connection Established.");
        }

        assert statusPieChart !=null : "fx:id=\"statusPieChart\" was not injected: check your FXML file 'ownerDash_View.fxml'.";
        int inPg=getRecords(1);
        int Deliver=getRecords(3);
        int Recieved=getRecords(2);

        ObservableList<PieChart.Data> data = FXCollections.observableArrayList(new PieChart.Data("In process "+ inPg,inPg),new PieChart.Data("Deliverd "+Deliver,Deliver),new PieChart.Data("Received "+Recieved,Recieved));
        statusPieChart.setData(data);
    }

    void showMyMsg(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
