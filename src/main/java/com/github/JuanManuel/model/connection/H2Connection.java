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

    /**
     * Retrieves a temporary connection to the H2 database.
     * If no connection exists, it initializes one using the specified URL, username, and password.
     *
     * @return a Connection object to the H2 database.
     * @throws RuntimeException if the connection cannot be established.
     */
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

    /**
     * Closes the current temporary connection to the H2 database, if one exists.
     * If the connection is already closed or null, this method does nothing.
     */
    public static void closeTEMPConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException sqlE) {
                sqlE.printStackTrace();
            }
        }
    }

    /**
     * Loads the database schema and initial data from an external SQL script.
     * If the database is not already initialized, it reads and executes the script from
     * the file "floreyesdb.sql" located in the resources directory.
     *
     * If the script file is not found or cannot be read, it displays an alert.
     */
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

    /**
     * Checks if the database is already initialized by querying a known table.
     *
     * @param con the Connection object to the H2 database.
     * @return true if the database is initialized, false otherwise.
     */
    private static boolean isDatabaseInitialized(Connection con) {
        try (Statement stmt = con.createStatement()) {
            stmt.executeQuery("SELECT 1 FROM centro LIMIT 1");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}





