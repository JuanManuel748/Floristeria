package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.Flor;
import com.github.JuanManuel.model.entity.Producto;
import org.h2.util.json.JSONItemType;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class florDAO implements DAO<Flor>{
    private static final String INSERT = "INSERT INTO Flor (idFlor, color, tipo) VALUES  (?,?,?)";
    private static final String UPDATE = "UPDATE Flor SET color = ?, tipo = ? WHERE idFlor = ?";
    private static final String DELETE = "DELETE FROM Flor WHERE idFlor = ?";
    private static final String FIND_ALL = "SELECT * FROM Flor";
    private static final String FIND_BY_PK = "SELECT * FROM Flor WHERE idFlor = ?";
    private static final String FIND_BY_TYPE = "SELECT * FROM Flor WHERE tipo = ?";
    private static final String FIND_BY_NAME = "SELECT f.*, p.* FROM Flor f JOIN Producto p ON p.idProducto = f.idFlor WHERE LOWER(p.nombre) LIKE ?";


    private Connection con;
    public florDAO() {con = MySQLConnection.getConnection();}
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

    public void insertFlor(Flor entity) {
        try {
            productoDAO.build().insertProducto(entity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setInt(1, entity.getIdFlor());
            ps.setString(2, entity.getColor());
            ps.setBoolean(3, entity.getTipoFlor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateFlor(Flor entity) {
        productoDAO.build().updateProducto(entity);
        try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getNombre());
            ps.setBoolean(2, entity.getTipoFlor());
            ps.setInt(3, entity.getIdFlor());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Flor delete(Flor entity) throws SQLException {
        if (entity != null) {
            try (PreparedStatement ps = con.prepareStatement(DELETE)) {
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

    @Override
    public Flor findByPK(Flor pk) {
        Producto pro = productoDAO.build().findByPK(pk);
        Flor result = null;
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_PK)) {
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

    @Override
    public List<Flor> findAll() {
        List<Flor> result = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_ALL)) {
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

    public List<Flor> findByType(boolean tipo) {
        List<Flor> result = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_TYPE)) {
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


    public List<Flor> findByName(String name) {
        List<Flor> result = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_NAME)) {
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

    @Override
    public void close() throws IOException {

    }

    public static florDAO build() {
        return new florDAO();
    }
}
