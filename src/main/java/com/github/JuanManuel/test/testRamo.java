package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.*;
import com.github.JuanManuel.model.entity.*;

import java.util.*;

public class testRamo {
    public static void main(String[] args) {
        try {
            /*
            Flor f1 = new Flor(5, "flor de prueba 3", 10.2, 10, "flor de prueba 3", "amarillo", false);
            Flor f2 = new Flor(6, "flor de prueba 4", 10.2, 10, "flor de prueba 3", "azul", false);
            //florDAO.build().save(f1);
            //florDAO.build().save(f2);
            List<Flor> flores = new ArrayList<>();
            flores.add(florDAO.build().findByPK(f1));
            flores.add(florDAO.build().findByPK(f2));
            Ramo r = new Ramo(7, "Ramo de prueba 2", 50.5, 1, "Ramo de prueba 2", florDAO.build().findByPK(new Flor(2)), 5, "verde", flores);
            //ramoDAO.build().save(r);
            Ramo rm = ramoDAO.build().findByPK(r);
            if (rm != null) {
                System.out.println(rm);

            }*/

            List<Ramo> ramos = ramoDAO.build().findAll();
            for(Ramo r: ramos) {
                System.out.println(r + "\n\n");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
