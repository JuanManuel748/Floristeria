package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.*;
import com.github.JuanManuel.model.entity.*;

import java.util.*;

public class testCentro {
    public static void main(String[] args) {
        try {
            /*
            List<Flor> flores = new ArrayList<>();
            for(int i = 3; i <6 ; i++) {
                flores.add(florDAO.build().findByPK(new Flor(i)));
            }
            Centro c = new Centro(12, "Centro personalizado", 50.5, 10, "Centro de prueba personalizado", florDAO.build().findByPK(new Flor(2)), "Grande", "", flores);
            centroDAO.build().save(c);
            Centro centro = centroDAO.build().findByPK(c);
            System.out.println(centro);
            */
            List<Centro> cn = new ArrayList<>();
            cn = centroDAO.build().findAll();
            //cn = centroDAO.build().findByName("");
            cn = centroDAO.build().findByRange(0, 100);
            for(Centro c: cn) {
                System.out.println(c);
            }

            System.out.println("\n\n"+cn.size());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}