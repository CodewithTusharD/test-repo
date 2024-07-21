package com.example.projectjava.enrollcustomer;

//import com.mysql.cj.jdbc.DriverManager;

import java.sql.Connection;
import java.sql.DriverManager;
public class MySqlConnection {
    public static Connection doConnect()
    {
        Connection con=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost/StitchWorks","root","Bhavish@2010");
        }catch(Exception exp)
        {
            exp.printStackTrace();
        }
        return con;
    }
    public static void main(String ary[])
    {
        if(doConnect()==null)
            System.out.println("Sorry...");
        else System.out.println("Database found!");
    }
}
