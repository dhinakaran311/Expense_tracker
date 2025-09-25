package com.expensetracker.util;

import java.sql.Connection; 
import java.sql.DriverManager; //JDBC API
import java.sql.SQLException; 

public class DatabaseUtil {

    // Update these values to match your MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/expense_tracker?useSSL=true&serverTimezone=UTC";
    private static final String USER = "root";      // replace with your MySQL username
    private static final String PASSWORD = "Dhina@311";  // replace with your MySQL password

    static{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            System.out.println("MySQL JDBC Driver not found.");
        }
    }
    // Get a database connection
    public static Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // // Optional: close connection safely
    // public static void closeConnection(Connection conn) {
    //     if (conn != null) {
    //         try {
    //             conn.close();
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }

}
