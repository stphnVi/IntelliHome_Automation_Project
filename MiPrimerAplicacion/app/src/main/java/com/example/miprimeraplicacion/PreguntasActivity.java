package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.PrintWriter;
import java.util.Scanner;

public class PreguntasActivity extends AppCompatActivity {

    private boolean pantallaPreguntasAbierta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preguntas_seguridad);

        EditText usernameDisplayEditText = findViewById(R.id.usernameDisplayEditText);
        EditText professorNameEditText = findViewById(R.id.professorNameEditText);
        EditText childhoodNicknameEditText = findViewById(R.id.childhoodNicknameEditText);
        EditText favoriteTeamEditText = findViewById(R.id.favoriteTeamEditText);

        Button buttonVerifyAnswers = findViewById(R.id.verifyAnswersButton);
        Button buttonCancel = findViewById(R.id.cancelRecoveryButton);

        //Cada vez que se abre la pantalla de preguntas se indica en el boolean como true
        pantallaPreguntasAbierta = true;

        buttonCancel.setOnClickListener(view -> { // mapeo del boton exit
            pantallaPreguntasAbierta = false;
            Intent intent = new Intent(PreguntasActivity.this, MainActivity.class);
            startActivity(intent);
        });

        buttonVerifyAnswers.setOnClickListener(view -> { // mapeo del boton exit
            //Asignacion de variables
            String username = usernameDisplayEditText.getText().toString();
            String nombreProfe = professorNameEditText.getText().toString();
            String apodo = childhoodNicknameEditText.getText().toString();
            String equipo = favoriteTeamEditText.getText().toString();

            String messageSend = "func: rec" + ", username: " + username + ", nombreProfe: " + nombreProfe + ", apodo: " + apodo + ", equipo: " +  equipo;
            Socket.sendMessage(messageSend);
        });
        new Thread(() -> {
            while (pantallaPreguntasAbierta == true) {
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
                    pantallaPreguntasAbierta = false;
                    Intent intent = new Intent(PreguntasActivity.this, PrincipalActivity.class);
                    startActivity(intent);
                    Socket.message = null;
                } else if ("0".equals(message)) {
                    // Mostrar mensaje de credenciales incorrectas
                    AlertDialog.Builder builder = new AlertDialog.Builder(PreguntasActivity.this);
                    builder.setTitle("Error de autenticación")
                            .setMessage("Credenciales incorrectas. Por favor, intenta de nuevo.")
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
