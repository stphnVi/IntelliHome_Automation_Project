package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;

public class RegistroCasa extends AppCompatActivity {

    private boolean pantallaRegistroCasasAbierta;
    private TextView textoUbicaciones;


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
    public void onBackPressed() {
        super.onBackPressed();
        pantallaRegistroCasasAbierta = false;
        Intent intent = new Intent(RegistroCasa.this, PrincipalActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_casa);

        EditText nombrepropiedadEditText = findViewById(R.id.nombrepropiedad);
        EditText reglasusoEditText = findViewById(R.id.reglasuso);
        EditText capacidadmaximaEditText = findViewById(R.id.cmaxima);
        EditText precioEditText = findViewById(R.id.precio);

        //Botones
        Button buttonRegresar= findViewById(R.id.regresar);
        Button buttonRegistrar = findViewById(R.id.registrar);

        //Mapa

        // Recuperar las coordenadas del Intent
        double latitud = getIntent().getDoubleExtra("latitud", 0.0);  // Valor por defecto 0.0
        double longitud = getIntent().getDoubleExtra("longitud", 0.0);  // Valor por defecto 0.0

        // Mostrar las coordenadas en el TextView (o usarlas como lo necesites)
        textoUbicaciones = findViewById(R.id.textoUbicaciones);
        textoUbicaciones.setText("latitud: " + latitud + ", longitud: " + longitud);


        //Amenidades
        Button botonAmenidades = findViewById(R.id.botonAmenidades);
        TextView textoSeleccionadas = findViewById(R.id.textoSeleccionadas);


        botonAmenidades.setOnClickListener(view -> {
            // Crear el AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroCasa.this);
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



        //Cada vez que se abre la pantalla de RegistroCasas se indica en el boolean como true
        pantallaRegistroCasasAbierta = true;

        buttonRegresar.setOnClickListener(view -> { // mapeo del boton exit
            pantallaRegistroCasasAbierta = false;
            Intent intent = new Intent(RegistroCasa.this, PrincipalActivity.class);
            startActivity(intent);

        });


        buttonRegistrar.setOnClickListener(view -> { // mapeo del boton registrar
            //Asignacion de variables
            String nombrepropiedad = nombrepropiedadEditText.getText().toString();
            String reglasuso = reglasusoEditText.getText().toString();
            String capacidadmaxima = capacidadmaximaEditText.getText().toString();
            String precio = precioEditText.getText().toString();
            String amenidades = textoSeleccionadas.getText().toString();
            String Ubicaciones = textoUbicaciones.getText().toString();

            // Validar campos vacíos

            if (nombrepropiedad.isEmpty()) {
                marcarCampoTemporalmente(nombrepropiedadEditText);
            }

            if (reglasuso.isEmpty()) {
                marcarCampoTemporalmente(reglasusoEditText);
            }

            if (capacidadmaxima.isEmpty()) {
                marcarCampoTemporalmente(capacidadmaximaEditText);
            }

            if (precio.isEmpty()) {
                marcarCampoTemporalmente(precioEditText);
            }

            if (Ubicaciones.equals("latitud: 0.0, longitud: 0.0")) {
                //Mensaje de completar
                Toast.makeText(this, "No agregaste la ubicacion", Toast.LENGTH_SHORT).show();
            }

            if (!nombrepropiedad.isEmpty() && !reglasuso.isEmpty() && !capacidadmaxima.isEmpty() && !precio.isEmpty() && !Ubicaciones.isEmpty()) {
                String messageSend = "func: regcasa" + ", nombre de la propiedad: " + nombrepropiedad + ", Ubicacion:" + Ubicaciones + ", reglas de uso: " + reglasuso + ", amenidades:" + amenidades + ", capacidad maxima: " + capacidadmaxima + ", precio: " + precio;
                Socket.sendMessage(messageSend);
            }


        });


        // Botón para abrir el mapa en un cuadro de diálogo
        Button botonSeleccionarUbicacion = findViewById(R.id.botonSeleccionarUbicacion);
        botonSeleccionarUbicacion.setOnClickListener(view -> {
            // Crear el DialogFragment
            MapaDialogFragment mapaDialogFragment = new MapaDialogFragment();
            mapaDialogFragment.show(getSupportFragmentManager(), "MapaDialog");

        });



        new Thread(() -> {
            while (pantallaRegistroCasasAbierta == true) {
                // Escuchar continuamente los mensajes del servidor
                if (Socket.message != null) {
                    procesarMensaje();
                    //textViewChat.append("Servidor: " + message + "\n");
                }
            }

        }).start();

    }

    // Método para cambiar el fondo de un campo a rojo temporalmente
    private void marcarCampoTemporalmente(EditText editText) {
        // Cambiar el fondo a rojo
        editText.setBackgroundResource(android.R.color.holo_red_light);

        // Restaurar el fondo blanco después de 3 segundos
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            editText.setBackgroundResource(android.R.color.white); // Reestablecer el color blanco
        }, 2000); // 2000 ms = 2 segundos
    }

    /**
     * Se procesan los mensajes del servidor
     * SIEMPRE al final de cada if poner "Socket.message=null"
     */
    private void procesarMensaje(){

        runOnUiThread(() -> {
            if (com.example.miprimeraplicacion.Socket.message != null) {
                String message = com.example.miprimeraplicacion.Socket.message;
                if ("1".equals(message)) {
                    // Abrir nueva ventana si el mensaje es "1"
                    pantallaRegistroCasasAbierta = false;
                    Intent intent = new Intent(RegistroCasa.this, PrincipalActivity.class);
                    startActivity(intent);
                    Socket.message = null;
                } else if ("0".equals(message)) {
                    // Mostrar mensaje de credenciales incorrectas
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroCasa.this);
                    builder.setTitle("Error de registro")
                            .setMessage("Faltan espacios por completar. Por favor, completelos.")
                            .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                            .create()
                            .show();
                    Socket.message = null;

                } else {
                    // Manejar otros mensajes si es necesario
                    Socket.message = null;


                }
            }
            else {

            }
        });
    }
}