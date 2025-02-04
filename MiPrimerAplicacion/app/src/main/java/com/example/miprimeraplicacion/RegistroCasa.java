package com.example.miprimeraplicacion;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

public class RegistroCasa extends AppCompatActivity {

    private boolean pantallaRegistroCasasAbierta;
    private TextView textoUbicaciones;

    // posicion del tipo elegido
    int position = 0;
    private ArrayList<Uri> imageUris;

    //pedir el codico para elegir imagenes
    private static final int PICK_IMAGES_CODE = 0;
    private ImageSwitcher casaImageView;
    private String imagePath = null;
    private String[] imagePaths = new String[4];


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
        Button buttonAgregarImagenes = findViewById(R.id.botonAgregarImagenes);
        FloatingActionButton buttonForward = findViewById(R.id.derecha);
        FloatingActionButton buttonBack = findViewById(R.id.izquierda);

        // Imagenes casa
        casaImageView = findViewById(R.id.casaImageView);

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
                String messageSend = "func: regcasa" + ", nombre de la propiedad: " + nombrepropiedad + ", ubi:" + Ubicaciones + ", reglas de uso: " + reglasuso + ", amenidades:" + amenidades + ", capacidad maxima: " + capacidadmaxima + ", precio: " + precio;
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

        //Se crea la lista de imagenes
        imageUris = new ArrayList<>();

        //Se crea el cambiador de imagenes
        casaImageView.setFactory(() -> new ImageView(getApplicationContext()));

        buttonAgregarImagenes.setOnClickListener(view -> {

            pickImagesIntent();

        });

        buttonForward.setOnClickListener(view -> {

            if (position < imageUris.size() - 1) {
                position++;
                casaImageView.setImageURI(imageUris.get(position));
            }
            else {
                Toast.makeText(this, "No hay mas imagenes...", Toast.LENGTH_SHORT).show();
            }

        });

        buttonBack.setOnClickListener(view -> {

            if (position > 0) {
                position--;
                casaImageView.setImageURI(imageUris.get(position));
            }
            else {
                Toast.makeText(this, "No hay imagenes previas...", Toast.LENGTH_SHORT).show();
            }
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

    private void pickImagesIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_CODE) {

            if (resultCode== Activity.RESULT_OK) {

                if (data.getClipData() != null) {
                    // Se eligieron varias imagenes

                    int count = data.getClipData().getItemCount(); // numero de imagenes elegidas
                    if (count <= 4) { // Se eligen menos de 4 casas
                        for (int i=0; i<count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            imagePath = imageUri.getPath();
                            imageUris.add(imageUri); //add to list
                        }
                        //Se pone la primer imagen en el image view
                        casaImageView.setImageURI(imageUris.get(0));
                        position = 0;
                        casaImageView.setBackground(getDrawable(R.color.Azul));
                    }
                    else { // se eligen mas de 12 casas
                        Toast.makeText(this, "El maximo de imagenes por casa es 4", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    // Se eligio una sola imagen
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                    //Se pone la primer imagen en el image view
                    casaImageView.setImageURI(imageUris.get(0));
                    position = 0;
                }
            }
        }
    }


}