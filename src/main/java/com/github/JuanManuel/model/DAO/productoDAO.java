package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.view.Alerta;

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
    private static final String FIND_COMP_BY_NAME = "SELECT * FROM Producto WHERE LOWER(nombre) LIKE ? AND (tipo = ? OR tipo = ?)";
    private static final String FIND_GROUPBY = "SELECT p.*, SUM(pp.cantidad) AS cantidadVendida FROM Producto p JOIN Pedido_Producto pp ON p.idProducto = pp.Producto_idProducto WHERE p.tipo = ? GROUP BY p.nombre HAVING cantidadVendida >= ?";
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

    public List<Producto> findByType(String type) {
        List<Producto> result = new ArrayList<>();
        //FIND_BY_TYPE = "SELECT * FROM Producto WHERE tipo = ?"
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_TYPE)) {
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

    public List<Producto> findGroupBy(int quantity, String type) {
        List<Producto> result = new ArrayList<>();
        //FIND_BY_TYPE = "SELECT * FROM Producto WHERE tipo = ?"
        try (PreparedStatement ps = con.prepareStatement(FIND_GROUPBY)) {
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

    public List<Producto> findByComplName(String name, String tipo1, String tipo2) {
        List<Producto> result = new ArrayList<>();
        //FIND_BY_TYPE = "SELECT * FROM Producto WHERE tipo = ?"
        try (PreparedStatement ps = con.prepareStatement(FIND_COMP_BY_NAME)) {
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

    @Override
    public Producto delete(Producto entity) throws SQLException {
        if (entity != null) {
            try (PreparedStatement pst = con.prepareStatement(DELETE)) {
                pst.setInt(1, entity.getIdProducto());
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
