package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class centroDAO implements DAO<Centro>{

    private Connection con;
    public centroDAO() {con = MySQLConnection.getConnection();}

    @Override
    public Centro save(Centro entity) {
        return null;
    }

    @Override
    public Centro delete(Centro entity) throws SQLException {
        return null;
    }

    @Override
    public Centro findByPK(Centro pk) {
        return null;
    }

    @Override
    public List<Centro> findAll() {
        return List.of();
    }

    @Override
    public void close() throws IOException {

    }

    public static centroDAO build() {
        return new centroDAO();
    }
}
