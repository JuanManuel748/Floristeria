package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.view.Alerta;
import com.github.JuanManuel.view.WelcomeController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class productoDAO  implements DAO<Producto>{
    private static final String SQL_INSERT = "INSERT INTO producto (idProducto, nombre, precio, stock, tipo, description, imagen) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE producto SET nombre = ?, precio = ?, stock = ?, tipo = ?, description = ?, imagen = ? WHERE idProducto = ?";
    private static final String SQL_DELETE = "DELETE FROM producto WHERE idProducto = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM producto";
    private static final String SQL_FIND_BY_PK = "SELECT * FROM producto WHERE idProducto = ?";
    private static final String SQL_FIND_BY_TYPE = "SELECT * FROM producto WHERE tipo = ?";
    private static final String SQL_FIND_COMP_BY_NAME = "SELECT * FROM producto WHERE LOWER(nombre) LIKE ? AND (tipo = ? OR tipo = ?)";
    private static final String SQL_FIND_GROUPBY = "SELECT p.*, SUM(pp.cantidad) AS cantidadVendida FROM producto p JOIN Pedido_Producto pp ON p.idProducto = pp.Producto_idProducto WHERE p.tipo = ? GROUP BY p.nombre HAVING cantidadVendida >= ?";
    // ===============
    private static final String H2_INSERT = "INSERT INTO \"producto\" (\"idProducto\", \"nombre\", \"precio\", \"stock\", \"tipo\", \"description\", \"imagen\") VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String H2_UPDATE = "UPDATE \"producto\" SET \"nombre\" = ?, \"precio\" = ?, \"stock\" = ?, \"tipo\" = ?, \"description\" = ?, \"imagen\" = ? WHERE \"idProducto\" = ?";
    private static final String H2_DELETE = "DELETE FROM \"producto\" WHERE \"idProducto\" = ?";
    private static final String H2_FIND_ALL = "SELECT * FROM \"producto\"";
    private static final String H2_FIND_BY_PK = "SELECT * FROM \"producto\" WHERE \"idProducto\" = ?";
    private static final String H2_FIND_BY_TYPE = "SELECT * FROM \"producto\" WHERE \"tipo\" = ?";
    private static final String H2_FIND_COMP_BY_NAME = "SELECT * FROM \"producto\" WHERE LOWER(\"nombre\") LIKE ? AND (\"tipo\" = ? OR \"tipo\" = ?)";
    private static final String H2_FIND_GROUPBY = "SELECT p.*, SUM(pp.\"cantidad\") AS \"cantidadVendida\" FROM \"producto\" p JOIN \"Pedido_Producto\" pp ON p.\"idProducto\" = pp.\"Producto_idProducto\" WHERE p.\"tipo\" = ? GROUP BY p.\"nombre\" HAVING \"cantidadVendida\" >= ?";

    private Connection con;
    private boolean sql;

    /**
     * Constructor that initializes the connection to the database.
     */
    public productoDAO() {
        con = WelcomeController.mainCon;
        sql = WelcomeController.isSQL;
    }

    /**
     * Saves a Producto entity to the database. If the product exists, it is updated, otherwise it is inserted.
     *
     * @param entity The Producto to be saved.
     * @return The saved Producto entity.
     */
    @Override
    public Producto save(Producto entity) {
        if (entity == null) {
            return null;
        }
        if (findByPK(entity) == null) {
            insertProducto(entity);
        } else {
            updateProducto(entity);
        }
        return entity;
    }

    /**
     * Inserts a new Producto into the database.
     *
     * @param entity The Producto entity to be inserted.
     */
    public void insertProducto(Producto entity){
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_INSERT);
            } else {
                ps = con.prepareStatement(H2_INSERT);
            }
            ps.setInt(1, entity.getIdProducto());
            ps.setString(2, entity.getNombre());
            ps.setDouble(3, entity.getPrecio());
            ps.setInt(4, entity.getStock());
            ps.setString(5, entity.getTipo());
            ps.setString(6, entity.getDescripcion());
            ps.setBytes(7, entity.getImg());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing Producto in the database.
     *
     * @param entity The Producto entity to be updated.
     */
    public void updateProducto(Producto entity){
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_UPDATE);
            } else {
                ps = con.prepareStatement(H2_UPDATE);
            }
            ps.setString(1, entity.getNombre());
            ps.setDouble(2, entity.getPrecio());
            ps.setInt(3, entity.getStock());
            ps.setString(4, entity.getTipo());
            ps.setString(5, entity.getDescripcion());
            ps.setBytes(6, entity.getImg());
            ps.setInt(7, entity.getIdProducto());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds a Producto by its primary key (idProducto).
     *
     * @param pk The Producto entity with the idProducto to search for.
     * @return The Producto with the specified idProducto, or null if not found.
     */
    @Override
    public Producto findByPK(Producto pk) {
        Producto result = null;
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_PK);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_PK);
            }
            ps.setInt(1, pk.getIdProducto());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("idProducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setStock(rs.getInt("stock"));
                    p.setTipo(rs.getString("tipo"));
                    p.setDescripcion(rs.getString("description"));
                    p.setImg(rs.getBytes("imagen"));
                    result = p;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Retrieves all Producto entities from the database.
     *
     * @return A list of all Producto entities.
     */
    @Override
    public List<Producto> findAll() {
        List <Producto> result = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_ALL);
            } else {
                ps = con.prepareStatement(H2_FIND_ALL);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("idProducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setStock(rs.getInt("stock"));
                    p.setTipo(rs.getString("tipo"));
                    p.setDescripcion(rs.getString("description"));
                    p.setImg(rs.getBytes("imagen"));
                    result.add(p);
                }
            }
        } catch (SQLException e) {
        throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Finds Producto entities by their type.
     *
     * @param type The type of the Producto to search for.
     * @return A list of Producto entities with the specified type.
     */
    public List<Producto> findByType(String type) {
        List<Producto> result = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_TYPE);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_TYPE);
            }
            ps.setString(1, type);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("idProducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setStock(rs.getInt("stock"));
                    p.setTipo(rs.getString("tipo"));
                    p.setDescripcion(rs.getString("description"));
                    p.setImg(rs.getBytes("imagen"));
                    result.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Finds Producto entities with a sales quantity greater than or equal to a specified number, grouped by their type.
     *
     * @param quantity The minimum quantity of products sold.
     * @param type The type of the Producto to search for.
     * @return A list of Producto entities matching the criteria.
     */
    public List<Producto> findGroupBy(int quantity, String type) {
        List<Producto> result = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_GROUPBY);
            } else {
                ps = con.prepareStatement(H2_FIND_GROUPBY);
            }
            ps.setString(1, type);
            ps.setInt(2, quantity);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("p.idProducto"));
                    p.setNombre(rs.getString("p.nombre"));
                    p.setPrecio(rs.getDouble("p.precio"));
                    p.setStock(rs.getInt("p.stock"));
                    p.setTipo(rs.getString("p.tipo"));
                    p.setDescripcion(rs.getString("p.description"));
                    p.setImg(rs.getBytes("p.imagen"));
                    p.setCantidadVendida(rs.getInt("cantidadVendida"));
                    result.add(p);
                }
            }
        } catch (SQLException e) {
            Alerta.showAlert("ERROR", "Ningún producto encontrado", "No se han encontrado ningun producto con cantidadVendidad >= " + quantity  + " de tipo " + type);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Finds Producto entities based on a partial name match and type.
     *
     * @param name The partial name to search for.
     * @param tipo1 The first type to search for.
     * @param tipo2 The second type to search for.
     * @return A list of Producto entities that match the name and types.
     */
    public List<Producto> findByComplName(String name, String tipo1, String tipo2) {
        List<Producto> result = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_COMP_BY_NAME);
            } else {
                ps = con.prepareStatement(H2_FIND_COMP_BY_NAME);
            }
            ps.setString(1, "%" + name.toLowerCase() + "%");
            ps.setString(2, tipo1);
            ps.setString(3, tipo2);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setIdProducto(rs.getInt("idProducto"));
                    p.setNombre(rs.getString("nombre"));
                    p.setPrecio(rs.getDouble("precio"));
                    p.setStock(rs.getInt("stock"));
                    p.setTipo(rs.getString("tipo"));
                    p.setDescripcion(rs.getString("description"));
                    p.setImg(rs.getBytes("imagen"));
                    result.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Deletes a Producto entity from the database.
     *
     * @param entity The Producto to be deleted.
     * @return The deleted Producto entity, or null if not found.
     * @throws SQLException If an SQL error occurs.
     */
    @Override
    public Producto delete(Producto entity) throws SQLException {
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
            } catch (SQLException e) {
                e.printStackTrace();
                entity = null;
            }
        }
        return entity;
    }

    /**
     * Closes the DAO, releasing any resources.
     *
     * @throws IOException If an error occurs while closing the connection.
     */
    @Override
    public void close() throws IOException {

    }

    /**
     * Builds and returns a new instance of productoDAO.
     *
     * @return A new instance of productoDAO.
     */
    public static productoDAO build() {
        return new productoDAO();
    }
}
