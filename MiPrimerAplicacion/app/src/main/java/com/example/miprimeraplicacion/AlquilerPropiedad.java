package com.example.miprimeraplicacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AlquilerPropiedad extends AppCompatActivity {

    private TextView nombrePropiedad;
    private EditText numeroTarjeta;
    private EditText fechaExpiracion;
    private EditText codigoCvv;
    private TextView precio;
    private Button regresar;
    private Button reservar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Esto cierra todas las actividades en la pila
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_alquilar);

        nombrePropiedad = findViewById(R.id.nombrepropiedad);
        numeroTarjeta = findViewById(R.id.NumTarjeta);
        fechaExpiracion = findViewById(R.id.textExpiracion);
        codigoCvv = findViewById(R.id.textCVV);
        precio = findViewById(R.id.textPrecio);
        regresar = findViewById(R.id.regresar);
        reservar = findViewById(R.id.reservar);

        nombrePropiedad.setText(CasaSeleccionada.nombrePropiedad);
        precio.setText((Integer.parseInt(CasaSeleccionada.precio) * Integer.parseInt(CasaSeleccionada.capacidadMaxima)) + " colones por dia");

        regresar.setOnClickListener(view -> { // mapeo del boton para alquilar
            Intent intent = new Intent(AlquilerPropiedad.this, OpcionSeleccionada.class);
            startActivity(intent);
        });

        reservar.setOnClickListener(view -> { // mapeo del boton para alquilar
            if (checkEspaciosObligatorios() && numeroTarjeta.getText().toString().length() == 16
            && fechaExpiracion.getText().toString().length() == 4 && codigoCvv.getText().toString().length() == 3) {
                Toast.makeText(this, "Se ha reservado la casa!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AlquilerPropiedad.this, PrincipalActivity.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Rellenar espacios de manera correcta", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public boolean checkEspaciosObligatorios() {
        if (isEmpty(numeroTarjeta) || isEmpty(fechaExpiracion)
        || isEmpty(codigoCvv)) {

            //Se notifica al usuario las casillas que faltan, se quita si ya esta llena
            if (isEmpty(numeroTarjeta)) {
                numeroTarjeta.setBackgroundResource(R.drawable.edittext_background);
            } else {numeroTarjeta.setBackgroundResource(R.color.white);}
            if (isEmpty(fechaExpiracion)) {
                fechaExpiracion.setBackgroundResource(R.drawable.edittext_background);
            } else {fechaExpiracion.setBackgroundResource(R.color.white);}
            if (isEmpty(codigoCvv)) {
                codigoCvv.setBackgroundResource(R.drawable.edittext_background);
            } else {codigoCvv.setBackgroundResource(R.color.white);}

            Toast.makeText(this, "Rellenar espacios obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean isEmpty(EditText editText) {
        if (editText == null) {
            return true; // Si el EditText es nulo, lo consideramos vacío
        }

        String input = editText.getText().toString().trim();
        return input.isEmpty();
    }

}
