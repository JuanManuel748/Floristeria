package com.github.JuanManuel.test;

import com.github.JuanManuel.model.connection.MySQLConnection;

import java.sql.*;

public class TestConnection {

    public static void main(String[] args) {
        Connection con = null;
        try {
        con = MySQLConnection.getConnection();

        PreparedStatement ps = con.prepareStatement("SELECT r.*, rf.*, f.* FROM Ramo r JOIN RamoFlores rf ON r.idRamo = rf.Ramo_idRamo JOIN Flor f ON rf.Flor_idFlor = f.idFlor");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            System.out.println(rs.getInt("r.idRamo"));
        }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
