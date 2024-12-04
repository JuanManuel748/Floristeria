package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.Flor;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.view.WelcomeController;
import org.h2.util.json.JSONItemType;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class florDAO implements DAO<Flor>{
    private static final String SQL_INSERT = "INSERT INTO flor (idFlor, color, tipo) VALUES  (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE flor SET color = ?, tipo = ? WHERE idFlor = ?";
    private static final String SQL_DELETE = "DELETE FROM flor WHERE idFlor = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM flor";
    private static final String SQL_FIND_BY_PK = "SELECT * FROM flor WHERE idFlor = ?";
    private static final String SQL_FIND_BY_TYPE = "SELECT * FROM flor WHERE tipo = ?";
    private static final String SQL_FIND_BY_NAME = "SELECT f.*, p.* FROM flor f JOIN producto p ON p.idProducto = f.idFlor WHERE LOWER(p.nombre) LIKE ?";
    // ==============
    private static final String H2_INSERT = "INSERT INTO \"flor\" (\"idFlor\", \"color\", \"tipo\") VALUES  (?,?,?)";
    private static final String H2_UPDATE = "UPDATE \"flor\" SET \"color\" = ?, \"tipo\" = ? WHERE \"idFlor\" = ?";
    private static final String H2_DELETE = "DELETE FROM \"flor\" WHERE \"idFlor\" = ?";
    private static final String H2_FIND_ALL = "SELECT * FROM \"flor\"";
    private static final String H2_FIND_BY_PK = "SELECT * FROM \"flor\" WHERE \"idFlor\" = ?";
    private static final String H2_FIND_BY_TYPE = "SELECT * FROM \"flor\" WHERE \"tipo\" = ?";
    private static final String H2_FIND_BY_NAME = "SELECT f.*, p.* FROM \"flor\" f JOIN \"producto\" p ON p.\"idProducto\" = f.\"idFlor\" WHERE LOWER(p.\"nombre\") LIKE ?";


    private Connection con;
    private boolean sql;

    /**
     * Constructor that initializes the database connection.
     */
    public florDAO() {
        con = WelcomeController.mainCon;
        sql = WelcomeController.isSQL;
    }

    /**
     * Saves a Flor entity. Inserts it if it does not exist; updates it otherwise.
     *
     * @param entity the Flor entity to save
     * @return the saved Flor entity
     */
    @Override
    public Flor save(Flor entity) {
        if (entity == null) {
            return null;
        }
        if (findByPK(entity) == null) {
            insertFlor(entity);
        } else {
            updateFlor(entity);
        }
        return entity;
    }

    /**
     * Inserts a new Flor entity into the database.
     *
     * @param entity the Flor entity to insert
     */
    public void insertFlor(Flor entity) {
        productoDAO.build().insertProducto(entity);
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_INSERT);
            } else {
                ps = con.prepareStatement(H2_INSERT);
            }
            ps.setInt(1, entity.getIdFlor());
            ps.setString(2, entity.getColor());
            ps.setBoolean(3, entity.getTipoFlor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing Flor entity in the database.
     *
     * @param entity the Flor entity to update
     */
    public void updateFlor(Flor entity) {
        productoDAO.build().updateProducto(entity);
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_UPDATE);
            } else {
                ps = con.prepareStatement(H2_UPDATE);
            }
            ps.setString(1, entity.getColor());
            ps.setBoolean(2, entity.getTipoFlor());
            ps.setInt(3, entity.getIdFlor());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a Flor entity from the database.
     *
     * @param entity the Flor entity to delete
     * @return the deleted Flor entity, or null if an error occurred
     * @throws SQLException if a database error occurs
     */
    @Override
    public Flor delete(Flor entity) throws SQLException {
        if (entity != null) {
            try {
                PreparedStatement ps;
                if (sql) {
                    ps = con.prepareStatement(SQL_DELETE);
                } else {
                    ps = con.prepareStatement(H2_DELETE);
                }
                ps.setInt(1, entity.getIdProducto());
                ps.executeUpdate();
                productoDAO.build().delete(entity);
            } catch (SQLException e) {
                e.printStackTrace();
                entity = null;
            }
        }
        return entity;
    }

    /**
     * Finds a Flor entity by its primary key (id).
     *
     * @param pk the Flor entity containing the primary key to search for
     * @return the found Flor entity, or null if not found
     */
    @Override
    public Flor findByPK(Flor pk) {
        Producto pro = productoDAO.build().findByPK(pk);
        Flor result = null;
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_PK);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_PK);
            }
            ps.setInt(1, pk.getIdProducto());
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Flor f = new Flor();
                    // Atributos producto
                    f.setIdProducto(pro.getIdProducto());
                    f.setNombre(pro.getNombre());
                    f.setPrecio(pro.getPrecio());
                    f.setStock(pro.getStock());
                    f.setTipo(pro.getTipo());
                    f.setDescripcion(pro.getDescripcion());
                    f.setImg(pro.getImg());
                    // Atributos flor
                    f.setIdFlor(rs.getInt("idFlor"));
                    f.setColor(rs.getString("color"));
                    f.setTipoFlor(rs.getBoolean("tipo"));
                    result = f;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Retrieves all Flor entities from the database.
     *
     * @return a list of all Flor entities
     */
    @Override
    public List<Flor> findAll() {
        List<Flor> result = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_ALL);
            } else {
                ps = con.prepareStatement(H2_FIND_ALL);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Flor f = new Flor();
                    // Atributos producto
                    Producto pro = productoDAO.build().findByPK(new Producto(rs.getInt("idFlor")));
                    f.setIdProducto(pro.getIdProducto());
                    f.setNombre(pro.getNombre());
                    f.setPrecio(pro.getPrecio());
                    f.setStock(pro.getStock());
                    f.setTipo(pro.getTipo());
                    f.setDescripcion(pro.getDescripcion());
                    f.setImg(pro.getImg());
                    // Atributos flor
                    f.setIdFlor(rs.getInt("idFlor"));
                    f.setColor(rs.getString("color"));
                    f.setTipoFlor(rs.getBoolean("tipo"));
                    result.add(f);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Finds Flor entities by their type (true/false).
     *
     * @param tipo the type of Flor to search for
     * @return a list of Flor entities matching the type
     */
    public List<Flor> findByType(boolean tipo) {
        List<Flor> result = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_TYPE);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_TYPE);
            }
            ps.setBoolean(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Flor f = new Flor();
                    // Atributos producto
                    Producto pro = productoDAO.build().findByPK(new Producto(rs.getInt("idFlor")));
                    f.setIdProducto(pro.getIdProducto());
                    f.setNombre(pro.getNombre());
                    f.setPrecio(pro.getPrecio());
                    f.setStock(pro.getStock());
                    f.setTipo(pro.getTipo());
                    f.setDescripcion(pro.getDescripcion());
                    f.setImg(pro.getImg());
                    // Atributos flor
                    f.setIdFlor(rs.getInt("idFlor"));
                    f.setColor(rs.getString("color"));
                    f.setTipoFlor(rs.getBoolean("tipo"));
                    result.add(f);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Finds Flor entities by their associated product name.
     *
     * @param name the partial name to search for
     * @return a list of Flor entities matching the name
     */
    public List<Flor> findByName(String name) {
        List<Flor> result = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_NAME);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_NAME);
            }
            ps.setString(1, "%" + name.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Flor f = new Flor();
                    // Atributos producto
                    Producto pro = productoDAO.build().findByPK(new Producto(rs.getInt("f.idFlor")));
                    f.setIdProducto(pro.getIdProducto());
                    f.setNombre(pro.getNombre());
                    f.setPrecio(pro.getPrecio());
                    f.setStock(pro.getStock());
                    f.setTipo(pro.getTipo());
                    f.setDescripcion(pro.getDescripcion());
                    f.setImg(pro.getImg());
                    // Atributos flor
                    f.setIdFlor(rs.getInt("f.idFlor"));
                    f.setColor(rs.getString("f.color"));
                    f.setTipoFlor(rs.getBoolean("f.tipo"));
                    result.add(f);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Closes any open resources. Currently, no resources to close.
     */
    @Override
    public void close() throws IOException {}

    /**
     * Factory method to create a new florDAO instance.
     *
     * @return a new florDAO instance
     */
    public static florDAO build() {
        return new florDAO();
    }
}
