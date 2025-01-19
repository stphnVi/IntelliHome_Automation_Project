package com.example.miprimeraplicacion;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;

public class ExitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        Button buttonCancel = findViewById(R.id.cancelButton);
        buttonCancel.setOnClickListener(view -> { // mapeo del boton exit
            Intent intent = new Intent( ExitActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Configurar la animación de degradado
        //TextView textView = findViewById(R.id.textViewExit);
        AlphaAnimation fade = new AlphaAnimation(0, 1);
        fade.setDuration(4000); // Aumenta la duración de la animación
        fade.setRepeatMode(AlphaAnimation.REVERSE);
        fade.setRepeatCount(AlphaAnimation.INFINITE);
        //textView.startAnimation(fade);

        // Configurar el botón para salir de la aplicación
        //Button buttonExitApp = findViewById(R.id.buttonExitApp);
        /*buttonExitApp.setOnClickListener(view -> {
            finishAffinity(); // Cierra esta actividad o pantalla
            System.exit(0); // Sale de la aplicación
        });*/
    }
}
