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
                    ps2.setInt(1, entity.getIdPedido());
                    ps2.setInt(2, de.getPro().getIdProducto());
                    ps2.setInt(3, de.getCantidad());
                    ps2.setDouble(4, de.getSubtotal());
                    ps2.executeUpdate();
                }
            }
        } catch (SQLException e) {
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
                        ps2.setInt(1, entity.getIdPedido());
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
        Pedido pedido = null;
        Map<Integer, Pedido> pedidosMap = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_PEDIDO)) {
            ps.setInt(1, pk.getIdPedido());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPedido = rs.getInt("ped.idPedido");
                    if (!pedidosMap.containsKey(idPedido)) {
                        // Crear el pedido si no existe en el mapa
                        Pedido p = new Pedido();
                        p.setIdPedido(idPedido);
                        p.setFechaPedido(rs.getString("ped.fechaPedido"));
                        p.setFechaEntrega(rs.getString("ped.fechaEntrega"));
                        p.setTotal(rs.getDouble("ped.total"));
                        p.setEstado(rs.getString("ped.estado"));
                        p.setUser(userDAO.build().findByPK(new User(rs.getString("ped.telefonoUsuario"))));
                        p.setDetalles(new ArrayList<>()); // Inicializar detalles
                        pedidosMap.put(idPedido, p);
                    }
                    // Añadir detalles al pedido correspondiente
                    Pedido p = pedidosMap.get(idPedido);
                    Producto producto = productoDAO.build().findByPK(new Producto(rs.getInt("pro.idProducto")));
                    Detalles detalle = new Detalles(p, producto, rs.getInt("pp.cantidad"));
                    p.getDetalles().add(detalle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el pedido por PK: " + e.getMessage(), e);
        }
        // Solo hay un pedido esperado para este método
        return pedidosMap.isEmpty() ? null : pedidosMap.values().iterator().next();
    }

    @Override
    public List<Pedido> findAll() {
        Map<Integer, Pedido> pedidosMap = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPedido = rs.getInt("ped.idPedido");
                    if (!pedidosMap.containsKey(idPedido)) {
                        // Crear el pedido si no existe en el mapa
                        Pedido p = new Pedido();
                        p.setIdPedido(idPedido);
                        p.setFechaPedido(rs.getString("ped.fechaPedido"));
                        p.setFechaEntrega(rs.getString("ped.fechaEntrega"));
                        p.setTotal(rs.getDouble("ped.total"));
                        p.setEstado(rs.getString("ped.estado"));
                        p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                        p.setDetalles(new ArrayList<>()); // Inicializar detalles
                        pedidosMap.put(idPedido, p);
                    }
                    // Añadir detalles al pedido correspondiente
                    Pedido p = pedidosMap.get(idPedido);
                    Producto producto = productoDAO.build().findByPK(new Producto(rs.getInt("pro.idProducto")));
                    Detalles detalle = new Detalles(p, producto, rs.getInt("pp.cantidad"));
                    p.getDetalles().add(detalle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los pedidos: " + e.getMessage(), e);
        }
        return new ArrayList<>(pedidosMap.values());
    }

    public List<Pedido> findByUser(User u) {
        Map<Integer, Pedido> pedidosMap = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_USER)) {
            ps.setString(1, u.getPhone());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPedido = rs.getInt("ped.idPedido");
                    if (!pedidosMap.containsKey(idPedido)) {
                        // Crear el pedido si no existe en el mapa
                        Pedido p = new Pedido();
                        p.setIdPedido(idPedido);
                        p.setFechaPedido(rs.getString("ped.fechaPedido"));
                        p.setFechaEntrega(rs.getString("ped.fechaEntrega"));
                        p.setTotal(rs.getDouble("ped.total"));
                        p.setEstado(rs.getString("ped.estado"));
                        p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                        p.setDetalles(new ArrayList<>()); // Inicializar detalles
                        pedidosMap.put(idPedido, p);
                    }
                    // Añadir detalles al pedido correspondiente
                    Pedido p = pedidosMap.get(idPedido);
                    Producto producto = productoDAO.build().findByPK(new Producto(rs.getInt("pro.idProducto")));
                    Detalles detalle = new Detalles(p, producto, rs.getInt("pp.cantidad"));
                    p.getDetalles().add(detalle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pedidos por usuario: " + e.getMessage(), e);
        }
        return new ArrayList<>(pedidosMap.values());
    }


    @Override
    public Pedido delete(Pedido entity) throws SQLException {
        if (entity != null) {
            try (PreparedStatement pst = con.prepareStatement(DELETE)) {
                pst.setString(1, String.valueOf(entity.getIdPedido()));
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
