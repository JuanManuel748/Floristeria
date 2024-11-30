package com.github.JuanManuel.model.entity;

import com.github.JuanManuel.model.DAO.pedidoDAO;
import com.github.JuanManuel.model.DAO.userDAO;
import com.github.JuanManuel.view.Alerta;
import com.github.JuanManuel.view.ProductosController;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private static Session _instance;
    private static User currentUser;
    private static Pedido ped;
    private static List<Detalles> detalles;

    private Session() {}

    public static Session getInstance() {
        if(_instance == null) {
            _instance = new Session();
            _instance.logIn(currentUser);
        }
        return _instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logIn(User usr) {
        currentUser = usr;
        createPed();

    }

    public void logOut(User usr) {
        currentUser = null;
    }

    public void createPed() {
        ped = new Pedido(searchID());
        ped.setUser(currentUser);
        detalles = new ArrayList<>();
    }
    private int searchID() {
        int result = 1;
        List<Pedido> pedidoLS = pedidoDAO.build().findAll();
        for (Pedido p:pedidoLS) {
            if (p.getIdPedido() == result) {
                result++;
            } else {
                break;
            }
        }
        return result;
    }

    public void addDetalle(Producto pro, int cant) {
        Boolean found = false;
        for(Detalles det : detalles) {
            if (det.getPro().equals(pro)) {
                det.setCantidad(det.getCantidad()+cant);
                found = true;
                break;
            }
        }
        if (!found) {
            detalles.add(new Detalles(ped, pro, cant));
        }
    }

    public void changeQuantity(Producto pro, int cant) {
        for(Detalles det : detalles) {
            if (det.getPro().equals(pro)) {
                det.setCantidad(cant);
                break;
            }
        }
    }

    public void delDetalle(Producto pro) {
        for(Detalles det : detalles) {
            if (det.getPro().equals(pro)) {
                detalles.remove(det);
                break;
            }
        }
    }

    public void madePed(String fechaP, String fechaE, Double total) {
        ped.setFechaPedido(fechaP);
        ped.setFechaEntrega(fechaE);
        ped.setTotal(total);
        ped.setEstado("PENDIENTE");
        ped.setDetalles(detalles);
        pedidoDAO.build().save(ped);
        createPed();
    }

    public List<Detalles> getDetalles() {
        return detalles;
    }

    public Pedido getPedido() {
        return ped;
    }
}
