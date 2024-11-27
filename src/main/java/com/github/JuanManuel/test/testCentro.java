package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.*;
import com.github.JuanManuel.model.entity.*;

import java.util.*;

public class testCentro {
    public static void main(String[] args) {
        try {
            List<Flor> flores = new ArrayList<>();
            for(int i = 3; i <6 ; i++) {
                flores.add(florDAO.build().findByPK(new Flor(i)));
            }
            Centro c = new Centro(8, "Centro prueba", 50.5, 10, "Centro de prueba", florDAO.build().findByPK(new Flor(2)), "Grande", "", flores);
            //centroDAO.build().save(c);
            Centro centro = centroDAO.build().findByPK(c);
            System.out.println(centro);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
// Unknown column 'Ramo_idRamo' in 'field list'
//	at com.github.JuanManuel/com.github.JuanManuel.test.testCentro.main(testCentro.java:20)