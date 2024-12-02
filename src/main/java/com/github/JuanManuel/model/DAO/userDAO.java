package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.User;
import com.github.JuanManuel.view.Alerta;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class userDAO implements DAO<User>{
    private static final String INSERT = "INSERT INTO Usuario (telefono, nombre, contraseña, isAdmin) VALUES  (?,?,?, ?)";
    private static final String UPDATE = "UPDATE Usuario SET nombre = ?, contraseña = ?, isAdmin = ? WHERE telefono = ?";
    private static final String DELETE = "DELETE FROM Usuario WHERE telefono = ?";
    private static final String FIND_ALL = "SELECT * FROM Usuario";
    private static final String FIND_BY_PHONE = "SELECT * FROM Usuario WHERE telefono = ?";
    private static final String FIND_ADMINS = "SELECT * FROM Usuario WHERE isAdmin = ?";
    private static final String FIND_BY_NAME = "SELECT * FROM Usuario WHERE LOWER(nombre) LIKE ?";

    private Connection con;

    public userDAO() {
        con = MySQLConnection.getConnection();
    }


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

    @Override
    public User findByPK(User pk) {
        User result = null;
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_PHONE)) {
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

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_ALL)) {
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

    @Override
    public User delete(User entity) throws SQLException {
        if (entity != null) {
            try (PreparedStatement pst = con.prepareStatement(DELETE)) {
                pst.setString(1, String.valueOf(entity.getPhone()));
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                entity = null;
            }
        }
        return entity;
    }

    public void insertUser(User entity) {
        try (PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setString(1, entity.getPhone());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getPassword());
            ps.setBoolean(4, entity.isAdmin());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User entity) {
        // UPDATE Usuario SET nombre = ?, contraseña = ?, isAdmin = ? WHERE telefono = ?
        try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
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

    public List<User> findAdmins(boolean admin) {
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_ADMINS)) {
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

    public List<User> findByName(String name) {
        List<User> list = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(FIND_BY_NAME)) {
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


    @Override
    public void close() throws IOException {

    }

    public static userDAO build() {
        return new userDAO();
    }
}
