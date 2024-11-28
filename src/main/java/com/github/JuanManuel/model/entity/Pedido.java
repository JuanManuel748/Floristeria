package com.github.JuanManuel.model.entity;

import com.github.JuanManuel.model.DAO.pedidoDAO;

import java.util.List;
import java.util.Objects;

public class Pedido {
    private int idPedido;
    private String fechaPedido;
    private String fechaEntrega;
    private Double total;
    private String estado;
    private User User;
    private List<Detalles> detalles;


    public Pedido() {}

    public Pedido(int id) {
        this.idPedido = id;
    }

    public Pedido(int id, String fechaP, String fechaE, Double total, String estado, User usr, List<Detalles> detalles) {
        this.idPedido = id;
        this.fechaEntrega = fechaE;
        this.fechaPedido = fechaP;
        this.total = total;
        this.estado = estado;
        this.User = usr;
        this.detalles = detalles;
    }

    // GETTERS
    public int getIdPedido() {
        return idPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public Double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public com.github.JuanManuel.model.entity.User getUser() {
        return User;
    }

    public List<Detalles> getDetalles() {
        return detalles;
    }


    // SETTERS
    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setUser(com.github.JuanManuel.model.entity.User user) {
        User = user;
    }

    public void setDetalles(List<Detalles> detalles) {
        this.detalles = detalles;
    }
    // EQUALS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return idPedido == pedido.idPedido;
    }

    // HASHCODE
    @Override
    public int hashCode() {
        return Objects.hashCode(idPedido);
    }

    // ToString
    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", fechaPedido='" + fechaPedido + '\'' +
                ", fechaEntrega='" + fechaEntrega + '\'' +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                ", User=" + User +
                ", productos=" + detalles +
                '}';
    }


}
