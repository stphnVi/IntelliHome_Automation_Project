package com.example.miprimeraplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class RecuperacionContrasena extends AppCompatActivity {
    private EditText editTextPassword;
    private EditText editTextconfirmPassword;

    private boolean pantallaRecuperacioncontrasenaabierta;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Esto cierra todas las actividades en la pila
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recuperacion_contrasena);

        editTextPassword = findViewById(R.id.editTextTextPassword2);
        editTextconfirmPassword = findViewById(R.id.editTextTextPassword3);


        Button buttonconfirmar = findViewById(R.id.botonconfirmar);
        Button buttonCancel = findViewById(R.id.botoncancelar);

        //Cada vez que se abre la pantalla de preguntas se indica en el boolean como true
        pantallaRecuperacioncontrasenaabierta = true;

        buttonCancel.setOnClickListener(view -> { // mapeo del boton exit
            pantallaRecuperacioncontrasenaabierta = false;
            finishAffinity(); // Esto cierra todas las actividades en la pila
        });

        buttonconfirmar.setOnClickListener(view -> { // mapeo del boton exit
            //Asignacion de variables
            String password = editTextPassword.getText().toString();
            String passwordconfirm = editTextconfirmPassword.getText().toString();
            if(isValidPassword(password) && passwordConfirmationMatch(password, passwordconfirm)&& checkEspaciosObligatorios()) {
                String messageSend = "func: cc" + ", password: " + password + ", passwordconfirm: " + passwordconfirm;
                Socket.sendMessage(messageSend);
                pantallaRecuperacioncontrasenaabierta = false;
                Intent intent = new Intent(RecuperacionContrasena.this, MainActivity.class);
                startActivity(intent);

            }else {
                Toast.makeText(this, "No se ha cambiado la contraseña", Toast.LENGTH_SHORT).show();
            }

        });

        new Thread(() -> {
            while (pantallaRecuperacioncontrasenaabierta == true) {
                // Escuchar continuamente los mensajes del servidor
                if (com.example.miprimeraplicacion.Socket.message != null) {
                    procesarMensaje();
                }
            }

        }).start();


    }

    public boolean checkEspaciosObligatorios() {
        if (isEmpty(editTextPassword) || isEmpty(editTextconfirmPassword) ) {

            //Se notifica al usuario las casillas que faltan, se quita si ya esta llena
            if (isEmpty(editTextPassword)) {
                editTextPassword.setBackgroundResource(R.drawable.edittext_background);
            } else {editTextPassword.setBackgroundResource(R.color.white);}
            if (isEmpty(editTextconfirmPassword)) {
                editTextconfirmPassword.setBackgroundResource(R.drawable.edittext_background);
            } else {editTextconfirmPassword.setBackgroundResource(R.color.white);}

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

    /**
     * Verifica si una contraseña es válida según los siguientes criterios:
     * - Mínimo 8 caracteres.
     * - Contiene al menos dos tipos diferentes de caracteres:
     *   (letras, números, caracteres especiales).
     *
     * @param password La contraseña a verificar.
     * @return true si la contraseña es válida, false en caso contrario.
     */


    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener minimo 8 caracteres", Toast.LENGTH_SHORT).show();
            return false; // Menos de 8 caracteres
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        // Recorre cada carácter en la contraseña
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }

            // Si tiene al menos dos tipos de caracteres, es válida
            if ((hasLetter && hasDigit) || (hasLetter && hasSpecial) || (hasDigit && hasSpecial)) {
                return true;
            }
        }

        Toast.makeText(this, "Contraseña invalida", Toast.LENGTH_SHORT).show();
        return false; // No cumple con los tipos mínimos de caracteres
    }


    public boolean passwordConfirmationMatch(String password, String passwordconfirm) {
        if (Objects.equals(password, passwordconfirm)) {
            return true;
        }
        else {
            Toast.makeText(this, "Confirmacion de contraseña incorrecta", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Se procesan los mensajes del servidor
     * SIEMPRE al final de cada if poner "Socket.message=null"
     */
    private void procesarMensaje(){

        runOnUiThread(() -> {
            if (com.example.miprimeraplicacion.Socket.message != null) {
                String message = com.example.miprimeraplicacion.Socket.message;
                if ("respuestas correctas".equals(message)) {
                    // Abrir nueva ventana si el mensaje es "1"
                    pantallaRecuperacioncontrasenaabierta = false;
                    Intent intent = new Intent(RecuperacionContrasena.this, MainActivity.class);
                    startActivity(intent);
                    Socket.message = null;

                } else {
                    // Mostrar mensaje de credenciales incorrectas
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecuperacionContrasena.this);
                    builder.setTitle("Contraseñas incorrecta")
                            .setMessage("Las contraseñas no coinsiden.")
                            .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                            .create()
                            .show();
                    // Manejar otros mensajes si es necesario
                    Socket.message = null;


                }
            }
            else {

            }
        });

    }


}
