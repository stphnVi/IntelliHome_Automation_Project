package com.example.miprimeraplicacion;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegistroCasa extends AppCompatActivity {

    private boolean pantallaRegistroCasasAbierta;

    // Lista de amenidades
    String[] amenidades = {"Cocina equipada (con electrodomésticos modernos)",
                            "Aire acondicionado",
                            "Calefacción",
                            "Wi-Fi gratuito",
                            "Televisión por cable o satélite",
                            "Lavadora y secadora",
                            "Piscina",
                            "Jardín o patio",
                            "Barbacoa o parrilla",
                            "Terraza o balcón",
                            "Gimnasio en casa",
                            "Garaje o espacio de estacionamiento",
                            "Sistema de seguridad",
                            "Habitaciones con baño en suite",
                            "Muebles de exterior",
                            "Microondas",
                            "Lavavajillas",
                            "Cafetera",
                            "Ropa de cama y toallas incluidas",
                            "Acceso a áreas comunes (piscina, gimnasio)",
                            "Camas adicionales o sofá cama",
                            "Servicios de limpieza opcionales",
                            "Acceso a transporte público cercano",
                            "Mascotas permitidas",
                            "Cercanía a tiendas y restaurantes",
                            "Sistema de calefacción por suelo radiante",
                            "Escritorio o área de trabajo",
                            "Sistemas de entretenimiento (videojuegos, equipo de música)",
                            "Chimenea",
                            "Acceso a internet de alta velocidad" };

    boolean[] amenidadesSeleccionadas = new boolean[amenidades.length];
    ArrayList<String> seleccionadas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_casa);

        EditText nombrepropiedadEditText = findViewById(R.id.nombrepropiedad);
        EditText reglasusoEditText = findViewById(R.id.reglasuso);
        EditText capacidadmaximaEditText = findViewById(R.id.cmaxima);
        EditText precioEditText = findViewById(R.id.precio);

        Button buttonRegresar= findViewById(R.id.regresar);
        Button buttonRegistrar = findViewById(R.id.registrar);


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

        buttonRegistrar.setOnClickListener(view -> { // mapeo del boton exit
            //Asignacion de variables
            String nombrepropiedad = nombrepropiedadEditText.getText().toString();
            String reglasuso = reglasusoEditText.getText().toString();
            String capacidadmaxima = capacidadmaximaEditText.getText().toString();
            String precio = precioEditText.getText().toString();
            String amenidades = textoSeleccionadas.getText().toString();

            String messageSend = "func: rec" + ", nombre de la propiedad: " + nombrepropiedad + ", reglas de uso: " + reglasuso + ", amenidades:" + amenidades + ", capacidad maxima: " + capacidadmaxima + ", precio: " +  precio;
            Socket.sendMessage(messageSend);



        });
        new Thread(() -> {
            while (pantallaRegistroCasasAbierta == true) {
                // Escuchar continuamente los mensajes del servidor
                if (com.example.miprimeraplicacion.Socket.message != null) {
                    procesarMensaje();
                    //textViewChat.append("Servidor: " + message + "\n");
                }
            }

        }).start();


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
