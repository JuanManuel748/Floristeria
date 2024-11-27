package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.productoDAO;
import com.github.JuanManuel.model.entity.Flor;
import com.github.JuanManuel.model.DAO.florDAO;

import java.util.List;

public class testFlor {
    public static void main(String[] args) {
        /*
        Flor f = new Flor(2, "flor de prueba", 2.5, 10, "Es una flor de prueba para ver si funciona el DAO", "verde", false);
        try {
            florDAO.build().insertFlor(f);
        } catch (Exception e) {
            System.out.println("No se ha podido añadir la flor");
        }
        System.out.println("Flor añadida con exito");

        List<Flor> florList = florDAO.build().findAll();
        for(Flor fl:florList) {
            System.out.println(fl);
        }*/
        Flor f = (Flor) productoDAO.build().findByPK(new Flor(2));
        System.out.println(f);
    }
}
