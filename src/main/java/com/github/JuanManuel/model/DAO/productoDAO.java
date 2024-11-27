package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.Producto;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class productoDAO  implements DAO<Producto>{
    private static final String INSERT = "INSERT INTO Producto (idProducto, nombre, precio, stock, tipo, description, imagen) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Producto SET nombre = ?, precio = ?, stock = ?, tipo = ?, description = ?, imagen = ? WHERE idProducto = ?";
    private static final String DELETE = "DELETE FROM Producto WHERE idProducto = ?";
    private static final String FIND_ALL = "SELECT * FROM Producto";
    private static final String FIND_BY_PK = "SELECT * FROM Producto WHERE idProducto = ?";
    private static final String FIND_BY_TYPE = "SELECT * FROM Producto WHERE tipo = ?";

    private Connection con;

    public productoDAO() { con = MySQLConnection.getConnection();}

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



    public void insertProducto(Producto entity){
        try (PreparedStatement ps = con.prepareStatement(INSERT)) {
            //idProducto, nombre, precio, stock, tipo, description, imagen
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

    public void updateProducto(Producto entity){
        try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
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

    @Override
    public Producto findByPK(Producto pk) {
        Producto result = null;
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_PK)) {
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

    @Override
    public List<Producto> findAll() {
        List <Producto> result = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_ALL)) {
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

    public List<Producto> findByTipo(String tipo) {
        List<Producto> result = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_TYPE)) {
            ps.setString(1, tipo);
            try (ResultSet rs = ps.executeQuery()) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Producto delete(Producto entity) throws SQLException {
        if (entity != null) {
            try (PreparedStatement pst = con.prepareStatement(DELETE)) {
                pst.setString(1, String.valueOf(entity.getIdProducto()));
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                entity = null;
            }
        }
        return entity;
    }

    @Override
    public void close() throws IOException {

    }

    public static productoDAO build() {
        return new productoDAO();
    }
}
