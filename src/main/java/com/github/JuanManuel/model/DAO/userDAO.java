package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.User;
import com.github.JuanManuel.view.Alerta;
import com.github.JuanManuel.view.WelcomeController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class userDAO implements DAO<User>{
    private static final String SQL_INSERT = "INSERT INTO usuario (telefono, nombre, contraseña, isAdmin) VALUES  (?,?,?, ?)";
    private static final String SQL_UPDATE = "UPDATE usuario SET nombre = ?, contraseña = ?, isAdmin = ? WHERE telefono = ?";
    private static final String SQL_DELETE = "DELETE FROM usuario WHERE telefono = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM usuario";
    private static final String SQL_FIND_BY_PHONE = "SELECT * FROM usuario WHERE telefono = ?";
    private static final String SQL_FIND_ADMINS = "SELECT * FROM usuario WHERE isAdmin = ?";
    private static final String SQL_FIND_BY_NAME = "SELECT * FROM usuario WHERE LOWER(nombre) LIKE ?";
    // ==============================================================================================================================================
    private static final String H2_INSERT = "INSERT INTO \"usuario\" (\"telefono\", \"nombre\", \"contraseña\", \"isAdmin\") VALUES  (?,?,?, ?)";
    private static final String H2_UPDATE = "UPDATE \"usuario\" SET \"nombre\" = ?, \"contraseña\" = ?, \"isAdmin\" = ? WHERE \"telefono\" = ?";
    private static final String H2_DELETE = "DELETE FROM \"usuario\" WHERE \"telefono\" = ?";
    private static final String H2_FIND_ALL = "SELECT * FROM \"usuario\"";
    private static final String H2_FIND_BY_PHONE = "SELECT * FROM \"usuario\" WHERE \"telefono\" = ?";
    private static final String H2_FIND_ADMINS = "SELECT * FROM \"usuario\" WHERE \"isAdmin\" = ?";
    private static final String H2_FIND_BY_NAME = "SELECT * FROM \"usuario\" WHERE LOWER(\"nombre\") LIKE ?";

    private boolean sql;
    private Connection con;

    /**
     * Constructor that initializes the database connection.
     */
    public userDAO() {
        con = WelcomeController.mainCon;
        sql = WelcomeController.isSQL;
    }

    /**
     * Saves a User entity to the database.
     * If the user already exists (based on the primary key), updates the existing user.
     * Otherwise, inserts a new user.
     *
     * @param entity The User entity to be saved.
     * @return The saved or updated User entity.
     */
    @Override
    public User save(User entity) {
        if (entity == null) {
            return null;
        }
        if (findByPK(entity) == null) {
            insertUser(entity);
        } else {
            updateUser(entity);
        }
        return entity;
    }

    /**
     * Finds a User by its primary key (phone number).
     *
     * @param pk The User entity with the phone number to search for.
     * @return The User entity if found, or null if not found.
     */
    @Override
    public User findByPK(User pk) {
        User result = null;
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_PHONE);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_PHONE);
            }

            ps.setString(1, pk.getPhone());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setName(rs.getString("nombre"));
                    u.setPhone(rs.getString("telefono"));
                    u.setPassword(rs.getString("contraseña"));
                    u.setAdmin(rs.getBoolean("isAdmin"));
                    result = u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Finds all users in the database.
     *
     * @return A list of all User entities.
     */
    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_ALL);
            } else {
                ps = con.prepareStatement(H2_FIND_ALL);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User temp = new User();
                    temp.setName(rs.getString("nombre"));
                    temp.setPhone(rs.getString("telefono"));
                    temp.setPassword(rs.getString("contraseña"));
                    temp.setAdmin(rs.getBoolean("isAdmin"));

                    list.add(temp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Deletes a User entity from the database.
     *
     * @param entity The User entity to be deleted.
     * @return The deleted User entity, or null if deletion failed.
     * @throws SQLException If an error occurs during the deletion process.
     */
    @Override
    public User delete(User entity) throws SQLException {
        if (entity != null) {
            try {
                PreparedStatement ps;
                if (sql) {
                    ps = con.prepareStatement(SQL_DELETE);
                } else {
                    ps = con.prepareStatement(H2_DELETE);
                }
                ps.setString(1, String.valueOf(entity.getPhone()));
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                entity = null;
            }
        }
        return entity;
    }

    /**
     * Inserts a new User entity into the database.
     *
     * @param entity The User entity to be inserted.
     */
    public void insertUser(User entity) {
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_INSERT);
            } else {
                ps = con.prepareStatement(H2_INSERT);
            }
            ps.setString(1, entity.getPhone());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getPassword());
            ps.setBoolean(4, entity.isAdmin());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing User entity in the database.
     *
     * @param entity The User entity to be updated.
     */
    public void updateUser(User entity) {
        // UPDATE Usuario SET nombre = ?, contraseña = ?, isAdmin = ? WHERE telefono = ?
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_UPDATE);
            } else {
                ps = con.prepareStatement(H2_UPDATE);
            }
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPassword());
            ps.setBoolean(3, entity.isAdmin());
            ps.setString(4, entity.getPhone());
            ps.executeUpdate();

        } catch (SQLException e) {
            Alerta.showAlert("ERROR", "UPDATE FALLIDO", "No se ha podido actualizar el usuario, revisa los atributos");
            e.printStackTrace();
        }
    }

    /**
     * Finds users who have a specific admin status.
     *
     * @param admin The admin status to search for.
     * @return A list of User entities with the specified admin status.
     */
    public List<User> findAdmins(boolean admin) {
        List<User> list = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_ADMINS);
            } else {
                ps = con.prepareStatement(H2_FIND_ADMINS);
            }
            ps.setBoolean(1, admin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User temp = new User();
                    temp.setName(rs.getString("nombre"));
                    temp.setPhone(rs.getString("telefono"));
                    temp.setPassword(rs.getString("contraseña"));
                    temp.setAdmin(rs.getBoolean("isAdmin"));

                    list.add(temp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Finds users whose names match the given search string.
     *
     * @param name The name (or partial name) to search for.
     * @return A list of User entities whose names match the specified search string.
     */
    public List<User> findByName(String name) {
        List<User> list = new ArrayList<>();
        try {
            PreparedStatement ps;
            if (sql) {
                ps = con.prepareStatement(SQL_FIND_BY_NAME);
            } else {
                ps = con.prepareStatement(H2_FIND_BY_NAME);
            }
            ps.setString(1, "%" + name.toLowerCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User temp = new User();
                    temp.setName(rs.getString("nombre"));
                    temp.setPhone(rs.getString("telefono"));
                    temp.setPassword(rs.getString("contraseña"));
                    temp.setAdmin(rs.getBoolean("isAdmin"));

                    list.add(temp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Closes the DAO resources. This method is not yet implemented.
     *
     * @throws IOException If there is an issue with closing resources.
     */
    @Override
    public void close() throws IOException {

    }

    /**
     * Builds an instance of userDAO.
     *
     * @return A new instance of userDAO.
     */
    public static userDAO build() {
        return new userDAO();
    }
}
