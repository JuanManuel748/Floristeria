package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.florDAO;
import com.github.JuanManuel.model.entity.Flor;
import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.DAO.productoDAO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class testProducto {
    public static void main(String[] args) {
        /*
        Scanner s = new Scanner(System.in);
        List<Producto> productos = new ArrayList<>();
        String filtro = s.nextLine();
        if (filtro.equalsIgnoreCase("Todos")) {
            productos = productoDAO.build().findByType("complemento");
            List<Producto> flores = productoDAO.build().findByType("flor");
            productos.addAll(flores);
        } else {
            productos = productoDAO.build().findByType(filtro.toLowerCase());
        }

        for(Producto p: productos) {
            System.out.println(p);
        }
        */

        int result = 2;
        try {
            List<Producto> p = productoDAO.build().findAll();
            Producto temp = p.get(p.size()-1);

            System.out.println(temp);
            result = temp.getIdProducto();
            result++;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);




    }
}
