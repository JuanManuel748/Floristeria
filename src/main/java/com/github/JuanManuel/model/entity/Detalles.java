package com.github.JuanManuel.model.entity;

import java.util.Objects;

public class Detalles {
    private Pedido ped;
    private Producto pro;
    private int cantidad;
    private Double subtotal;

    public Detalles () {}
    public Detalles (Pedido ped) {
        this.ped = ped;
    }

    public Detalles (Pedido ped, Producto pro, int cantidad) {
        this.ped = ped;
        this.pro = pro;
        this.cantidad = cantidad;
        this.subtotal = (Double) pro.getPrecio() * cantidad;
    }

    // GETTERS
    public Pedido getPed() {
        return ped;
    }

    public Producto getPro() {
        return pro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    // SETTERS
    public void setPed(Pedido ped) {
        this.ped = ped;
    }

    public void setPro(Producto pro) {
        this.pro = pro;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    // EQUALS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Detalles detalles = (Detalles) o;
        return Objects.equals(ped, detalles.ped) && Objects.equals(pro, detalles.pro);
    }

    // HASHCODE
    @Override
    public int hashCode() {
        return Objects.hash(ped, pro);
    }

    // ToString
    @Override
    public String toString() {
        return "\nDetalles{" +
                "ped=" + this.ped.getIdPedido() +
                ", pro=" + this.pro +
                ", cantidad=" + this.cantidad +
                ", subtotal=" + this.subtotal +
                '}';
    }
}
