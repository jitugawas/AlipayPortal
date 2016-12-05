package com.dynamicpayment.unionpay;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UnionPayDAO {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/contactdb";

    // Database credentials
    static final String USER = "root";
    static final String PASS = "admin";

    Connection conn = null;
    Statement stmt = null;

    public UnionPayDAO() {
        // Register JDBC driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int saveRequestData(String[] merreqvo, String[] valueVo, String signType, String gateway) {

        return 0;
    }

}
