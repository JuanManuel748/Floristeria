package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.*;
import com.github.JuanManuel.view.Alerta;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class pedidoDAO implements DAO<Pedido>{
    private static final String INSERT = "INSERT INTO Pedido (idPedido, fechaPedido, fechaEntrega, total, estado, telefonoUsuario) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_DETALLES = "INSERT INTO Pedido_Producto (Pedido_idPedido, Producto_idProducto, cantidad, subtotal) VALUES (?,?,?,?)";
    private static final String UPDATE = "UPDATE Pedido SET fechaPedido = ?, fechaEntrega = ?, total = ?, estado = ?, telefonoUsuario = ? WHERE idPedido = ?";
    private static final String DELETE_DETALLES = "DELETE FROM Pedido_Producto WHERE Pedido_idPedido = ?";
    private static final String DELETE = "DELETE FROM Pedido WHERE idPedido = ?";
    private static final String FIND_ALL = "SELECT * FROM Pedido ped JOIN Pedido_Producto pp ON pp.Pedido_idPedido = ped.idPedido JOIN Producto pro ON pro.idProducto = pp.Producto_idProducto";
    private static final String FIND_BY_PEDIDO = "SELECT * FROM Pedido ped JOIN Pedido_Producto pp ON pp.Pedido_idPedido = ped.idPedido JOIN Producto pro ON pro.idProducto = pp.Producto_idProducto WHERE ped.idPedido = ?";
    private static final String FIND_BY_USER = "SELECT * FROM Pedido ped JOIN Pedido_Producto pp ON pp.Pedido_idPedido = ped.idPedido JOIN Producto pro ON pro.idProducto = pp.Producto_idProducto WHERE ped.telefonoUsuario = ?";
    private static final String FIND_STATS_MONTH = "SELECT MONTH(STR_TO_DATE(fechaPedido, '%d/%m/%Y')) AS mes, COUNT(idPedido) AS cantidadPedidos  FROM Pedido GROUP BY mes ORDER BY mes";
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
            List<Detalles> detaller = entity.getDetalles();
            for (Detalles de : detaller) {
                try (PreparedStatement ps2 = con.prepareStatement(INSERT_DETALLES)) {
                    ps2.setInt(1, de.getPed().getIdPedido());
                    ps2.setInt(2, de.getPro().getIdProducto());
                    ps2.setInt(3, de.getCantidad());
                    ps2.setDouble(4, de.getSubtotal());
                    ps2.executeUpdate();
                }
            }
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

            try (PreparedStatement ps3 = con.prepareStatement(DELETE_DETALLES)) {
                ps3.setInt(1, entity.getIdPedido());
                ps3.executeUpdate();
            }
            List<Detalles> detaller = entity.getDetalles();
            for (Detalles de : detaller) {
                int productoId = de.getPro().getIdProducto();

                if (productoDAO.build().findByPK(new Producto(productoId)) != null) {
                    try (PreparedStatement ps2 = con.prepareStatement(INSERT_DETALLES)) {
                        ps2.setInt(1, de.getPed().getIdPedido());
                        ps2.setInt(2, productoId);
                        ps2.setInt(3, de.getCantidad());
                        ps2.setDouble(4, de.getSubtotal());
                        ps2.executeUpdate();
                    }
                } else {
                    Alerta.showAlert("ERROR", "Producto no encontrado", "El producto que intentas insertar no se ha encontrado en la base de datos");
                    return;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pedido findByPK(Pedido pk) {
        Pedido result = null;
        List<Detalles> detalles = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_PEDIDO)) {
            ps.setInt(1, pk.getIdPedido());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    // Atributos Pedido
                    p.setIdPedido(rs.getInt("ped.idPedido"));
                    p.setFechaPedido(rs.getString("ped.fechaPedido"));
                    p.setFechaEntrega(rs.getString("ped.fechaEntrega"));
                    p.setTotal(rs.getDouble("ped.total"));
                    p.setEstado(rs.getString("ped.estado"));
                    p.setUser(userDAO.build().findByPK(new User(rs.getString("ped.telefonoUsuario"))));
                    // Atributos de los detalles del array
                    Detalles det = new Detalles(p, productoDAO.build().findByPK(new Producto(rs.getInt("pro.idProducto"))), rs.getInt("pp.cantidad"));
                    detalles.add(det);

                    p.setDetalles(detalles);
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
        List<Detalles> detalles = new ArrayList<>();
        int compTemp = 0;
        try (PreparedStatement ps = con.prepareStatement(FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    if(compTemp != rs.getInt("ped.idPedido")) {
                        detalles = new ArrayList<>();
                        p.setIdPedido(rs.getInt("ped.idPedido"));
                        p.setFechaPedido(rs.getString("ped.fechaPedido"));
                        p.setFechaEntrega(rs.getString("ped.fechaEntrega"));
                        p.setTotal(rs.getDouble("ped.total"));
                        p.setEstado(rs.getString("ped.estado"));
                        p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                    }
                    Detalles det = new Detalles(p, productoDAO.build().findByPK(new Producto(rs.getInt("pro.idProducto"))), rs.getInt("pp.cantidad"));
                    detalles.add(det);
                    p.setDetalles(detalles);
                    if(compTemp != rs.getInt("ped.idPedido")) {
                        resultLS.add(p);
                    }

                    compTemp = rs.getInt("ped.idPedido");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultLS;
    }

    public List<Pedido> findByUser(User u) {
        List<Pedido> resultLS = new ArrayList<Pedido>();
        List<Detalles> detalles = new ArrayList<>();
        int compTemp = 0;
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_USER)) {
            ps.setString(1, u.getPhone());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    if(compTemp != rs.getInt("ped.idPedido")) {
                        detalles = new ArrayList<>();
                        p.setIdPedido(rs.getInt("ped.idPedido"));
                        p.setFechaPedido(rs.getString("ped.fechaPedido"));
                        p.setFechaEntrega(rs.getString("ped.fechaEntrega"));
                        p.setTotal(rs.getDouble("ped.total"));
                        p.setEstado(rs.getString("ped.estado"));
                        p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                    }
                    Detalles det = new Detalles(p, productoDAO.build().findByPK(new Producto(rs.getInt("pro.idProducto"))), rs.getInt("pp.cantidad"));
                    detalles.add(det);
                    p.setDetalles(detalles);
                    if(compTemp != rs.getInt("ped.idPedido")) {
                        resultLS.add(p);
                    }

                    compTemp = rs.getInt("ped.idPedido");
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

    public Map<String, Integer> FindStatsMonth() {
        Map<String, Integer> result = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_STATS_MONTH)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String month = rs.getString("mes");
                    switch (month) {
                        case "1":
                            month = "ENERO";
                            break;
                        case "2":
                            month = "FEBRERO";
                            break;
                        case "3":
                            month = "MARZO";
                            break;
                        case "4":
                            month = "ABRIL";
                            break;
                        case "5":
                            month = "MAYO";
                            break;
                        case "6":
                            month = "JUNIO";
                            break;
                        case "7":
                            month = "JULIO";
                            break;
                        case "8":
                            month = "AGOSTO";
                            break;
                        case "9":
                            month = "SEPTIEMBRE";
                            break;
                        case "10":
                            month = "OCTUBRE";
                            break;
                        case "11":
                            month = "NOVIEMBRE";
                            break;
                        case "12":
                            month = "DICIEMBRE";
                            break;
                        default:
                            month = "DESCONOCIDO";
                            break;
                    }

                    int cantidad = rs.getInt("cantidadPedidos");

                    result.put(month, cantidad);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
