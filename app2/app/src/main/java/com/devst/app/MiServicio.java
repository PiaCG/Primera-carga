package com.devst.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MiServicio extends Service {

    private static final String TAG = "MiServicio";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Servicio creado: Tarea iniciada en background.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Sincronización Iniciada (5s)", Toast.LENGTH_LONG).show();

        // Ejecución simulada de la tarea principal con Thread interno
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Simulación de tarea de 5 segundos
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Log.d(TAG, "Servicio finalizado: Sincronización completada.");

            // Usamos un Handler para mostrar el Toast final en el hilo principal
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                Toast.makeText(getApplicationContext(), "Sincronización de datos completada", Toast.LENGTH_LONG).show();
            });

            stopSelf(); // Detener el servicio al finalizar la tarea
        }).start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Servicio destruido.");
    }
}