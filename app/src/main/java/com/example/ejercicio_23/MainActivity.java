package com.example.ejercicio_23;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ejercicio_23.Configuracion.SQLiteConexion;
import com.example.ejercicio_23.Configuracion.Tabla;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnguardar,btnfoto,btnir;
    EditText descripcion;
    ImageView imagen;

    static final int peticion_captura_imagen = 101;
    static final int peticion_acceso_camara = 102;

    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnguardar = (Button) findViewById(R.id.btnsave);
        btnfoto = (Button) findViewById(R.id.btnfoto);
        btnir = (Button) findViewById(R.id.btnir);
        descripcion = (EditText) findViewById(R.id.descripcion);
        imagen = (ImageView) findViewById(R.id.imagen);

        btnir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MostrarFotos.class);
                startActivity(intent);
            }
        });
        
        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap foto = obtenerfoto();
                AddFoto(foto);
            }
        });
        
        btnfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permisos();
            }
        });
    }

    private void permisos()
    {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},peticion_acceso_camara);
        }
        else
        {
            dispatchTakePictureIntent();
            //TomarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_acceso_camara)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakePictureIntent();
                //TomarFoto();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "se necesita el permiso de la camara",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == peticion_captura_imagen)
        {
            //Bundle extra = data.getExtras();
            //Bitmap imagen = (Bitmap) extra.get("data");
            //imageView.setImageBitmap(imagen);

            try {
                File foto = new File(currentPhotoPath);
                imagen.setImageURI(Uri.fromFile(foto));
            }
            catch (Exception ex)
            {
                ex.toString();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.toString();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.ejercicio_23.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, peticion_captura_imagen);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private byte[] convertirimagen(Bitmap imagen){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imagen.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private Bitmap obtenerfoto() {
        imagen.setDrawingCacheEnabled(true);
        imagen.buildDrawingCache();
        Bitmap foto = imagen.getDrawingCache();

        return foto;
    }

    private void AddFoto(Bitmap imagen){
        byte[] foto = convertirimagen(imagen);

        SQLiteConexion conexion = new SQLiteConexion(this, Tabla.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Tabla.descripcion, descripcion.getText().toString());
        valores.put(Tabla.imagen, foto);

        Long result = db.insert(Tabla.tablaFotografia, Tabla.id, valores);
        Toast.makeText(getApplicationContext(),"Registro Ingresado",Toast.LENGTH_LONG).show();

        db.close();

        CleanScreen();
    }

    private void CleanScreen() {
        imagen.setImageBitmap(null);
        descripcion.setText("");
    }

}