package com.github.JuanManuel.test;

import com.github.JuanManuel.model.DAO.pedidoDAO;
import com.github.JuanManuel.model.entity.Pedido;

public class testPedido {
    public static void main(String[] args) {
        Pedido p = new Pedido(1);
        pedidoDAO.build().setPagado(p);
    }

}
