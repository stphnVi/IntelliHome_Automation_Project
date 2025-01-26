package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PrincipalActivity extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        ImageButton buttonAgregar= findViewById(R.id.agregarCasa);

        buttonAgregar.setOnClickListener(view -> { // mapeo del boton agregar
            Intent intent = new Intent(PrincipalActivity.this, RegistroCasa.class);
            startActivity(intent);
        });

        Button buttonCancel = findViewById(R.id.button2);
        buttonCancel.setOnClickListener(view -> { // mapeo del boton exit
            Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }
}
