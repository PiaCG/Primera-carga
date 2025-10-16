package com.devst.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PerfilActivity extends AppCompatActivity {

    private EditText edtNombre;
    private TextView tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        edtNombre = findViewById(R.id.edtNombre);
        tvEmail = findViewById(R.id.tvEmail);
        Button btnGuardar = findViewById(R.id.btnGuardar);

        // recibir datos
        String email = getIntent().getStringExtra("email_usuario");
        tvEmail.setText("Corre: " + (email != null ? email : "N/A"));

        // Devolver reesultado
        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = edtNombre.getText().toString().trim();

            if (nuevoNombre.isEmpty()) {
                // Mostrar el Toast. El return sale del listener.
                Toast.makeText(this, "Ingresa un nombre para guardar.", Toast.LENGTH_SHORT).show();
                return; // Sale del setOnClickListener (no de la Activity)
            }

            // Crear el Intent para enviar el resultado (Explícito con Resultado)
            Intent resultadoIntent = new Intent();
            resultadoIntent.putExtra("nombre_editado", nuevoNombre); //

            // Establecer el resultado como OK (CRÍTICO)
            setResult(RESULT_OK, resultadoIntent);

            // Cerrar esta Activity y volver a HomeActivity
            finish();
        });

        // Para manejar el botón Atrás (sustituye al obsoleto onBackPressed())
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Enviar resultado CANCELADO si el usuario usa el botón de Atrás
                setResult(RESULT_CANCELED);
                finish(); // Cierra la Activity
            }
        });
    }
}