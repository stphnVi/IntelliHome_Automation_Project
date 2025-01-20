package com.example.miprimeraplicacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

// Recordar que dar los permisos del HW para utilizar los componentes por ejemplo la red
// Esto se hace en el archivo AndroidManifest

/**
 * Creado por Jason Leitón Jiménez para guía del curso de Principio de Modelado
 * Esta clase es la que permite enviar y recibir mensajes desde y hacia el
 * servidor en python
 * Recordar que la gui está en res
 */
public class MainActivity extends AppCompatActivity {

    private EditText editTextMessage;
    //private TextView textViewChat;
    private Socket socket;
    private PrintWriter out;
    private Scanner in;

    private EditText editTextPassword;
    private ImageButton imageButtonShowHidePassword;
    private boolean isPasswordVisible = false; // Flag para manejar el estado de visibilidad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMessage = findViewById(R.id.editTextMessage);
        // textViewChat = findViewById(R.id.textViewChat);
        Button buttonSend = findViewById(R.id.buttonSend);
        Button buttonExit = findViewById(R.id.buttonExit);
        Button buttonForgetPassword = findViewById(R.id.buttonForgot);

        // Configuración para olvidar contraseña
        buttonForgetPassword.setOnClickListener(view -> {
            Log.d("MainActivity", "Botón de olvidar contraseña presionado");
            Intent intent = new Intent(MainActivity.this, PreguntasActivity.class);
            startActivity(intent);
        });

        // Inicializar elementos de la contraseña
        editTextPassword = findViewById(R.id.editTextTextPassword);
        imageButtonShowHidePassword = findViewById(R.id.imageButton3);

        imageButtonShowHidePassword.setOnClickListener(view -> {
            if (isPasswordVisible) {
                // Cambiar a "ocultar" contraseña
                editTextPassword.setInputType(
                        android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imageButtonShowHidePassword.setImageResource(R.drawable.ojo);
            } else {

                editTextPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                imageButtonShowHidePassword.setImageResource(R.drawable.botonojo);
            }
            isPasswordVisible = !isPasswordVisible; // Alternar estado
        });

        // Iniciar el hilo para conectarse al servidor y recibir mensajes
        new Thread(() -> {
            try {
                // Cambiar a la dirección IP de su servidor
                socket = new Socket("192.168.0.106", 1717);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new Scanner(socket.getInputStream());
                new Thread(() -> {
                    while (true) {
                        // Escuchar continuamente los mensajes del servidor
                        if (in.hasNextLine()) {
                            String message = in.nextLine();
                            procesarMensaje(message);
                            //textViewChat.append("Servidor: " + message + "\n");
                        }
                    }

                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();



        buttonSend.setOnClickListener(view -> {
            String userEmail = ((EditText) findViewById(R.id.editTextMessage)).getText().toString();
            String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
            String messageSend = "userEmail: " + userEmail + ", password: " + password;
            sendMessage(messageSend);

            //Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
            //startActivity(intent);



            //textViewChat.append("Yo: " + message + "\n");
            // editTextMessage.setText("");

        });

        buttonExit.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    private void sendMessage(String message) {
        new Thread(() -> {
            try {
                if (out != null) {
                    out.println(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void procesarMensaje(String message){

        runOnUiThread(() -> {
            if ("1".equals(message)) {
                // Abrir nueva ventana si el mensaje es "1"
                Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                startActivity(intent);
            } else if ("0".equals(message)) {
                // Mostrar mensaje de error si el mensaje es "0"

            } else {
                // Manejar otros mensajes si es necesario


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (out != null)
                out.close();
            if (in != null)
                in.close();
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}