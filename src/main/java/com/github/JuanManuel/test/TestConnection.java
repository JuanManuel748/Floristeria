package com.github.JuanManuel.test;

import com.github.JuanManuel.model.connection.H2Connection;
import com.github.JuanManuel.model.connection.MySQLConnection;

import java.sql.*;

public class TestConnection {
    private static final String H2_FIND_ALL = "SELECT r.*, rf.*, f.* "
            + "FROM \"ramo\" r "
            + "JOIN \"ramoflores\" rf ON r.\"idRamo\" = rf.\"Ramo_idRamo\" "
            + "JOIN \"flor\" f ON rf.\"Flor_idFlor\" = f.\"idFlor\"";
    public static void main(String[] args) {
        try {
            /*
            Connection con = null;
            con = MySQLConnection.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT r.*, rf.*, f.* FROM Ramo r JOIN RamoFlores rf ON r.idRamo = rf.Ramo_idRamo JOIN Flor f ON rf.Flor_idFlor = f.idFlor");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getInt("r.idRamo"));
            }
            /*  */

            //H2Connection.loadDB();
            Connection con = H2Connection.getTEMPConnection();

            if (con == null) {
                System.out.println("CONEXION FALLIDA");
            } else {
                System.out.println("CONEXION EXITOSA");
            }
            /*
            String insert = "INSERT INTO \"ramo\" (\"idRamo\", \"florPrincipal\", \"cantidadFlores\", \"colorEnvoltorio\") VALUES  (?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setInt(1, 20);
            ps.setInt(2, 6);
            ps.setInt(3, 10);
            ps.setString(4, "azul");

            int rs = ps.executeUpdate();
            System.out.println("Insertado:" + rs);
            /*  */
            // contraseña = 6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b
            /**/
            String select = "SELECT \"r\".* FROM \"ramo\" \"r\"";
            PreparedStatement ps = con.prepareStatement(H2_FIND_ALL);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                String columnName = rs.getString("idRamo");
                System.out.println(columnName);
            }

            /*  */


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
