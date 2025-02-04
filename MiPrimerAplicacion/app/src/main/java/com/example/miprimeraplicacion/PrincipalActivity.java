package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PrincipalActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Esto cierra todas las actividades en la pila
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        ImageButton buttonAgregar = findViewById(R.id.agregarCasa);
        ImageButton botoncasamodelo = findViewById(R.id.casamodelo);
        ImageButton botonBusqueda = findViewById(R.id.dirigeNavBar);

        botonBusqueda.setOnClickListener(view -> { // mapeo del boton para alquilar
            Intent intent = new Intent(PrincipalActivity.this, BusquedaAlquiler.class);
            startActivity(intent);
        });

        botoncasamodelo.setOnClickListener(view -> { // mapeo del boton casa modelo
            Intent intent = new Intent(PrincipalActivity.this, CasaModelo.class);
            startActivity(intent);
        });

        buttonAgregar.setOnClickListener(view -> { // mapeo del boton agregar
            Intent intent = new Intent(PrincipalActivity.this, RegistroCasa.class);
            startActivity(intent);
        });

        ImageButton buttonCancel = findViewById(R.id.button2);
        buttonCancel.setOnClickListener(view -> { // mapeo del boton exit
            // Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
            // startActivity(intent);
            finishAffinity(); // Cierra esta actividad o pantalla
        });

    }
}
