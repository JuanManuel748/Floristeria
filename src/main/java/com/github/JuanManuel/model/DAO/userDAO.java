package com.github.JuanManuel.model.DAO;

import com.github.JuanManuel.model.connection.MySQLConnection;
import com.github.JuanManuel.model.entity.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class userDAO implements DAO<User>{
    private static final String INSERT = "INSERT INTO Usuario (telefono, nombre, contraseña) VALUES  (?,?,?)";
    private static final String UPDATE = "UPDATE Usuario SET nombre = ?, contraseña = ? WHERE telefono = ?";
    private static final String DELETE = "DELETE FROM Usuario WHERE telefono = ?";
    private static final String FIND_ALL = "SELECT * FROM Usuario";
    private static final String FIND_BY_PHONE = "SELECT * FROM Usuario WHERE telefono = ?";

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
                    result = u;
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR EN FINDBYPK EN userDAO");
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

                    list.add(temp);
                }
            }
        } catch (SQLException e) {
            System.out.println("ERROR EN FINDALL EN userDAO");
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
                System.out.println("ERROR EN DELETE EN userDAO");
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
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ERROR EN INSERT EN userDAO");
            e.printStackTrace();
        }
    }

    public void updateUser(User entity) {
        try (PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getPhone());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("ERROR EN UPDATE EN userDAO");
            e.printStackTrace();
        }
    }


    @Override
    public void close() throws IOException {

    }

    public static userDAO build() {
        return new userDAO();
    }
}
