package com.example.miprimeraplicacion;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class RegistroActivity extends AppCompatActivity {

    private Button selectDateButton;
    private Button buttonCancel;
    private EditText fechaNacimientoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializa los elementos de la interfaz
        selectDateButton = findViewById(R.id.selectDateButton);
        buttonCancel = findViewById(R.id.cancelButton);
        fechaNacimientoEditText = findViewById(R.id.fechaNacimientoEditText);

        // Configura el listener para el botón de seleccionar fecha
        selectDateButton.setOnClickListener(view -> {
            Log.d("RegistroActivity", "Botón de selección de fecha presionado");
            mostrarDatePickerDialog();
        });

        // Configura el listener para el botón de cancelar
        buttonCancel.setOnClickListener(view -> {
            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    // Método para mostrar el DatePickerDialog
    private void mostrarDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegistroActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Formatea la fecha seleccionada como "dd/MM/yyyy"
                    String fechaSeleccionada = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    fechaNacimientoEditText.setText(fechaSeleccionada);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}
