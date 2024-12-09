package com.github.JuanManuel.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static Connection con = null;
    private static final String URL = "jdbc:mysql://localhost:3306/floreyesdb";
    private static final String ROOT = "root";
    private static final String PASS = "";


    public MySQLConnection() {}

    /**
     * Retrieves a connection to the MySQL database.
     * If no connection exists, it initializes one using the specified URL, username, and password.
     *
     * @return a Connection object to the MySQL database.
     * @throws RuntimeException if the connection cannot be established.
     */
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

    /**
     * Closes the current connection to the MySQL database, if one exists.
     * If the connection is already closed or null, this method does nothing.
     */
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
