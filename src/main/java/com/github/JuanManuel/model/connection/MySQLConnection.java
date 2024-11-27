package com.github.JuanManuel.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static Connection con = null;
    private static final String URL = "jdbc:mysql://localhost:3306/floreyesdb";
    private static final String ROOT = "root";
    private static final String PASS = "";


    public MySQLConnection() {
    }

    public static Connection getConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(URL, ROOT, PASS);
            } catch (SQLException SQLe) {
                throw new RuntimeException(SQLe);
            }
        }
        return con;
    }

    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
    }



}
