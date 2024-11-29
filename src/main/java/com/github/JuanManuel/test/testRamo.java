package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.*;
import com.github.JuanManuel.model.entity.*;

import java.util.*;

public class testRamo {
    public static void main(String[] args) {
        try {
            /*
            List<Flor> flores = new ArrayList<>();
            for(int i = 3; i <6 ; i++) {
                flores.add(florDAO.build().findByPK(new Flor(i)));
            }
            Ramo r = new Ramo(11, "Ramo personalizado", 20.99, 1, "Ramo personalizado", florDAO.build().findByPK(new Flor(2)), 5, "verde", flores);
            ramoDAO.build().save(r);
            Ramo ramo = ramoDAO.build().findByPK(r);
            System.out.println(ramo);
             */
            Ramo ramo = ramoDAO.build().findByPK(new Ramo(6));
            System.out.println(ramo);

            List<Ramo> ramos = new ArrayList<>();
            //ramos = ramoDAO.build().findAll();
            //ramos = ramoDAO.build().findByName("ramo");
            ramos = ramoDAO.build().findByRange(0, 100);
            for(Ramo r: ramos) {
                System.out.println(r);
            }
            System.out.println("\n\n" + ramos.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
