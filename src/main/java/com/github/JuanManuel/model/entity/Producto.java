package com.github.JuanManuel.model.entity;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class Producto {
    private int idProducto;
    private String nombre;
    private Double precio;
    private int stock;
    private String tipo;
    private String descripcion;
    private byte[] img;

    private static final File imgNull = new File("src/main/resources/com/github/JuanManuel/view/images/noPicture.jpg");

    public Producto() {}

    public Producto(int id) {this.idProducto = id;}

    public Producto(int idProducto, String nombre, Double precio, int stock, String tipo, String descripcion, File img) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.img = ImageToByte(img);
    }

    public Producto(int idProducto, String nombre, Double precio, int stock, String tipo, String descripcion, byte[] img) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.img = img;
    }

    public Producto(int idProducto, String nombre, Double precio, int stock, String tipo, String descripcion) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.img = ImageToByte(imgNull);
    }

    //
    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public byte[] getImg() {
        return img;
    }

    // SETTERS
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setImg(byte[] img) {
        this.img = img;
    }
    public void setImg(File img) {
        this.img = ImageToByte(img);
    }

    // EQUALS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return idProducto == producto.idProducto;
    }

    // HASHCODE
    @Override
    public int hashCode() {
        return Objects.hashCode(idProducto);
    }

    // ToString
    @Override
    public String toString() {
        return "\nProducto{" +
                "idProducto=" + idProducto +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", tipo='" + tipo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", img=" + img +
                '}';
    }


    public byte[] ImageToByte(File img) {
        byte [] imageData = null;
        imageData = new byte[(int) img.length()];
        try (FileInputStream fis = new FileInputStream(img)) {
            int bytesRead = fis.read(imageData);
            if (bytesRead != imageData.length) {
                throw new IOException("No se pudieron leer todos los datos del archivo.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo: " + img.getPath(), e);
        }
        return imageData;
    }
}
