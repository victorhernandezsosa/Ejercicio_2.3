package com.example.ejercicio_23.Configuracion;

import java.sql.Blob;

public class Tabla {

    //Nombre de la base de datos
    public static final String NameDatabase = "Foto";

    //Tabla Base de Datos

    public static final String tablaFotografia = "fotografia";

    //Campos de la tabla personas

    public static final String id = "id";
    public static final String descripcion = "descripcion";
    public static final String imagen = "imagen";

    //DDL Create and Drop

    public static final String CreateTableFotografia = "CREATE TABLE FOTOGRAFIA"+ "(id INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT, imagen BLOB)";

    public static final String DROPTableFotografia = "DROP TABLE IF EXISTS fotografia";

    public static final String SelectTableFotografia = "SELECT * FROM " + Tabla.tablaFotografia;
}
