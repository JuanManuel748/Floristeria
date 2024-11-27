package com.github.JuanManuel.test;

import com.github.JuanManuel.model.entity.Producto;
import com.github.JuanManuel.model.DAO.productoDAO;


public class testProducto {
    public static void main(String[] args) {
        // FUNCIONA
        Producto p = new Producto(1, "Abono 20kg", 2.5, 20, "complemento", "Es un saco de abono orgánico");
        try {
            productoDAO.build().insertProducto(p);
        } catch (Exception e) {
            System.out.println("No se pudo insertar");
            throw new RuntimeException(e);
        }

        System.out.println("Producto insertado correctamente");


    }
}
