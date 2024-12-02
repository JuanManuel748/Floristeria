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
    private static final String FIND_ALL = "SELECT * FROM Pedido";
    private static final String FIND_BY_PK = "SELECT * FROM Pedido WHERE idPedido = ?";
    private static final String FIND_BY_USER = "SELECT * FROM Pedido WHERE telefonoUsuario = ?";
    private static final String FIND_DETAILS = "SELECT * FROM Pedido_Producto WHERE Pedido_idPedido = ?";
    private static final String FIND_STATS_MONTH = "SELECT MONTH(STR_TO_DATE(fechaPedido, '%d/%m/%Y')) AS mes, COUNT(idPedido) AS cantidadPedidos  FROM Pedido GROUP BY mes ORDER BY mes";

    private Connection con;

    /**
     * Constructor that initializes the connection to the database.
     */
    public pedidoDAO() {con = MySQLConnection.getConnection();}

    /**
     * Saves a Pedido (order) entity to the database.
     * If the order doesn't exist, it will be inserted; otherwise, it will be updated.
     *
     * @param entity The Pedido object to be saved.
     * @return The saved Pedido entity.
     */
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

    /**
     * Inserts a new Pedido (order) into the database.
     * Also inserts the associated Pedido_Producto details.
     *
     * @param entity The Pedido object to be inserted.
     */
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
            if (detaller != null && detaller.size() > 0) {
                for (Detalles de : detaller) {
                    try (PreparedStatement ps2 = con.prepareStatement(INSERT_DETALLES)) {
                        ps2.setInt(1, entity.getIdPedido());
                        ps2.setInt(2, de.getPro().getIdProducto());
                        ps2.setInt(3, de.getCantidad());
                        ps2.setDouble(4, de.getSubtotal());
                        ps2.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing Pedido (order) in the database.
     * Also updates the associated Pedido_Producto details.
     *
     * @param entity The Pedido object to be updated.
     */
    public void updatePedido(Pedido entity) {
        try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getFechaPedido());
            ps.setString(2, entity.getFechaEntrega());
            ps.setDouble(3, entity.getTotal());
            ps.setString(4, entity.getEstado());
            ps.setString(5, entity.getUser().getPhone());
            ps.setInt(6, entity.getIdPedido());
            ps.executeUpdate();


            List<Detalles> detaller = entity.getDetalles();
            if (detaller != null && detaller.size() > 0) {
                try (PreparedStatement ps3 = con.prepareStatement(DELETE_DETALLES)) {
                    ps3.setInt(1, entity.getIdPedido());
                    ps3.executeUpdate();
                }
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds a Pedido by its primary key (idPedido).
     *
     * @param pk The Pedido object containing the idPedido to search for.
     * @return The found Pedido entity, or null if not found.
     */
    @Override
    public Pedido findByPK(Pedido pk) {
        Pedido pedido = null;
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_PK)) {
            ps.setInt(1, pk.getIdPedido());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPedido = rs.getInt("idPedido");
                    Pedido p = new Pedido();
                    p.setIdPedido(idPedido);
                    p.setFechaPedido(rs.getString("fechaPedido"));
                    p.setFechaEntrega(rs.getString("fechaEntrega"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                    p.setDetalles(new ArrayList<>());

                    try (PreparedStatement ps2 = con.prepareStatement(FIND_DETAILS)) {
                        List<Detalles> detallesList = new ArrayList<>();
                        ps2.setInt(1, p.getIdPedido());
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            while(rs2.next()) {
                                Detalles d = new Detalles();
                                d.setPed(p);
                                Producto producto = productoDAO.build().findByPK(new Producto(rs2.getInt("Producto_idProducto")));
                                d.setPro(producto);
                                d.setCantidad(rs2.getInt("cantidad"));
                                d.setSubtotal(rs2.getDouble("subtotal"));
                                detallesList.add(d);
                            }
                        }
                        p.setDetalles(detallesList);
                    }
                    pedido = p;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el pedido por PK: " + e.getMessage(), e);
        }
        return pedido;
    }

    /**
     * Finds all Pedidos in the database.
     *
     * @return A list of all Pedido entities.
     */
    @Override
    public List<Pedido> findAll() {
        List<Pedido> allLS = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_ALL)){
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPedido = rs.getInt("idPedido");
                    Pedido p = new Pedido();
                    p.setIdPedido(idPedido);
                    p.setFechaPedido(rs.getString("fechaPedido"));
                    p.setFechaEntrega(rs.getString("fechaEntrega"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                    p.setDetalles(new ArrayList<>());

                    try (PreparedStatement ps2 = con.prepareStatement(FIND_DETAILS)) {
                        List<Detalles> detallesList = new ArrayList<>();
                        ps2.setInt(1, p.getIdPedido());
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            while(rs2.next()) {
                                Detalles d = new Detalles();
                                d.setPed(p);
                                Producto producto = productoDAO.build().findByPK(new Producto(rs2.getInt("Producto_idProducto")));
                                d.setPro(producto);
                                d.setCantidad(rs2.getInt("cantidad"));
                                d.setSubtotal(rs2.getDouble("subtotal"));
                                detallesList.add(d);
                            }
                        }
                        p.setDetalles(detallesList);
                    }
                    allLS.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los pedidos: " + e.getMessage(), e);
        }
        return allLS;
    }

    /**
     * Retrieves a list of orders (Pedidos) placed by a specific user.
     *
     * @param u The user whose orders are being retrieved.
     * @return A list of Pedido objects associated with the provided user.
     */
    public List<Pedido> findByUser(User u) {
        List<Pedido> byUserLS = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_USER)){
            ps.setString(1, u.getPhone());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPedido = rs.getInt("idPedido");
                    Pedido p = new Pedido();
                    p.setIdPedido(idPedido);
                    p.setFechaPedido(rs.getString("fechaPedido"));
                    p.setFechaEntrega(rs.getString("fechaEntrega"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setUser(userDAO.build().findByPK(new User(rs.getString("telefonoUsuario"))));
                    p.setDetalles(new ArrayList<>());

                    try (PreparedStatement ps2 = con.prepareStatement(FIND_DETAILS)) {
                        List<Detalles> detallesList = new ArrayList<>();
                        ps2.setInt(1, p.getIdPedido());
                        try (ResultSet rs2 = ps2.executeQuery()) {
                            while(rs2.next()) {
                                Detalles d = new Detalles();
                                d.setPed(p);
                                Producto producto = productoDAO.build().findByPK(new Producto(rs2.getInt("Producto_idProducto")));
                                d.setPro(producto);
                                d.setCantidad(rs2.getInt("cantidad"));
                                d.setSubtotal(rs2.getDouble("subtotal"));
                                detallesList.add(d);
                            }
                        }
                        p.setDetalles(detallesList);
                    }
                    byUserLS.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los pedidos: " + e.getMessage(), e);
        }
        return byUserLS;
    }

    /**
     * Deletes a given Pedido from the database.
     *
     * @param entity The Pedido to be deleted.
     * @return The deleted Pedido, or null if an error occurred during the deletion.
     * @throws SQLException If there is an issue executing the delete statement.
     */
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

    /**
     * Closes any resources used by the DAO. Currently, does nothing, but can be expanded if needed.
     *
     * @throws IOException If there is an issue closing resources (not implemented in this case).
     */
    @Override
    public void close() throws IOException {

    }

    /**
     * Static method to create a new instance of the pedidoDAO.
     *
     * @return A new instance of pedidoDAO.
     */
    public static pedidoDAO build() {
        return new pedidoDAO();
    }

    /**
     * Updates the state of the specified Pedido to "PAGADO" (Paid) and performs the necessary database update.
     *
     * @param p The Pedido to be marked as paid.
     */
    public void setPagado(Pedido p) {
        Pedido result = findByPK(p);
        result.setEstado("PAGADO");
        updatePedido(result);
    }

    /**
     * Retrieves the monthly statistics of orders, including the count of orders placed in each month.
     *
     * @return A map where the key is the month name and the value is the number of orders placed in that month.
     */
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
