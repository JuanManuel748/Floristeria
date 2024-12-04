package com.github.JuanManuel.model.connection;

import com.github.JuanManuel.view.Alerta;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Connection {
    private static final String URL = "jdbc:h2:./data/centroDB";
    private static final String ROOT = "sa";
    private static final String PASS = "";
    private static Connection con = null;

    public H2Connection() {}

    public static Connection getTEMPConnection() {
        if (con == null) {
            try {
                con = DriverManager.getConnection(URL, ROOT, PASS);
            } catch (SQLException SQLe) {
                Alerta.showAlert("ERROR", "No se encuentra la base de datos", "No se ha podido establecer conexión con la base de datos");
                throw new RuntimeException(SQLe);
            }
        }
        return con;
    }

    public static void closeTEMPConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
    }

    public static void loadDB() {
        try (Connection con = getTEMPConnection();
             Statement stmt = con.createStatement()) {
            if (!isDatabaseInitialized(con)) {
                File db_script = new File("src/main/resources/floreyesdb.sql");

                if (!db_script.exists()) {
                    Alerta.showAlert("ERROR", "SQL error", "El archivo de script SQL no se encuentra.");
                    return;
                }

                try (FileInputStream fis = new FileInputStream(db_script);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {

                    StringBuilder sqlScript = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sqlScript.append(line).append("\n");
                    }

                    stmt.execute(sqlScript.toString());
                } catch (IOException e) {
                    Alerta.showAlert("ERROR", "Error al leer el archivo", "No se pudo leer el archivo SQL.");
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static boolean isDatabaseInitialized(Connection con) {
        try (Statement stmt = con.createStatement()) {
            stmt.executeQuery("SELECT 1 FROM centro LIMIT 1");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}





