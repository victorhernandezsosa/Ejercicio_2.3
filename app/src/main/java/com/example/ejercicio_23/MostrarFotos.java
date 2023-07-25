package com.example.ejercicio_23;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import com.example.ejercicio_23.Configuracion.SQLiteConexion;
import com.example.ejercicio_23.Configuracion.Tabla;

import java.util.ArrayList;
import java.util.List;

public class MostrarFotos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_fotos);

        ListView listView = findViewById(R.id.listView);
        List<Photograh> lista = ObtenerDatos();
        CustomAdapter adapter = new CustomAdapter(this,lista);
        listView.setAdapter(adapter);
    }

    private List<Photograh> ObtenerDatos() {

        List<Photograh> lista = new ArrayList<>();

        SQLiteConexion conexion = new SQLiteConexion(this, Tabla.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getReadableDatabase();

        String [] columnas = {Tabla.descripcion,Tabla.imagen};
        Cursor cursor = db.query(Tabla.tablaFotografia,columnas,null,null,null,null,null);

        while (cursor.moveToNext()){
            String descripcion = cursor.getString(cursor.getColumnIndex(Tabla.descripcion));
            byte[] fotobytes = cursor.getBlob(cursor.getColumnIndex(Tabla.imagen));

            Photograh photo = new Photograh(fotobytes,descripcion);
            lista.add(photo);
        }

        cursor.close();
        db.close();

        return lista;
    }

}