package com.example.miprimeraplicacion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.widget.SeekBar;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.Menu;

import android.view.LayoutInflater;




import androidx.appcompat.app.AppCompatActivity;


public class BusquedaAlquiler extends AppCompatActivity {

    private boolean pantallaBusquedaAlquilerAbierta;

    private String fechaEntrada = "";
    private String fechaSalida = "";

    private TextView rangoFechasText;
    private Button selectDateButton;

    private TextView capacidadText;

    private TextView PrecioSeleccionado;

    private int cantidad;

    private int precio;



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

        ListaCasas.listaCasas.clear();

        EditText UbiEditText = findViewById(R.id.UbiText);

        rangoFechasText = findViewById(R.id.textFecha);
        selectDateButton = findViewById(R.id.fecha);
        capacidadText = findViewById(R.id.textCapacidad);
        PrecioSeleccionado = findViewById(R.id.textPrecio);
        Button CantidadButton = findViewById(R.id.capacidad);
        Button PrecioButton = findViewById(R.id.precio);



        Button botonCancelar = findViewById(R.id.volver);
        Button botonAceptar = findViewById(R.id.aceptar);
        Button botonLimpiar = findViewById(R.id.limpiar);

        pantallaBusquedaAlquilerAbierta = true;

        botonCancelar.setOnClickListener(view -> { // mapeo del boton para alquilar
            Intent intent = new Intent(BusquedaAlquiler.this, PrincipalActivity.class);
            startActivity(intent);
        });



        //--------------------------------------------------------------------------------Amenidades
        Button botonAmenidades = findViewById(R.id.amenidades);
        TextView textoSeleccionadas = findViewById(R.id.textAmenidades);

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

        //--------------------------------------------------------------------------------Fecha


        selectDateButton.setOnClickListener(view -> {
            Log.d("BusquedaAlquiler", "Botón de selección de fecha");
            mostrarDatePickerDialogEntrada();
        });

        //--------------------------------------------------------------------------------Precio


        PrecioButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BusquedaAlquiler.this);
            builder.setTitle("Seleccione el precio por noche");
            LinearLayout layout = new LinearLayout(BusquedaAlquiler.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(40, 40, 40, 40);


            SeekBar seekBar = new SeekBar(BusquedaAlquiler.this);
            seekBar.setMax(15);
            seekBar.setProgress(0);
            seekBar.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            TextView PrecioText = new TextView(BusquedaAlquiler.this);
            PrecioText.setText(String.valueOf(seekBar.getProgress()));
            PrecioText.setTextSize(18);

            layout.addView(seekBar);
            layout.addView(PrecioText);

            builder.setView(layout);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    PrecioText.setText("₡" + String.valueOf(progress * 5000)); // Actualizar el TextView con el valor del SeekBar
                    precio = progress * 5000; // Guardar el valor de progreso en la variable cantidad
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            // Botón de confirmación (Guardar)
            builder.setPositiveButton("Guardar", (dialog, which) -> {

                //TextView capacidadText = findViewById(R.id.textCapacidad);
                PrecioSeleccionado.setText(String.valueOf(precio));

            });


            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());


            builder.create().show();
        });


        //--------------------------------------------------------------------------------Capacidad


        CantidadButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(BusquedaAlquiler.this);
            builder.setTitle("Seleccione la cantidad de personas");
            LinearLayout layout = new LinearLayout(BusquedaAlquiler.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(40, 40, 40, 40);


            SeekBar seekBar = new SeekBar(BusquedaAlquiler.this);
            seekBar.setMax(10);
            seekBar.setProgress(0);
            seekBar.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));


            TextView capacityText = new TextView(BusquedaAlquiler.this);
            capacityText.setText(String.valueOf(seekBar.getProgress()));
            capacityText.setTextSize(18);

            layout.addView(seekBar);
            layout.addView(capacityText);

            builder.setView(layout);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    capacityText.setText(String.valueOf(progress)); // Actualizar el TextView con el valor del SeekBar
                    cantidad = progress; // Guardar el valor de progreso en la variable cantidad
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            // Botón de confirmación (Guardar)
            builder.setPositiveButton("Guardar", (dialog, which) -> {

                //TextView capacidadText = findViewById(R.id.textCapacidad);
                capacidadText.setText(String.valueOf(cantidad));

            });


            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());


            builder.create().show();
        });



        //--------------------------------------------------------------------------------Ubicación Y envio de datos al server

        botonAceptar.setOnClickListener(view -> { // mapeo del boton para alquilar
            //Intent intent = new Intent(BusquedaAlquiler.this, PrincipalActivity.class);
            //startActivity(intent);
            String ubicacion = UbiEditText.getText().toString();
            String amenidades = textoSeleccionadas.getText().toString();
            String fecha = rangoFechasText.getText().toString();
            String capacidadElegida = capacidadText.getText().toString();
            String precioElegido = PrecioSeleccionado.getText().toString();

            // Verificar si algún campo está vacío y reemplazar con "-1"
            if (ubicacion.isEmpty()) {
                ubicacion = "-1";
            }
            if (amenidades.isEmpty()) {
                amenidades = "-1";
            }
            if (fecha.isEmpty()) {
                fecha = "-1";
            }
            if (capacidadElegida.isEmpty()) {
                capacidadElegida = "-1";
            }
            if (precioElegido.isEmpty()) {
                precioElegido = "-1";
            }
            //cambiar func
            String messageSend = "func: buscarCasa" + ", ubi: " + ubicacion + "; amenidades:" + amenidades + "; fecha: " + fecha + "; capacidad:" + capacidadElegida + "; precio:" + precioElegido;
            Socket.sendMessage(messageSend);
        });

        botonLimpiar.setOnClickListener(view -> {

            UbiEditText.setText("");
            textoSeleccionadas.setText("");
            rangoFechasText.setText("");
            capacidadText.setText("");
            PrecioSeleccionado.setText("");

        });

        new Thread(() -> {
            while (pantallaBusquedaAlquilerAbierta == true) {
                // Escuchar continuamente los mensajes del servidor
                if (Socket.message != null) {
                    procesarMensaje();
                    //textViewChat.append("Servidor: " + message + "\n");
                }
            }

        }).start();

    }




    //--------------------------------------------------------------------------------------------------------Metodo para fechas
    // Método para mostrar el DatePickerDialog para la fecha de entrada
    private void mostrarDatePickerDialogEntrada() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                BusquedaAlquiler.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Formatea la fecha de entrada como "dd/MM/yyyy"
                    fechaEntrada = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    mostrarDatePickerDialogSalida(); // Mostrar el DatePicker para la fecha de salida
                },
                year, month, day
        );

        // Personalizar el título del diálogo
        datePickerDialog.setTitle("Seleccione la fecha de entrada");
        datePickerDialog.show();
    }

    // Método para mostrar el DatePickerDialog para la fecha de salida
    private void mostrarDatePickerDialogSalida() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                BusquedaAlquiler.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Formatea la fecha de salida como "dd/MM/yyyy"
                    fechaSalida = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    guardarRangoFechas(); // Guardar el rango de fechas
                },
                year, month, day
        );

        // Personalizar el título del diálogo
        datePickerDialog.setTitle("Seleccione la fecha de salida");
        datePickerDialog.show();
    }

    // Método para guardar el rango de fechas y mostrarlo en el TextView
    private void guardarRangoFechas() {
        if (!fechaEntrada.isEmpty() && !fechaSalida.isEmpty()) {
            // Formatear el rango de fechas
            String rangoFechas = String.format("%s-%s", fechaEntrada, fechaSalida);

            // Obtener el TextView y establecer el texto
            TextView textFecha = findViewById(R.id.textFecha);
            textFecha.setText(rangoFechas);
        }
    }



    private void procesarMensaje(){

        runOnUiThread(() -> {
            if (com.example.miprimeraplicacion.Socket.message != null) {
                String message = com.example.miprimeraplicacion.Socket.message;

                //amenidades tiene que tener un espacio luego de los dos puntos

                String propiedadString = "nombre de la propiedad: Propiedad, Ubicacion:latitud: 9.928271373823975, longitud: -84.09072875976562, reglas de uso: Nada, amenidades:Lavadora y secadora, Calefaccion, Piscina, capacidad maxima: 1, precio: 20000";

                List<Map<String, Object>> propiedades = parsePropiedades(message);
                int cantPropiedades = propiedades.size();

                // Mostrar el resultado en consola
                for (Map<String, Object> propiedad : propiedades) {
                    System.out.println("Propiedad:");
                    Casa casa = new Casa();
                    propiedad.forEach((key, value) -> {
                        System.out.println(key + " -> " + value);
                        if (key.equals("nombre de la propiedad")) {
                            casa.nombrePropiedad = value.toString();
                        }
                        else if (key.equals("reglas")) {
                            if (value instanceof List) {
                                casa.reglas = (List<String>) value;
                            }
                        }
                        else if (key.equals("amenidades")) {
                            if (value instanceof List) {
                                casa.amenidades = (List<String>) value;
                            }
                        }
                        else if (key.equals("capacidad maxima")) {
                            casa.capacidadMaxima = value.toString();
                        }
                        else if (key.equals("precio")) {
                            casa.precio = value.toString();
                        }
                    });
                    System.out.println("----------");
                    ListaCasas.listaCasas.add(casa);
                }

                Socket.message = null;

                Intent intent = new Intent(BusquedaAlquiler.this, ResultadosBusqueda.class);
                startActivity(intent); //se abre la nueva ventana

            }
            else {

            }
        });
    }

    public static List<Map<String, Object>> parsePropiedades(String input) {
        // Dividir cada propiedad por "; "
        String[] propiedades = input.split("; ");
        List<Map<String, Object>> propiedadesList = new ArrayList<>();

        for (String propiedad : propiedades) {
            Map<String, Object> propiedadData = new HashMap<>();
            String[] entries = propiedad.split(", (?=[^,]+: )");

            for (String entry : entries) {
                String[] keyValue = entry.split(": ", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    // Si la clave es "amenidades" o "reglas", dividir en lista
                    if (key.equals("amenidades") || key.equals("reglas")) {
                        propiedadData.put(key, Arrays.asList(value.split(", ")));
                    } else {
                        propiedadData.put(key, value);
                    }
                }
            }
            propiedadesList.add(propiedadData);
        }

        return propiedadesList;
    }

}