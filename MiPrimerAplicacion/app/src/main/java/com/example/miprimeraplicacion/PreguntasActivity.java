package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.PrintWriter;
import java.util.Scanner;

public class PreguntasActivity extends AppCompatActivity {

    public static PrintWriter out;

    public static Scanner in;

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

        buttonCancel.setOnClickListener(view -> { // mapeo del boton exit
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
            sendMessage(messageSend);
        });

    }

    private void sendMessage(String message) {
        new Thread(() -> {
            try {
                if (MainActivity.out != null) {
                    MainActivity.out.println(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
