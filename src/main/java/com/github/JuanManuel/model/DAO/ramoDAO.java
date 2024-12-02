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

    /**
     * Constructor that initializes the connection to the database.
     */
    public ramoDAO() {
        con = MySQLConnection.getConnection();
    }

    /**
     * Saves a Ramo entity to the database.
     * If the Ramo entity doesn't exist, it inserts it, otherwise updates the existing one.
     *
     * @param entity The Ramo entity to be saved.
     * @return The saved Ramo entity.
     */
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

    /**
     * Deletes a Ramo entity from the database.
     * Also deletes associated RamoFlores records and Producto entity.
     *
     * @param entity The Ramo entity to be deleted.
     * @return The deleted Ramo entity or null if not found.
     * @throws SQLException If an SQL error occurs.
     */
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

    /**
     * Inserts a new Ramo entity into the database.
     * Inserts RamoFlores associations and Producto entity.
     *
     * @param entity The Ramo entity to be inserted.
     */
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

    /**
     * Updates an existing Ramo entity in the database.
     * Updates associated RamoFlores and Producto entities.
     *
     * @param entity The Ramo entity to be updated.
     */
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

    /**
     * Retrieves a Ramo entity by its primary key.
     *
     * @param pk The Ramo entity's primary key.
     * @return The Ramo entity corresponding to the primary key, or null if not found.
     */
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

    /**
     * Retrieves all Ramo entities from the database.
     *
     * @return A list of all Ramo entities.
     */
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

    /**
     * Finds Ramo entities based on a partial name match.
     *
     * This method performs a case-insensitive search for Ramo entities by matching the product's name.
     * It also ensures that only products with descriptions containing "prehecho" (pre-made) are included.
     *
     * @param name The partial name to search for.
     * @return A list of Ramo entities matching the specified name.
     */
    public List<Ramo> findByName(String name) {
        List<Ramo> result = new ArrayList<>();
        Map<Integer, Ramo> ramosMap = new HashMap<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_NAME)) {
            ps.setString(1, "%" + name.toLowerCase() + "%");
            ps.setString(2, "%prehecho%");
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

    /**
     * Finds Ramo entities based on a partial name match, similar to findByName but with a different SQL query.
     *
     * This method also performs a case-insensitive search for Ramo entities based on the product's name. The query logic may differ slightly
     * depending on the implementation of the SQL query identified by `FIND_BY_NAMES`.
     *
     * @param name The partial name to search for.
     * @return A list of Ramo entities matching the specified name.
     */
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

    /**
     * Finds Ramo entities based on the price range of associated products.
     *
     * This method filters Ramo entities by the price range specified by the `min` and `max` parameters.
     * It also filters by description, only including products that contain "prehecho" (pre-made).
     *
     * @param min The minimum price of the products.
     * @param max The maximum price of the products.
     * @return A list of Ramo entities that fall within the specified price range.
     */
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

    /**
     * Finds Ramo entities based on whether they are pre-made or custom-made.
     *
     * This method filters Ramo entities by their type, which can either be pre-made or custom-made, depending on the boolean value of the `prehecho` parameter.
     *
     * @param prehecho If true, retrieves "prehecho" (pre-made) Ramo entities; if false, retrieves "personalizado" (custom-made) Ramo entities.
     * @return A list of Ramo entities of the specified type.
     */
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

    /**
     * Closes the DAO resources.
     *
     * This method is part of the AutoCloseable interface implementation but is not yet implemented for this DAO.
     */
    @Override
    public void close() throws IOException {

    }

    /**
     * Builds an instance of ramoDAO.
     *
     * @return A new instance of ramoDAO.
     */
    public static ramoDAO build() {
        return new ramoDAO();
    }
}
