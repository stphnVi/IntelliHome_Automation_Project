package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OpcionSeleccionada extends AppCompatActivity {
    private TextView nombrePropiedad;
    private TextView reglasUso;
    private TextView capacidad;
    private TextView amenidades;
    private TextView precio;
    private Button botonVolver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opcion_seleccionada);

        nombrePropiedad = findViewById(R.id.nombrepropiedad);
        reglasUso = findViewById(R.id.reglasuso);
        amenidades = findViewById(R.id.amenidades);
        capacidad = findViewById(R.id.cmaxima);
        precio = findViewById(R.id.precio);
        botonVolver = findViewById(R.id.regresar);


        nombrePropiedad.setText(CasaSeleccionada.nombrePropiedad);
        capacidad.setText(CasaSeleccionada.capacidadMaxima);
        precio.setText(CasaSeleccionada.precio);

        String amenidadesString = CasaSeleccionada.convertirListaEnString(CasaSeleccionada.amenidades);
        String reglasString = CasaSeleccionada.convertirListaEnString(CasaSeleccionada.reglas);

        reglasUso.setText(reglasString);
        amenidades.setText(amenidadesString);

        botonVolver.setOnClickListener(view -> { // mapeo del boton para alquilar
            Intent intent = new Intent(OpcionSeleccionada.this, ResultadosBusqueda.class);
            startActivity(intent);
        });


    }
}
