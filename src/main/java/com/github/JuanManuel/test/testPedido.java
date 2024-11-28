package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.*;
import com.github.JuanManuel.model.entity.*;

import java.util.*;

public class testPedido {
    public static void main(String[] args) {

        // String fechaP, String fechaE, Double total, User usr, List<Detalles> detalles
        Pedido ped = new Pedido("hoy", "mañana", 10.20, new User("1"), null);
        List<Detalles> detaller = new ArrayList<>();
        detaller.add(new Detalles(ped, productoDAO.build().findByPK(new Producto(2)), 5));
        detaller.add(new Detalles(ped, productoDAO.build().findByPK(new Producto(3)), 5));
        detaller.add(new Detalles(ped, productoDAO.build().findByPK(new Producto(4)), 5));
        ped.setDetalles(detaller);
        pedidoDAO.build().save(ped);
    }

}
