package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.SeekBar;
import androidx.appcompat.app.AlertDialog;
import android.view.View;

import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;


public class BusquedaAlquiler extends AppCompatActivity {

    private boolean pantallaRegistroCasasAbierta;
    private TextView textoUbicaciones;
    private TextView textCapacidades;
    private SeekBar seekBar;

    private LinearLayout busquedaLayout;
    private TextView textoCapacidad;

    private boolean isSeekBarVisible = false;


    // Lista de amenidades
    String[] amenidades = {"Cocina equipada (con electrodomesticos modernos)",
            "Aire acondicionado",
            "Calefaccion",
            "Wi-Fi gratuito",
            "Television por cable o satelite",
            "Lavadora y secadora",
            "Piscina",
            "Jardin o patio",
            "Barbacoa o parrilla",
            "Terraza o balcon",
            "Gimnasio en casa",
            "Garaje o espacio de estacionamiento",
            "Sistema de seguridad",
            "Habitaciones con baño en suite",
            "Muebles de exterior",
            "Microondas",
            "Lavavajillas",
            "Cafetera",
            "Ropa de cama y toallas incluidas",
            "Acceso a areas comunes (piscina, gimnasio)",
            "Camas adicionales o sofa cama",
            "Servicios de limpieza opcionales",
            "Acceso a transporte público cercano",
            "Mascotas permitidas",
            "Cercania a tiendas y restaurantes",
            "Sistema de calefaccion por suelo radiante",
            "Escritorio o area de trabajo",
            "Sistemas de entretenimiento (videojuegos, equipo de musica)",
            "Chimenea",
            "Acceso a internet de alta velocidad" };

    boolean[] amenidadesSeleccionadas = new boolean[amenidades.length];
    ArrayList<String> seleccionadas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);

        Button botonCancelar = findViewById(R.id.cancelar);

        botonCancelar.setOnClickListener(view -> { // mapeo del boton para alquilar
            Intent intent = new Intent(BusquedaAlquiler.this, PrincipalActivity.class);
            startActivity(intent);
        });


        //Mapa-coordenadas

        // Recuperar las coordenadas del Intent
        double latitud = getIntent().getDoubleExtra("latitud", 0.0);  // Valor por defecto 0.0
        double longitud = getIntent().getDoubleExtra("longitud", 0.0);  // Valor por defecto 0.0

        // Mostrar las coordenadas en el TextView (o usarlas como lo necesites)
        textoUbicaciones = findViewById(R.id.textView6);
        textoUbicaciones.setText("latitud: " + latitud + ", longitud: " + longitud);

        //--------------------------------------------------------------------------------Amenidades
        Button botonAmenidades = findViewById(R.id.amenidades);
        TextView textoSeleccionadas = findViewById(R.id.textView7);

        botonAmenidades.setOnClickListener(view -> {
            // Crear el AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(BusquedaAlquiler.this);
            builder.setTitle("Selecciona las amenidades");

            // Configurar las opciones con CheckBox
            builder.setMultiChoiceItems(amenidades, amenidadesSeleccionadas, (dialog, which, isChecked) -> {
                if (isChecked) {
                    // Agregar amenidad seleccionada
                    seleccionadas.add(amenidades[which]);
                } else {
                    // Quitar amenidad deseleccionada
                    seleccionadas.remove(amenidades[which]);
                }
            });

            // Botón de confirmación
            builder.setPositiveButton("Aceptar", (dialog, which) -> {
                // Mostrar las amenidades seleccionadas en el TextView
                textoSeleccionadas.setText(String.join(", ", seleccionadas));
            });

            // Botón de cancelación
            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

            // Mostrar el diálogo
            builder.create().show();
        });



        //---------------------------------------------------------------------------Muestra el mapa

        // Botón para abrir el mapa en un cuadro de diálogo
        Button botonSeleccionarUbicacion = findViewById(R.id.ubicacion);
        botonSeleccionarUbicacion.setOnClickListener(view -> {
            // Crear el DialogFragment
            MapaDialogFragment mapaDialogFragment = new MapaDialogFragment();
            mapaDialogFragment.show(getSupportFragmentManager(), "MapaDialog");

        });

        //----------------------------------------------------------------Muestra barra de Capacidad

    }

}