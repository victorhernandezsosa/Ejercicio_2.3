package com.example.ejercicio_23;

import java.sql.Blob;

public class Photograh {
    private byte[] imagen;
    private String descripcion;

    public Photograh(byte[] imagen, String descripcion) {
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public Photograh() {
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
