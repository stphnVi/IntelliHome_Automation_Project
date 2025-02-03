package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ResultadosBusqueda extends AppCompatActivity {

    private LinearLayout layoutPrincipal;
    private Button botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultados_busqueda);

        layoutPrincipal = findViewById(R.id.layoutPrincipal);
        botonVolver = findViewById(R.id.botonVolver);

        // Agregar dinámicamente layouts para cada casa
        for (Casa casa : ListaCasas.listaCasas) {
            // Crear un LinearLayout vertical para cada casa
            LinearLayout casaLayout = new LinearLayout(this);
            casaLayout.setOrientation(LinearLayout.VERTICAL);
            casaLayout.setPadding(16, 16, 16, 16);
            casaLayout.setBackgroundResource(android.R.color.white);
            casaLayout.setGravity(Gravity.CENTER_VERTICAL);

            // Crear un TextView para mostrar la información de la casa
            TextView nombreTextView = new TextView(this);
            nombreTextView.setText("Nombre: " + casa.nombrePropiedad);
            nombreTextView.setTextSize(18);

            TextView capacidadTextView = new TextView(this);
            capacidadTextView.setText("Capacidad: " + casa.capacidadMaxima);

            TextView precioTextView = new TextView(this);
            precioTextView.setText("Precio: " + casa.precio + " colones");

            // Añadir TextViews al layout de cada casa
            casaLayout.addView(nombreTextView);
            casaLayout.addView(capacidadTextView);
            casaLayout.addView(precioTextView);

            // Añadir el layout de la casa al layout principal
            layoutPrincipal.addView(casaLayout);

            // Añadir espacio entre las casas
            View separator = new View(this);
            separator.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    20
            ));
            layoutPrincipal.addView(separator);
        }

        botonVolver.setOnClickListener(view -> { // mapeo del boton para alquilar
            Intent intent = new Intent(ResultadosBusqueda.this, BusquedaAlquiler.class);
            startActivity(intent);
        });



    }
}
