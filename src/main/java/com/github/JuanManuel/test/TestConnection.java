package com.github.JuanManuel.test;

import com.github.JuanManuel.model.connection.MySQLConnection;

import java.sql.*;

public class TestConnection {
    private static final String FIND_BY_PK = "SELECT r.*, rm.*, f.* FROM Ramo r JOIN RamoFlores rm ON r.idRamo = rm.Ramo_idRamo JOIN Flor f ON rm.Flor_idFlor = f.idFlor WHERE idRamo = ?";

    public static void main(String[] args) {
        Connection con = null;
        try {
            con = MySQLConnection.getConnection();
            System.out.println("CONECTADO A LA BASE DE DATOS");
            PreparedStatement ps = con.prepareStatement(FIND_BY_PK);
            ps.setInt(1, 4);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getInt("r.idRamo"));
                System.out.println(rs.getInt("rm.Ramo_idRamo"));
                System.out.println(rs.getInt("rm.Flor_idFlor"));
                System.out.println(rs.getInt("f.idFlor"));
            }
        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
        }
    }
}
