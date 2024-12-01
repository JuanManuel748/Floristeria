package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.Flor;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.entity.Ramo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ramoDAO implements DAO<Ramo>{
    private static final String INSERT = "INSERT INTO Ramo (idRamo, florPrincipal, cantidadFlores, colorEnvoltorio) VALUES  (?,?,?,?)";
    private static final String INSERT_FLORES = "INSERT INTO RamoFlores (Ramo_idRamo, Flor_idFlor) VALUES (?,?)";
    private static final String UPDATE = "UPDATE Ramo SET florPrincipal = ?, cantidadFlores = ?, colorEnvoltorio = ? WHERE idRamo = ?";
    private static final String DELETE = "DELETE FROM Ramo WHERE idRamo = ?";
    private static final String DELETE_FLORES = "DELETE FROM RamoFlores WHERE Ramo_idRamo = ?";
    private static final String FIND_ALL = "SELECT r.*, rf.*, f.* FROM Ramo r JOIN RamoFlores rf ON r.idRamo = rf.Ramo_idRamo JOIN Flor f ON rf.Flor_idFlor = f.idFlor";
    private static final String FIND_BY_PK = "SELECT r.*, rf.*, f.* FROM Ramo r JOIN RamoFlores rf ON r.idRamo = rf.Ramo_idRamo JOIN Flor f ON rf.Flor_idFlor = f.idFlor WHERE idRamo = ?";
    private static final String FIND_BY_NAMES = "SELECT r.*, rf.*, f.* FROM Ramo r JOIN RamoFlores rf ON r.idRamo = rf.Ramo_idRamo JOIN Flor f ON rf.Flor_idFlor = f.idFlor JOIN Producto p ON r.idRamo = p.idProducto WHERE LOWER(p.nombre) LIKE ?";
    private static final String FIND_BY_NAME = "SELECT r.*, rf.*, f.* FROM Ramo r JOIN RamoFlores rf ON r.idRamo = rf.Ramo_idRamo JOIN Flor f ON rf.Flor_idFlor = f.idFlor JOIN Producto p ON r.idRamo = p.idProducto WHERE LOWER(p.nombre) LIKE ? AND LOWER(p.description) LIKE ?";
    private static final String FIND_BY_RANGE = "SELECT r.*, rf.*, f.* FROM Ramo r JOIN RamoFlores rf ON r.idRamo = rf.Ramo_idRamo JOIN Flor f ON rf.Flor_idFlor = f.idFlor JOIN Producto p ON r.idRamo = p.idProducto WHERE p.precio >= ? AND p.precio <= ? AND LOWER(p.description) LIKE ?";
    private static final String FIND_BY_TYPE = "SELECT r.*, rf.*, f.* FROM Ramo r JOIN RamoFlores rf ON r.idRamo = rf.Ramo_idRamo JOIN Flor f ON rf.Flor_idFlor = f.idFlor JOIN Producto p ON r.idRamo = p.idProducto WHERE LOWER(p.description) LIKE ?";


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
        if(entity != null) {
            try (PreparedStatement ps = con.prepareStatement(DELETE)) {
                ps.setInt(1, entity.getIdRamo());
                ps.executeUpdate();
                productoDAO.build().delete(entity);
            } catch (SQLException e) {
                e.printStackTrace();
                entity = null;
            }
        }
        return entity;
    }

    public void insertRamo(Ramo entity) {
        productoDAO.build().insertProducto(entity);

        try (PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setInt(1, entity.getIdRamo());
            ps.setInt(2, entity.getFlorPr().getIdFlor());
            ps.setInt(3, entity.getCantidadFlores());
            ps.setString(4, entity.getColorEnvol());
            ps.executeUpdate();
            List<Flor> flores = entity.getFloresSecun();
            for(Flor f: flores) {
                try (PreparedStatement ps2 = con.prepareStatement(INSERT_FLORES)) {
                    ps2.setInt(1, entity.getIdRamo());
                    ps2.setInt(2, f.getIdFlor());
                    ps2.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRamo(Ramo entity) {

        productoDAO.build().updateProducto(entity);

        try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setInt(1, entity.getFlorPr().getIdFlor());
            ps.setInt(2, entity.getCantidadFlores());
            ps.setString(3, entity.getColorEnvol());
            ps.setInt(4, entity.getIdRamo());
            ps.executeUpdate();

            PreparedStatement ps3 = con.prepareStatement(DELETE_FLORES);
            ps3.setInt(1, entity.getIdRamo());
            ps3.executeUpdate();

            List<Flor> flores = entity.getFloresSecun();
            for(Flor f: flores) {
                try (PreparedStatement ps2 = con.prepareStatement(INSERT_FLORES)) {
                    ps2.setInt(1, entity.getIdRamo());
                    ps2.setInt(2, f.getIdFlor());
                    ps2.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ramo findByPK(Ramo pk) {
        Ramo result = null;
        List<Flor> flores = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_PK)) {
            ps.setInt(1, pk.getIdProducto());
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Ramo r = new Ramo();
                    // Atributos producto
                    Producto pro = productoDAO.build().findByPK(pk);
                    r.setIdProducto(pro.getIdProducto());
                    r.setNombre(pro.getNombre());
                    r.setPrecio(pro.getPrecio());
                    r.setStock(pro.getStock());
                    r.setTipo(pro.getTipo());
                    r.setDescripcion(pro.getDescripcion());
                    r.setImg(pro.getImg());
                    // Atributos Ramo
                    r.setIdRamo(rs.getInt("r.idRamo"));
                    r.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("r.florPrincipal"))));
                    r.setCantidadFlores(rs.getInt("r.cantidadFlores"));
                    r.setColorEnvol(rs.getString("r.colorEnvoltorio"));

                    // Atributos de las flores del array
                    Flor f = florDAO.build().findByPK(new Flor(rs.getInt("f.idFlor")));
                    flores.add(f);

                    r.setFloresSecun(flores);
                    result = r;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<Ramo> findAll() {
        List<Ramo> result = new ArrayList<>();
        Map<Integer, Ramo> ramosMap = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idRamo = rs.getInt("r.idRamo");
                    Ramo ramo = ramosMap.get(idRamo);

                    if (ramo == null) {
                        ramo = new Ramo();
                        // Atributos producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idRamo));
                        ramo.setIdProducto(pro.getIdProducto());
                        ramo.setNombre(pro.getNombre());
                        ramo.setPrecio(pro.getPrecio());
                        ramo.setStock(pro.getStock());
                        ramo.setTipo(pro.getTipo());
                        ramo.setDescripcion(pro.getDescripcion());
                        ramo.setImg(pro.getImg());

                        // Atributos del ramo
                        ramo.setIdRamo(idRamo);
                        ramo.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("r.florPrincipal"))));
                        ramo.setCantidadFlores(rs.getInt("r.cantidadFlores"));
                        ramo.setColorEnvol(rs.getString("r.colorEnvoltorio"));

                        ramo.setFloresSecun(new ArrayList<>());
                        ramosMap.put(idRamo, ramo);
                    }

                    // Agregar flor secundaria
                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("rf.Flor_idFlor")));
                    ramo.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(ramosMap.values());
        return result;
    }


    public List<Ramo> findByName(String name) {
        List<Ramo> result = new ArrayList<>();
        Map<Integer, Ramo> ramosMap = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_NAME)) {
            ps.setString(1, "%" + name.toLowerCase() + "%");
            ps.setString(1, "%prehecho%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idRamo = rs.getInt("r.idRamo");
                    Ramo ramo = ramosMap.get(idRamo);

                    if (ramo == null) {
                        ramo = new Ramo();
                        // Atributos del producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idRamo));
                        ramo.setIdProducto(pro.getIdProducto());
                        ramo.setNombre(pro.getNombre());
                        ramo.setPrecio(pro.getPrecio());
                        ramo.setStock(pro.getStock());
                        ramo.setTipo(pro.getTipo());
                        ramo.setDescripcion(pro.getDescripcion());
                        ramo.setImg(pro.getImg());

                        // Atributos del ramo
                        ramo.setIdRamo(idRamo);
                        ramo.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("r.florPrincipal"))));
                        ramo.setCantidadFlores(rs.getInt("r.cantidadFlores"));
                        ramo.setColorEnvol(rs.getString("r.colorEnvoltorio"));

                        ramo.setFloresSecun(new ArrayList<>());
                        ramosMap.put(idRamo, ramo);
                    }

                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("rf.Flor_idFlor")));
                    ramo.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(ramosMap.values());
        return result;
    }

    public List<Ramo> findByNames(String name) {
        List<Ramo> result = new ArrayList<>();
        Map<Integer, Ramo> ramosMap = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_NAMES)) {
            ps.setString(1, "%" + name.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idRamo = rs.getInt("r.idRamo");
                    Ramo ramo = ramosMap.get(idRamo);

                    if (ramo == null) {
                        ramo = new Ramo();
                        // Atributos del producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idRamo));
                        ramo.setIdProducto(pro.getIdProducto());
                        ramo.setNombre(pro.getNombre());
                        ramo.setPrecio(pro.getPrecio());
                        ramo.setStock(pro.getStock());
                        ramo.setTipo(pro.getTipo());
                        ramo.setDescripcion(pro.getDescripcion());
                        ramo.setImg(pro.getImg());

                        // Atributos del ramo
                        ramo.setIdRamo(idRamo);
                        ramo.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("r.florPrincipal"))));
                        ramo.setCantidadFlores(rs.getInt("r.cantidadFlores"));
                        ramo.setColorEnvol(rs.getString("r.colorEnvoltorio"));

                        ramo.setFloresSecun(new ArrayList<>());
                        ramosMap.put(idRamo, ramo);
                    }

                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("rf.Flor_idFlor")));
                    ramo.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(ramosMap.values());
        return result;
    }

    public List<Ramo> findByRange(int min, int max) {
        List<Ramo> result = new ArrayList<>();
        Map<Integer, Ramo> ramosMap = new HashMap<>(); // Map para evitar duplicados.
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_RANGE)) {
            ps.setInt(1, min);
            ps.setInt(2, max);
            ps.setString(3, "%prehecho%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idRamo = rs.getInt("r.idRamo");
                    Ramo ramo = ramosMap.get(idRamo); // Revisamos si ya existe el ramo.

                    if (ramo == null) { // Si no existe, lo creamos.
                        ramo = new Ramo();
                        // Atributos del producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idRamo));
                        ramo.setIdProducto(pro.getIdProducto());
                        ramo.setNombre(pro.getNombre());
                        ramo.setPrecio(pro.getPrecio());
                        ramo.setStock(pro.getStock());
                        ramo.setTipo(pro.getTipo());
                        ramo.setDescripcion(pro.getDescripcion());
                        ramo.setImg(pro.getImg());

                        // Atributos del ramo
                        ramo.setIdRamo(idRamo);
                        ramo.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("r.florPrincipal"))));
                        ramo.setCantidadFlores(rs.getInt("r.cantidadFlores"));
                        ramo.setColorEnvol(rs.getString("r.colorEnvoltorio"));

                        ramo.setFloresSecun(new ArrayList<>()); // Inicializar lista de flores secundarias.
                        ramosMap.put(idRamo, ramo); // Guardar el ramo en el mapa.
                    }

                    // Agregar flor secundaria
                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("rf.Flor_idFlor")));
                    ramo.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(ramosMap.values()); // Pasar los valores del mapa a la lista final.
        return result;
    }

    public List<Ramo> findByType(Boolean prehecho) {
        List<Ramo> result = new ArrayList<>();
        Map<Integer, Ramo> ramosMap = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_TYPE)) {
            if (prehecho) {
                ps.setString(1, "%prehecho%");
            } else {
                ps.setString(1, "%personalizado%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idRamo = rs.getInt("r.idRamo");
                    Ramo ramo = ramosMap.get(idRamo);

                    if (ramo == null) {
                        ramo = new Ramo();
                        // Atributos del producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idRamo));
                        ramo.setIdProducto(pro.getIdProducto());
                        ramo.setNombre(pro.getNombre());
                        ramo.setPrecio(pro.getPrecio());
                        ramo.setStock(pro.getStock());
                        ramo.setTipo(pro.getTipo());
                        ramo.setDescripcion(pro.getDescripcion());
                        ramo.setImg(pro.getImg());

                        // Atributos del ramo
                        ramo.setIdRamo(idRamo);
                        ramo.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("r.florPrincipal"))));
                        ramo.setCantidadFlores(rs.getInt("r.cantidadFlores"));
                        ramo.setColorEnvol(rs.getString("r.colorEnvoltorio"));

                        ramo.setFloresSecun(new ArrayList<>());
                        ramosMap.put(idRamo, ramo);
                    }

                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("rf.Flor_idFlor")));
                    ramo.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(ramosMap.values());
        return result;
    }

    @Override
    public void close() throws IOException {

    }

    public static ramoDAO build() {
        return new ramoDAO();
    }
}
