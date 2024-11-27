package com.github.JuanManuel.model.entity;

import java.io.File;
import java.util.Objects;

public class Flor extends Producto{
    private int idFlor;
    private String color;
    private boolean tipoFlor;

    public Flor () {super();}
    public Flor(int id) {
        super(id);
        this.idFlor = id;
    }

    public Flor(int id, String nom, Double precio, int stock, String description, File img, String color, boolean tipoF) {
        super(id, nom, precio, stock, "flor", description, img);
        this.idFlor = id;
        this.color = color;
        this.tipoFlor = tipoF;
    }

    public Flor(int id, String nom, Double precio, int stock, String description, String color, boolean tipoF) {
        super(id, nom, precio, stock, "flor", description);
        this.idFlor = id;
        this.color = color;
        this.tipoFlor = tipoF;
    }

    // GETTERS
    public int getIdFlor() {
        return idFlor;
    }

    public String getColor() {
        return color;
    }

    public boolean getTipoFlor() {
        return tipoFlor;
    }

    // SETTERS
    public void setIdFlor(int idFlor) {
        this.idFlor = idFlor;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setTipoFlor(boolean tipoFlor) {
        this.tipoFlor = tipoFlor;
    }

    // EQUALS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Flor flor = (Flor) o;
        return idFlor == flor.idFlor;
    }

    //HASHCODE
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idFlor);
    }

    // ToString
    @Override
    public String toString() {
        return super.toString()  + "\n" +
                "Flor{" +
                "idFlor=" + idFlor +
                ", color='" + color + '\'' +
                ", tipoFlor=" + tipoFlor +
                '}';
    }
}
