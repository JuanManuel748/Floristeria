package com.github.JuanManuel.test;

import com.github.JuanManuel.model.connection.MySQLConnection;

import java.sql.*;

public class TestConnection {

    public static void main(String[] args) {
        Connection con = null;
        con = MySQLConnection.getConnection();
        if(con != null) {
            System.out.printf("CONEXION EXITOSA");
        }
    }
}
