package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.Pedido;
import com.github.JuanManuel.model.entity.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class pedidoDAO implements DAO<Pedido>{
    private static final String INSERT = "INSERT INTO Pedido (idPedido, fechaPedido, fechaEntrega, total, estado, telefonoUsuario) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE Pedido SET fechaPedido = ?, fechaEntrega = ?, total = ?, estado = ?, telefonoUsuario = ? WHERE idPedido = ?";
    private static final String DELETE = "DELETE FROM Pedido WHERE idPedido = ?";
    private static final String FIND_ALL = "SELECT * FROM Pedido";
    private static final String FIND_BY_PK = "SELECT * FROM Pedido WHERE idPedido = ?";
    private static final String FIND_BY_USER = "SELECT * FROM Pedido WHERE telefonoUsuario = ?";

    private Connection con;

    public pedidoDAO() {con = MySQLConnection.getConnection();}

    @Override
    public Pedido save(Pedido entity) {
        if (entity == null) {
            return null;
        }
        if (findByPK(entity) == null) {
            insertPedido(entity);
        } else {
            updatePedido(entity);
        }
        return entity;
    }

    public void insertPedido(Pedido entity) {
        try (PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setInt(1, entity.getIdPedido());
            ps.setString(2, entity.getFechaPedido());
            ps.setString(3, entity.getFechaEntrega());
            ps.setDouble(4, entity.getTotal());
            ps.setString(5, entity.getEstado());
            ps.setString(6, entity.getUser().getPhone());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERROR EN INSERT EN userDAO");
            e.printStackTrace();
        }
    }

    public void updatePedido(Pedido entity) {
        try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getFechaPedido());
            ps.setString(2, entity.getFechaEntrega());
            ps.setDouble(3, entity.getTotal());
            ps.setString(4, entity.getEstado());
            ps.setString(5, entity.getUser().getPhone());
            ps.setInt(6, entity.getIdPedido());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERROR EN INSERT EN userDAO");
            e.printStackTrace();
        }
    }

    @Override
    public Pedido findByPK(Pedido pk) {
        Pedido result = null;
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_PK)) {
            ps.setInt(1, pk.getIdPedido());
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    Pedido p = new Pedido();
                    p.setIdPedido(rs.getInt("idPedido"));
                    p.setFechaPedido(rs.getString("fechaPedido"));
                    p.setFechaEntrega(rs.getString("fechaEntrega"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                    result = p;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<Pedido> findAll() {
        List<Pedido> resultLS = new ArrayList<Pedido>();
        try (PreparedStatement ps = con.prepareStatement(FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setIdPedido(rs.getInt("idPedido"));
                    p.setFechaPedido(rs.getString("fechaPedido"));
                    p.setFechaEntrega(rs.getString("fechaEntrega"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                    resultLS.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultLS;
    }

    public List<Pedido> findByUser(User u) {
        List<Pedido> resultLS = new ArrayList<Pedido>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_USER)) {
            ps.setString(1, u.getPhone());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setIdPedido(rs.getInt("idPedido"));
                    p.setFechaPedido(rs.getString("fechaPedido"));
                    p.setFechaEntrega(rs.getString("fechaEntrega"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                    resultLS.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultLS;
    }

    @Override
    public Pedido delete(Pedido entity) throws SQLException {
        if (entity != null) {
            try (PreparedStatement pst = con.prepareStatement(DELETE)) {
                pst.setString(1, String.valueOf(entity.getIdPedido()));
                pst.executeUpdate();
            } catch (SQLException e) {
                System.out.println("ERROR EN DELETE EN userDAO");
                e.printStackTrace();
                entity = null;
            }
        }
        return entity;
    }

    @Override
    public void close() throws IOException {

    }

    public static pedidoDAO build() {
        return new pedidoDAO();
    }

    public void setPagado(Pedido p) {
        Pedido result = findByPK(p);
        result.setEstado("PAGADO");
        updatePedido(result);
    }
}
