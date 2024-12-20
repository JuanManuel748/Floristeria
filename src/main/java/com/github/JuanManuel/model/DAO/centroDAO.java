package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.*;
import com.github.JuanManuel.view.WelcomeController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class centroDAO implements DAO<Centro>{
    private static final String SQL_INSERT = "INSERT INTO centro (idCentro, florPrincipal, tamaño, frase) VALUES  (?,?,?,?)";
    private static final String SQL_INSERT_FLORES = "INSERT INTO centroflores (Centro_idCentro, Flor_idFlor) VALUES (?,?)";
    private static final String SQL_UPDATE = "UPDATE centro SET florPrincipal = ?, tamaño = ?, frase = ? WHERE idCentro = ?";
    private static final String SQL_DELETE = "DELETE FROM centro WHERE idCentro = ?";
    private static final String SQL_DELETE_FLORES = "DELETE FROM centroflores WHERE Centro_idCentro = ?";
    private static final String SQL_FIND_ALL = "SELECT c.*, cf.*, f.* FROM centro c JOIN centroflores cf ON c.idCentro = cf.Centro_idCentro JOIN Flor f ON cf.Flor_idFlor = f.idFlor";
    private static final String SQL_FIND_BY_PK = "SELECT c.*, cf.*, f.* FROM centro c JOIN centroflores cf ON c.idCentro = cf.Centro_idCentro JOIN Flor f ON cf.Flor_idFlor = f.idFlor WHERE c.idCentro = ?";
    private static final String SQL_FIND_BY_NAME = "SELECT c.*, cf.*, f.* FROM centro c JOIN centroflores cf ON c.idCentro = cf.Centro_idCentro JOIN Flor f ON cf.Flor_idFlor = f.idFlor JOIN Producto p ON c.idCentro = p.idProducto WHERE LOWER(p.nombre) LIKE ? AND LOWER(p.description) LIKE ?";
    private static final String SQL_FIND_BY_RANGE = "SELECT c.*, cf.*, f.* FROM centro c JOIN centroflores cf ON c.idCentro = cf.Centro_idCentro JOIN Flor f ON cf.Flor_idFlor = f.idFlor JOIN Producto p ON c.idCentro = p.idProducto WHERE p.precio >= ? AND p.precio <= ? AND LOWER(p.description) LIKE ?";
    private static final String SQL_FIND_BY_NAMES = "SELECT c.*, cf.*, f.* FROM centro c JOIN centroflores cf ON c.idCentro = cf.Centro_idCentro JOIN Flor f ON cf.Flor_idFlor = f.idFlor JOIN Producto p ON c.idCentro = p.idProducto WHERE LOWER(p.nombre) LIKE ?";
    private static final String SQL_FIND_BY_TYPE = "SELECT c.*, cf.*, f.* FROM centro c JOIN centroflores cf ON c.idCentro = cf.Centro_idCentro JOIN Flor f ON cf.Flor_idFlor = f.idFlor JOIN Producto p ON c.idCentro = p.idProducto WHERE LOWER(p.description) LIKE ?";
    // =======================================================================================================================================================================================================================================================================================================
    private static final String H2_INSERT = "INSERT INTO \"centro\" (\"idCentro\", \"florPrincipal\", \"tamaño\", \"frase\") VALUES  (?,?,?,?)";
    private static final String H2_INSERT_FLORES = "INSERT INTO \"centroflores\" (\"Centro_idCentro\", \"Flor_idFlor\") VALUES (?,?)";
    private static final String H2_UPDATE = "UPDATE \"centro\" SET \"florPrincipal\" = ?, \"tamaño\" = ?, \"frase\" = ? WHERE \"idCentro\" = ?";
    private static final String H2_DELETE = "DELETE FROM \"centro\" WHERE \"idCentro\" = ?";
    private static final String H2_DELETE_FLORES = "DELETE FROM \"centroflores\" WHERE \"Centro_idCentro\" = ?";
    private static final String H2_FIND_ALL = "SELECT c.*, cf.*, f.* FROM \"centro\" c JOIN \"centroflores\" cf ON c.\"idCentro\" = cf.\"Centro_idCentro\" JOIN \"flor\" f ON cf.\"Flor_idFlor\" = f.\"idFlor\"";
    private static final String H2_FIND_BY_PK = "SELECT c.*, cf.*, f.* FROM \"centro\" c JOIN \"centroflores\" cf ON c.\"idCentro\" = cf.\"Centro_idCentro\" JOIN \"flor\" f ON cf.\"Flor_idFlor\" = f.\"idFlor\" WHERE c.\"idCentro\" = ?";
    private static final String H2_FIND_BY_NAME = "SELECT c.*, cf.*, f.* FROM \"centro\" c JOIN \"centroflores\" cf ON c.\"idCentro\" = cf.\"Centro_idCentro\" JOIN \"flor\" f ON cf.\"Flor_idFlor\" = f.\"idFlor\" JOIN \"producto\" p ON c.\"idCentro\" = p.\"idProducto\" WHERE LOWER(p.\"nombre\") LIKE ? AND LOWER(p.\"description\") LIKE ?";
    private static final String H2_FIND_BY_RANGE = "SELECT c.*, cf.*, f.* FROM \"centro\" c JOIN \"centroflores\" cf ON c.\"idCentro\" = cf.\"Centro_idCentro\" JOIN \"flor\" f ON cf.\"Flor_idFlor\" = f.\"idFlor\" JOIN \"producto\" p ON c.\"idCentro\" = p.\"idProducto\" WHERE p.\"precio\" >= ? AND p.\"precio\" <= ? AND LOWER(p.\"description\") LIKE ?";
    private static final String H2_FIND_BY_NAMES = "SELECT c.*, cf.*, f.* FROM \"centro\" c JOIN \"centroflores\" cf ON c.\"idCentro\" = cf.\"Centro_idCentro\" JOIN \"flor\" f ON cf.\"Flor_idFlor\" = f.\"idFlor\" JOIN \"producto\" p ON c.\"idCentro\" = p.\"idProducto\" WHERE LOWER(p.\"nombre\") LIKE ?";
    private static final String H2_FIND_BY_TYPE = "SELECT c.*, cf.*, f.* FROM \"centro\" c JOIN \"centroflores\" cf ON c.\"idCentro\" = cf.\"Centro_idCentro\" JOIN \"flor\" f ON cf.\"Flor_idFlor\" = f.\"idFlor\" JOIN \"producto\" p ON c.\"idCentro\" = p.\"idProducto\" WHERE LOWER(p.\"description\") LIKE ?";


    private Connection con;
    private boolean sql;

    /**
     * Constructs a new `centroDAO` instance and initializes the database connection.
     */
    public centroDAO() {
        con = WelcomeController.mainCon;
        sql = WelcomeController.isSQL;
    }

    /**
     * Saves a `Centro` entity to the database.
     * Inserts if not found, updates otherwise.
     *
     * @param entity The `Centro` entity to save.
     * @return The saved `Centro` entity.
     */
    @Override
    public Centro save(Centro entity) {
        if (entity == null) {
            return null;
        }
        if (findByPK(entity) == null) {
            insertCentro(entity);
        } else {
            updateCentro(entity);
        }
        return entity;
    }

    /**
     * Inserts a new `Centro` record along with associated secondary flowers into the database.
     *
     * @param entity The `Centro` object to be inserted.
     */
    public void insertCentro(Centro entity) {
        productoDAO.build().insertProducto(entity);
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_INSERT);
            } else {
                ps = con.prepareStatement(H2_INSERT);
            }
            ps.setInt(1, entity.getIdCentro());
            ps.setInt(2, entity.getFlorPr().getIdFlor());
            ps.setString(3, entity.getSize());
            ps.setString(4, entity.getFrase());
            ps.executeUpdate();
            List<Flor> flores = entity.getFloresSecun();
            for (Flor f : flores) {
                try {
                    PreparedStatement ps2;
                    if (sql) {
                        ps2 = con.prepareStatement(SQL_INSERT_FLORES);
                    } else {
                        ps2 = con.prepareStatement(H2_INSERT_FLORES);
                    }
                    ps2.setInt(1, entity.getIdCentro());
                    ps2.setInt(2, f.getIdFlor());
                    ps2.executeUpdate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing `Centro` record and its associated secondary flowers.
     *
     * @param entity The `Centro` object to be updated.
     */
    public void updateCentro(Centro entity) {
        productoDAO.build().updateProducto(entity);
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_UPDATE);
            } else {
                ps = con.prepareStatement(H2_UPDATE);
            }
            ps.setInt(1, entity.getFlorPr().getIdFlor());
            ps.setString(2, entity.getSize());
            ps.setString(3, entity.getFrase());
            ps.setInt(4, entity.getIdCentro());
            ps.executeUpdate();


                PreparedStatement ps3;
                if (sql) {
                    ps3 = con.prepareStatement(SQL_DELETE_FLORES);
                } else {
                    ps3 = con.prepareStatement(H2_DELETE_FLORES);
                }
            ps3.setInt(1, entity.getIdCentro());
            ps3.executeUpdate();

            List<Flor> flores = entity.getFloresSecun();
            for (Flor f : flores) {
                try {
                    PreparedStatement ps2;
                    if (sql) {
                        ps2 = con.prepareStatement(SQL_INSERT_FLORES);
                    } else {
                        ps2 = con.prepareStatement(H2_INSERT_FLORES);
                    }
                    ps2.setInt(1, entity.getIdCentro());
                    ps2.setInt(2, f.getIdFlor());
                    ps2.executeUpdate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a `Centro` entity from the database.
     *
     * @param entity The `Centro` entity to delete.
     * @return The deleted `Centro` entity, or null if an error occurs.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public Centro delete(Centro entity) throws SQLException {
        if (entity != null) {
            try {
                PreparedStatement ps;
                if (sql) {
                    ps = con.prepareStatement(SQL_DELETE);
                } else {
                    ps = con.prepareStatement(H2_DELETE);
                }
                ps.setInt(1, entity.getIdCentro());
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
     * Finds a `Centro` entity by its primary key.
     *
     * @param pk The primary key to search for.
     * @return The found `Centro` entity, or null if not found.
     */
    @Override
    public Centro findByPK(Centro pk) {
        Centro result = null;
        List<Flor> flores = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_PK);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_PK);
            }
            ps.setInt(1, pk.getIdCentro());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Centro c = new Centro();
                    // Atributos producto
                    Producto pro = productoDAO.build().findByPK(pk);
                    c.setIdProducto(pro.getIdProducto());
                    c.setNombre(pro.getNombre());
                    c.setPrecio(pro.getPrecio());
                    c.setStock(pro.getStock());
                    c.setTipo(pro.getTipo());
                    c.setDescripcion(pro.getDescripcion());
                    c.setImg(pro.getImg());
                    // Atributos centro
                    c.setIdCentro(rs.getInt("idCentro"));
                    c.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("florPrincipal"))));
                    c.setSize(rs.getString("tamaño"));
                    c.setFrase(rs.getString("frase"));

                    // Atributos de las flores del array
                    Flor f = florDAO.build().findByPK(new Flor(rs.getInt("idFlor")));
                    flores.add(f);

                    c.setFloresSecun(flores);
                    result = c;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Finds all `Centro` entities in the database.
     *
     * @return A list of all `Centro` entities.
     */
    @Override
    public List<Centro> findAll() {
        List<Centro> result = new ArrayList<>();
        Map<Integer, Centro>centrosMap =new HashMap<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_ALL);
            } else {
                ps = con.prepareStatement(H2_FIND_ALL);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idCentro = rs.getInt("idCentro");
                    Centro c = centrosMap.get(idCentro);

                    if (c == null) {
                        c = new Centro();
                        // Atributos producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idCentro));
                        c.setIdProducto(pro.getIdProducto());
                        c.setNombre(pro.getNombre());
                        c.setPrecio(pro.getPrecio());
                        c.setStock(pro.getStock());
                        c.setTipo(pro.getTipo());
                        c.setDescripcion(pro.getDescripcion());
                        c.setImg(pro.getImg());
                        // Atributos del Centro
                        c.setIdCentro(idCentro);
                        c.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("florPrincipal"))));
                        c.setSize(rs.getString("tamaño"));
                        c.setFrase(rs.getString("frase"));

                        c.setFloresSecun(new ArrayList<>());
                        centrosMap.put(idCentro, c);
                    }
                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("Flor_idFlor")));
                    c.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(centrosMap.values());
        return result;
    }

    /**
     * Finds and retrieves a list of Centro objects whose name or description
     * contains the given string (case-insensitive) and are marked as "prehecho".
     *
     * @param name the partial name to search for
     * @return a list of Centro objects matching the criteria
     */
    public List<Centro> findByName(String name) {
        List<Centro> result = new ArrayList<>();
        Map<Integer, Centro>centrosMap =new HashMap<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_NAME);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_NAME);
            }
            ps.setString(1, "%" + name.toLowerCase() + "%");
            ps.setString(2, "%prehecho%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idCentro = rs.getInt("idCentro");
                    Centro c = centrosMap.get(idCentro);

                    if (c == null) {
                        c = new Centro();
                        // Atributos producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idCentro));
                        c.setIdProducto(pro.getIdProducto());
                        c.setNombre(pro.getNombre());
                        c.setPrecio(pro.getPrecio());
                        c.setStock(pro.getStock());
                        c.setTipo(pro.getTipo());
                        c.setDescripcion(pro.getDescripcion());
                        c.setImg(pro.getImg());
                        // Atributos del centro
                        c.setIdCentro(idCentro);
                        c.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("florPrincipal"))));
                        c.setSize(rs.getString("tamaño"));
                        c.setFrase(rs.getString("frase"));

                        c.setFloresSecun(new ArrayList<>());
                        centrosMap.put(idCentro, c);
                    }
                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("Flor_idFlor")));
                    c.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(centrosMap.values());
        return result;
    }

    /**
     * Finds and retrieves a list of Centro objects based on a partial name match.
     *
     * @param name the partial name to search for
     * @return a list of Centro objects matching the criteria
     */
    public List<Centro> findByNames(String name) {
        List<Centro> result = new ArrayList<>();
        Map<Integer, Centro>centrosMap =new HashMap<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_NAMES);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_NAMES);
            }
            ps.setString(1, "%" + name.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idCentro = rs.getInt("idCentro");
                    Centro c = centrosMap.get(idCentro);

                    if (c == null) {
                        c = new Centro();
                        // Atributos producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idCentro));
                        c.setIdProducto(pro.getIdProducto());
                        c.setNombre(pro.getNombre());
                        c.setPrecio(pro.getPrecio());
                        c.setStock(pro.getStock());
                        c.setTipo(pro.getTipo());
                        c.setDescripcion(pro.getDescripcion());
                        c.setImg(pro.getImg());
                        // Atributos del centro
                        c.setIdCentro(idCentro);
                        c.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("florPrincipal"))));
                        c.setSize(rs.getString("tamaño"));
                        c.setFrase(rs.getString("frase"));

                        c.setFloresSecun(new ArrayList<>());
                        centrosMap.put(idCentro, c);
                    }
                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("Flor_idFlor")));
                    c.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(centrosMap.values());
        return result;
    }

    /**
     * Retrieves a list of Centro objects that fall within the specified price range
     * and are marked as "prehecho".
     *
     * @param min the minimum price
     * @param max the maximum price
     * @return a list of Centro objects within the price range
     */
    public List<Centro> findByRange(int min, int max) {
        List<Centro> result = new ArrayList<>();
        Map<Integer, Centro>centrosMap =new HashMap<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_RANGE);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_RANGE);
            }
            ps.setInt(1, min);
            ps.setInt(2, max);
            ps.setString(3, "%prehecho%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idCentro = rs.getInt("idCentro");
                    Centro c = centrosMap.get(idCentro);

                    if (c == null) {
                        c = new Centro();
                        // Atributos producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idCentro));
                        c.setIdProducto(pro.getIdProducto());
                        c.setNombre(pro.getNombre());
                        c.setPrecio(pro.getPrecio());
                        c.setStock(pro.getStock());
                        c.setTipo(pro.getTipo());
                        c.setDescripcion(pro.getDescripcion());
                        c.setImg(pro.getImg());
                        // Atributos del centro
                        c.setIdCentro(idCentro);
                        c.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("florPrincipal"))));
                        c.setSize(rs.getString("tamaño"));
                        c.setFrase(rs.getString("frase"));

                        c.setFloresSecun(new ArrayList<>());
                        centrosMap.put(idCentro, c);
                    }
                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("Flor_idFlor")));
                    c.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(centrosMap.values());
        return result;
    }

    /**
     * Retrieves a list of Centro objects based on their type (prehecho or personalizado).
     *
     * @param prehecho true to filter "prehecho" type, false for "personalizado"
     * @return a list of Centro objects matching the specified type
     */
    public List<Centro> findByType(Boolean prehecho) {
        List<Centro> result = new ArrayList<>();
        Map<Integer, Centro>centrosMap =new HashMap<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_TYPE);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_TYPE);
            }
            if (prehecho) {
                ps.setString(1, "%prehecho%");
            } else {
                ps.setString(1, "%personalizado%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idCentro = rs.getInt("idCentro");
                    Centro c = centrosMap.get(idCentro);

                    if (c == null) {
                        c = new Centro();
                        // Atributos producto
                        Producto pro = productoDAO.build().findByPK(new Producto(idCentro));
                        c.setIdProducto(pro.getIdProducto());
                        c.setNombre(pro.getNombre());
                        c.setPrecio(pro.getPrecio());
                        c.setStock(pro.getStock());
                        c.setTipo(pro.getTipo());
                        c.setDescripcion(pro.getDescripcion());
                        c.setImg(pro.getImg());
                        // Atributos del Centro
                        c.setIdCentro(idCentro);
                        c.setFlorPr(florDAO.build().findByPK(new Flor(rs.getInt("florPrincipal"))));
                        c.setSize(rs.getString("tamaño"));
                        c.setFrase(rs.getString("frase"));

                        c.setFloresSecun(new ArrayList<>());
                        centrosMap.put(idCentro, c);
                    }
                    Flor florSecundaria = florDAO.build().findByPK(new Flor(rs.getInt("Flor_idFlor")));
                    c.getFloresSecun().add(florSecundaria);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        result.addAll(centrosMap.values());
        return result;
    }

    /**
     * Closes resources if necessary. Currently, no specific resources are managed.
     */
    @Override
    public void close() throws IOException {

    }

    /**
     * Static factory method to create a new instance of centroDAO.
     *
     * @return a new instance of centroDAO
     */
    public static centroDAO build() {
        return new centroDAO();
    }
}
