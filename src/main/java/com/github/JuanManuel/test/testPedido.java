package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.*;
import com.github.JuanManuel.model.entity.*;

import java.util.*;

public class testPedido {
    public static void main(String[] args) {

        // String fechaP, String fechaE, Double total, User usr, List<Detalles> detalles
        List<Detalles> detaller = new ArrayList<>();
        Pedido ped = new Pedido(1);
        detaller.add(new Detalles(ped, productoDAO.build().findByPK(new Producto(2)), 5));
        detaller.add(new Detalles(ped, productoDAO.build().findByPK(new Producto(3)), 5));
        detaller.add(new Detalles(ped, productoDAO.build().findByPK(new Producto(4)), 5));
        ped = new Pedido(1, "01/01/2024", "07/01/2024",  10.20, "PAGADO", new User("666666666"), detaller);
        pedidoDAO.build().save(ped);


    }

}
