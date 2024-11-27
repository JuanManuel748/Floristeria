package com.github.JuanManuel.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        Connection con = null;
        String URL = "jdbc:mysql://localhost:3306/floreyesdb";
        String ROOT = "root";
        String PASS = "";

        try {
            con = DriverManager.getConnection(URL, ROOT, PASS);
            System.out.println("CONEXION EXITOSA");
        } catch (SQLException SQLe) {
            System.out.println("ERROR AL CONECTAR CON LA BASE DE DATOS");
        }
    }
}
