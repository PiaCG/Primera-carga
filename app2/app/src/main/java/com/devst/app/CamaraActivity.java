package com.devst.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamaraActivity extends AppCompatActivity {


    private ImageView imagenPrevia;
    private Uri urlImagen;


    private final ActivityResultLauncher<String> permisoCamaraLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), carga -> {
                if (carga) tomarFoto();
                else Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            });

    private final ActivityResultLauncher<Uri> takePictureLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), okay -> {
                if (okay && urlImagen != null) {
                    imagenPrevia.setImageURI(urlImagen);
                    Toast.makeText(this, "Foto guardada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Captura cancelada", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<String>galeriaLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imagenPrevia.setImageURI(uri);
                    Toast.makeText(this, "Foto seleccionada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Selección cancelada", Toast.LENGTH_SHORT).show();
                }
            });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        Button btnTomarFoto = findViewById(R.id.btnTomarFoto);
        imagenPrevia = findViewById(R.id.imgPreview);

        Button btnGaleria = findViewById(R.id.btnGaleria);

        btnTomarFoto.setOnClickListener(v -> checkPermisoYTomar());
        btnGaleria.setOnClickListener(v -> galeriaLauncher.launch("image/*"));
    }



    private void checkPermisoYTomar() {
        boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        if (granted) tomarFoto();
        else permisoCamaraLauncher.launch(Manifest.permission.CAMERA);
    }




    private void btnTomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    assert result.getData() != null;
                    Bundle extras = result.getData().getExtras();
                    assert extras != null;
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imagenPrevia.setImageBitmap(imageBitmap);
                }
            });

        }
    }

    private void tomarFoto() {
        try {
            File archivoFoto = crearArchivoImagen();
            urlImagen = FileProvider.getUriForFile(
                    this, getPackageName() + ".fileprovider", archivoFoto);
            takePictureLauncher.launch(urlImagen);
        } catch (IOException e) {
            Toast.makeText(this, "No se pudo crear el archivo de imagen", Toast.LENGTH_SHORT).show();
        }
    }


    private File crearArchivoImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = "IMG_" + timeStamp + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(nombre, ".jpg", dir);
    }
}