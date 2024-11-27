package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.entity.Flor;
import com.github.JuanManuel.model.DAO.florDAO;

import java.util.List;

public class testFlor {
    public static void main(String[] args) {
        Flor f1 = new Flor(2, "flor de prueba 1" , 20.5, 5, "flor de prueba 1", "verde", false);
        Flor f2 = new Flor(3, "flor de prueba 2" , 20.5, 5, "flor de prueba 2", "amarillo", false);
        Flor f3 = new Flor(4, "flor de prueba 3" , 20.5, 5, "flor de prueba 3", "azul", true);
        Flor f4 = new Flor(5, "flor de prueba 4" , 20.5, 5, "flor de prueba 4", "rojo", false);
        florDAO.build().save(f1);
        florDAO.build().save(f2);
        florDAO.build().save(f3);
        florDAO.build().save(f4);
    }
}
