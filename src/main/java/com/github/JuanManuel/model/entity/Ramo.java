package com.github.JuanManuel.model.entity;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class Ramo extends Producto{
    private int idRamo;
    private Flor florPr;
    private int cantidadFlores;
    private String colorEnvol;
    private Flor [] floresSecun;

    public Ramo() {}
    public Ramo(int id) {
        super(id);
        this.idRamo = id;
    }

    public Ramo(int id, String nombre, Double precio, int stock, String descrip, File img, Flor florPr, int cantidadFlores, String color, Flor[] ar) {
        super(id, nombre, precio, stock, "ramo", descrip, img);
        this.idRamo = id;
        this.florPr = florPr;
        this.cantidadFlores = cantidadFlores;
        this.colorEnvol = color;
        this.floresSecun = ar;
    }

    public Ramo(int id, String nombre, Double precio, int stock, String descrip, Flor florPr, int cantidadFlores, String color, Flor[] ar) {
        super(id, nombre, precio, stock, "ramo", descrip);
        this.idRamo = id;
        this.florPr = florPr;
        this.cantidadFlores = cantidadFlores;
        this.colorEnvol = color;
        this.floresSecun = ar;
    }

    // GETTERS
    public int getIdRamo() {
        return idRamo;
    }

    public Flor getFlorPr() {
        return florPr;
    }

    public int getCantidadFlores() {
        return cantidadFlores;
    }

    public String getColorEnvol() {
        return colorEnvol;
    }

    public Flor[] getFloresSecun() {
        return floresSecun;
    }

    // SETTERS
    public void setIdRamo(int idRamo) {
        this.idRamo = idRamo;
    }

    public void setFlorPr(Flor florPr) {
        this.florPr = florPr;
    }

    public void setCantidadFlores(int cantidadFlores) {
        this.cantidadFlores = cantidadFlores;
    }

    public void setColorEnvol(String colorEnvol) {
        this.colorEnvol = colorEnvol;
    }

    public void setFloresSecun(Flor[] floresSecun) {
        this.floresSecun = floresSecun;
    }

    // EQUALS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Ramo ramo = (Ramo) o;
        return idRamo == ramo.idRamo;
    }

    // HASHCODE
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idRamo);
    }

    // ToString
    @Override
    public String toString() {
        return super.toString() + "Ramo{" +
                "idRamo=" + idRamo +
                ", florPr=" + florPr +
                ", cantidadFlores=" + cantidadFlores +
                ", colorEnvol='" + colorEnvol + '\'' +
                ", floresSecun=" + Arrays.toString(floresSecun) +
                '}';
    }
}
