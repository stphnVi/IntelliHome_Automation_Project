package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PreguntasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preguntas_seguridad);
        Button buttonCancel = findViewById(R.id.cancelRecoveryButton);
        buttonCancel.setOnClickListener(view -> { // mapeo del boton exit
            Intent intent = new Intent(PreguntasActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }
}
