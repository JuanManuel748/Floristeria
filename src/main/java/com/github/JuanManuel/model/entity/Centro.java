package com.github.JuanManuel.model.entity;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Centro extends Producto{
    private int idCentro;
    private Flor FlorPr;
    private String size;
    private String frase;
    private List<Flor> floresSecun;

    public Centro() {}
    public Centro(int id) {
        super(id);
        this.idCentro = id;
    }

    public Centro (int id, String nombre, Double precio, int stock, String descrip, File img, Flor pr, String size, String frase, List<Flor> flores) {
        super(id, nombre, precio, stock, "centro", descrip, img);
        this.idCentro = id;
        this.FlorPr = pr;
        this.size = size;
        this.frase = frase;
        this.floresSecun = flores;
    }

    public Centro (int id, String nombre, Double precio, int stock, String descrip, Flor pr, String size, String frase, List<Flor> flores) {
        super(id, nombre, precio, stock, "centro", descrip);
        this.idCentro = id;
        this.FlorPr = pr;
        this.size = size;
        this.frase = frase;
        this.floresSecun = flores;
    }

    // GETTERS
    public int getIdCentro() {
        return idCentro;
    }

    public Flor getFlorPr() {
        return FlorPr;
    }

    public String getSize() {
        return size;
    }

    public String getFrase() {
        return frase;
    }

    public List<Flor> getFloresSecun() {
        return floresSecun;
    }

    // SETTERS
    public void setIdCentro(int idCentro) {
        this.idCentro = idCentro;
    }

    public void setFlorPr(Flor florPr) {
        FlorPr = florPr;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public void setFloresSecun(List<Flor> floresSecun) {
        this.floresSecun = floresSecun;
    }

    // EQUALS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Centro centro = (Centro) o;
        return idCentro == centro.idCentro;
    }

    // HASHCODE
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idCentro);
    }

    // ToString

    @Override
    public String toString() {
        return "Centro{" +
                "idCentro=" + idCentro +
                ", FlorPr=" + FlorPr +
                ", size='" + size + '\'' +
                ", frase='" + frase + '\'' +
                ", floresSecun=" + floresSecun +
                '}';
    }
}
