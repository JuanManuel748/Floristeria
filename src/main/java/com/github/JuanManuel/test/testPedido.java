package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.pedidoDAO;
import com.github.JuanManuel.model.DAO.userDAO;
import com.github.JuanManuel.model.entity.Pedido;
import com.github.JuanManuel.model.entity.User;

public class testPedido {
    public static void main(String[] args) {
        Pedido ped = new Pedido("hoy", "mañana", 100.50, userDAO.build().findByPK(new User("666666666")));
        //pedidoDAO.build().save(ped);
        Pedido p = new Pedido(1);
        pedidoDAO.build().setPagado(p);
    }

}
