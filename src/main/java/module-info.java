module com.example.projectjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.java;
    requires java.desktop;
    requires java.mail;

    opens com.example.projectjava to javafx.fxml;
    exports com.example.projectjava;

    opens com.example.projectjava.enrollcustomer to javafx.fxml;
    exports com.example.projectjava.enrollcustomer;

    opens com.example.projectjava.workerconsole to javafx.fxml;
    exports com.example.projectjava.workerconsole;

    opens com.example.projectjava.measurements to javafx.fxml;
    exports com.example.projectjava.measurements;

    opens com.example.projectjava.getreadyproducts to javafx.fxml;
    exports com.example.projectjava.getreadyproducts;

    opens com.example.projectjava.allworkers to javafx.fxml;
    exports com.example.projectjava.allworkers;

    opens com.example.projectjava.measurementsexplorer to javafx.fxml;
    exports com.example.projectjava.measurementsexplorer;

    opens com.example.projectjava.getdelivery to javafx.fxml;
    exports com.example.projectjava.getdelivery;

    opens com.example.projectjava.ownerdash to javafx.fxml;
    exports com.example.projectjava.ownerdash;
}