package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.Ramo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ramoDAO implements DAO<Ramo>{
    private static final String INSERT = "INSERT INTO Ramo (idRamo, florPrincipal, cantidadFlores, colorEnvoltorio) VALUES  (?,?,?,?)";
    private static final String UPDATE = "UPDATE Ramo SET florPrincipal = ?, cantidadFlores = ?, colorEnvoltorio = ? WHERE idRamo = ?";
    private static final String DELETE = "DELETE FROM Ramo WHERE idRamo = ?";
    private static final String FIND_ALL = "SELECT * FROM Ramo";
    private static final String FIND_BY_PHONE = "SELECT * FROM Ramo WHERE idRamo = ?";



    private Connection con;

    public ramoDAO() {
        con = MySQLConnection.getConnection();
    }
    @Override
    public Ramo save(Ramo entity) {
        if (entity == null) {
            return null;
        }
        if (findByPK(entity) == null) {
            insertRamo(entity);
        } else {
            updateRamo(entity);
        }
        return entity;
    }

    @Override
    public Ramo delete(Ramo entity) throws SQLException {
        return null;
    }

    public void insertRamo(Ramo entity) {

    }

    public void updateRamo(Ramo entity) {

    }

    @Override
    public Ramo findByPK(Ramo pk) {
        return null;
    }

    @Override
    public List<Ramo> findAll() {
        return List.of();
    }

    @Override
    public void close() throws IOException {

    }
}
