package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.entity.Flor;
import com.github.JuanManuel.model.DAO.florDAO;

import java.util.List;

public class testFlor {
    public static void main(String[] args) {
        Flor f1 = new Flor(14, "flor de prueba 112" , 20.5, 5, "flor de prueba 1", "verde", false);
        florDAO.build().save(f1);
    }
}
