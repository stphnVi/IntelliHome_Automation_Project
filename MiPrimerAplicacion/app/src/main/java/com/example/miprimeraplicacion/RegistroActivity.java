package com.example.miprimeraplicacion;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.net.Uri;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.PrintWriter;
import java.util.Calendar;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import android.provider.MediaStore;

public class RegistroActivity extends AppCompatActivity {

    private Button selectDateButton;
    private Button buttonCancel;
    private EditText fechaNacimientoEditText;

    private EditText nameEditText;

    private EditText lastNameEditText;

    private EditText nicknameEditText;

    private EditText emailEditText;

    private EditText passwordEditText;

    private EditText confirmPasswordEditText;

    private EditText nationalityEditText;

    private EditText descriptionEditText;

    private EditText hobbiesEditText;

    private EditText generalInstructionsEditText;

    private EditText professorNameEditText;

    private EditText childhoodNicknameEditText;

    private EditText favoriteTeamEditText;

    private EditText cardNumberEditText;

    private EditText cardExpiryEditText;

    private EditText cardCvcEditText;
    private Button registerButton;
    private Button selectImageButton;
    private Button uploadImageButton;

    private static final int PICK_IMAGE_REQUEST = 1; // Código de solicitud para la galería
    private Uri imageUri; // URI de la imagen seleccionada

    private ImageView fotoPerfil;

    private PrintWriter out;
    private Scanner in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializa los elementos de la interfaz
        selectDateButton = findViewById(R.id.selectDateButton);
        buttonCancel = findViewById(R.id.cancelButton);
        fechaNacimientoEditText = findViewById(R.id.fechaNacimientoEditText);
        nameEditText = findViewById(R.id.nameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        nicknameEditText = findViewById(R.id.nicknameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        nationalityEditText = findViewById(R.id.nationalityEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        hobbiesEditText = findViewById(R.id.hobbiesEditText);
        generalInstructionsEditText = findViewById(R.id.generalInstructionsEditText);
        professorNameEditText = findViewById(R.id.professorNameEditText);
        childhoodNicknameEditText = findViewById(R.id.childhoodNicknameEditText);
        favoriteTeamEditText = findViewById(R.id.favoriteTeamEditText);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        cardExpiryEditText = findViewById(R.id.cardExpiryEditText);
        cardCvcEditText = findViewById(R.id.cardCvcEditText);
        registerButton = findViewById(R.id.registerButton);
        selectImageButton = findViewById(R.id.selectPhotoButton);
        uploadImageButton = findViewById(R.id.uploadPhotoButton);
        fotoPerfil = findViewById(R.id.fotoPerfil);


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

        registerButton.setOnClickListener(view -> {

            //Asignacion de variables
            String userNombre = nameEditText.getText().toString();
            String userApellido = lastNameEditText.getText().toString();
            String fechaNacimiento = fechaNacimientoEditText.getText().toString();
            String username = nicknameEditText.getText().toString();
            String userEmail = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmacionPassword = confirmPasswordEditText.getText().toString();
            String descripcion = descriptionEditText.getText().toString();
            String instrucciones = generalInstructionsEditText.getText().toString();
            String nombreProfe = professorNameEditText.getText().toString();
            String apodo = childhoodNicknameEditText.getText().toString();
            String equipo = favoriteTeamEditText.getText().toString();
            String numeroTarjeta = cardNumberEditText.getText().toString();
            String expiracionTarjeta = cardExpiryEditText.getText().toString();
            String cvvTarjeta =  cardCvcEditText.getText().toString();

            if (checkProfanidades() && isValidPassword(password) && passwordConfirmationMatch(password, confirmacionPassword)
            && checkEspaciosObligatorios()) {
                // Se envia al usuario a la pagina principal
                // Se envia al usuario a la pagina principal
                String messageSend = "func: reg" + ", userNombre: " + userNombre + ", userApellido: " + userApellido + ", fechaNacimiento: " + fechaNacimiento +
                        ", username: " + username + ", password: " + password + " email: " + userEmail + ", descripcion: " + descripcion + ", instrucciones: "
                        + instrucciones + ", nombreProfe: " + nombreProfe + ", apodo: " + apodo + ", equipo: " +
                        equipo + ", numeroTarjeta: " + numeroTarjeta + ", expiracionTarjeta: " + expiracionTarjeta +
                        ", cvvTarjeta: " + cvvTarjeta;
                sendMessage(messageSend);
            }
            else {
                Toast.makeText(this, "No se ha creado el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        selectImageButton.setOnClickListener(v -> openGallery());

        // Botón para subir la imagen seleccionada
        uploadImageButton.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImage(imageUri);
            } else {
                Toast.makeText(this, "Primero selecciona una imagen", Toast.LENGTH_SHORT).show();
            }
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

    private static final Set<String> PROFANIDADES = new HashSet<>();

    // Lista de profanidades a prohibir, se tienen que añadir en lower case.
    static {
        PROFANIDADES.add("puta");
        PROFANIDADES.add("pene");
        PROFANIDADES.add("vagina");
        PROFANIDADES.add("sexo");
        PROFANIDADES.add("mierda");
        PROFANIDADES.add("idiota");
        PROFANIDADES.add("estupido");
        PROFANIDADES.add("coño");
        PROFANIDADES.add("tetas");
        PROFANIDADES.add("culo");
        PROFANIDADES.add("verga");
        PROFANIDADES.add("pito");
        PROFANIDADES.add("perra");
        PROFANIDADES.add("mamar");
        PROFANIDADES.add("zorra");
        PROFANIDADES.add("maricon");

    }

    /**
     * Checkea si un string contiene profanidades
     *
     * @param input String que se desea ver si tiene profanidedes
     * @return true si contiene profanidades, false en caso contrario
     */
    public boolean contieneProfanidades(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // Normalize input to lowercase
        String normalizedInput = input.toLowerCase();

        // Check if any profanity is a substring of the input
        for (String profanity : PROFANIDADES) {
            if (normalizedInput.contains(profanity)) {
                Toast.makeText(this, "No incluir profanidades", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;
    }

    /**
     * Checkea edit texts para ver si tienen profanidades
     *
     * @return false si cualquier edit text tiene profanidades, true en caso contrario
     */
    public boolean checkProfanidades() {
        if (contieneProfanidades(String.valueOf(nameEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(lastNameEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(nicknameEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(passwordEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(confirmPasswordEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(nationalityEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(descriptionEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(hobbiesEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(generalInstructionsEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(professorNameEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(childhoodNicknameEditText.getText()))) {
            return false;
        }
        else if (contieneProfanidades(String.valueOf(favoriteTeamEditText.getText()))) {
            return false;
        }
        else {
            return true;
        }
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

    public boolean passwordConfirmationMatch(String password1, String password2) {
        if (Objects.equals(password1, password2)) {
            return true;
        }
        else {
            Toast.makeText(this, "Confirmacion de contraseña incorrecta", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Metodo para abrir la galeria
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Recibe el resultado de la galería
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Obtiene la URI de la imagen seleccionada
            fotoPerfil.setImageURI(imageUri); // Muestra la imagen en un ImageView
        }
    }

    // Metodo para subir la imagen
    private void uploadImage(Uri imageUri) {
        // Aquí puedes usar Retrofit, Volley o cualquier otra biblioteca para enviar la imagen al servidor.
        // Por ejemplo, puedes convertir la URI en un archivo para su uso en la solicitud:
        String filePath = imageUri.getPath();
        File file = new File(filePath);

        // Implementar lógica para subir el archivo (ejemplo no incluido aquí).
        Toast.makeText(this, "Subiendo imagen: " + file.getName(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Verifica si un EditText está vacío.
     *
     * @param editText El EditText a verificar.
     * @return true si el campo está vacío, false en caso contrario.
     */
    public static boolean isEmpty(EditText editText) {
        if (editText == null) {
            return true; // Si el EditText es nulo, lo consideramos vacío
        }

        String input = editText.getText().toString().trim();
        return input.isEmpty();
    }

    public boolean checkEspaciosObligatorios() {
        if (isEmpty(nameEditText) || isEmpty(lastNameEditText) || isEmpty(nicknameEditText)
                || isEmpty(emailEditText) || isEmpty(passwordEditText) || isEmpty(nicknameEditText)
                || isEmpty(nationalityEditText) || isEmpty(professorNameEditText)
                || isEmpty(childhoodNicknameEditText) || isEmpty(favoriteTeamEditText)
                || isEmpty(cardNumberEditText) || isEmpty(cardExpiryEditText)
                || isEmpty(cardCvcEditText)) {
            Toast.makeText(this, "Rellenar espacios obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Muestra una notificación de alerta en caso de error.
     * La notificacion es intrusiva, es preferible que el
     * mensaje de error sea rapido y conciso.
     *
     * @param title   El título de la alerta.
     * @param message El mensaje de error que se mostrará.
     */
    private void showErrorAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert); // Icono de alerta estándar
        builder.setPositiveButton("Aceptar", (dialog, which) -> {
            // Acción al presionar el botón "Aceptar"
            dialog.dismiss(); // Cierra la alerta
        });

        // Muestra la alerta
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
