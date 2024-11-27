package com.github.JuanManuel.model.DAO;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

public interface DAO<O> extends Closeable {
    O save(O entity);

    O delete(O entity) throws SQLException;

    O findByPK(O pk);


    List<O> findAll();




}