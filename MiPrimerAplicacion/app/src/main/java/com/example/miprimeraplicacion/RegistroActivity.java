package com.example.miprimeraplicacion;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Calendar;

import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import android.provider.MediaStore;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

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

    private FloatingActionButton fotoPerfilButton;
    private ImageView fotoPerfilImageView;
    private Button terminosButton;

    private static final int PICK_IMAGE_REQUEST = 1; // Código de solicitud para la galería
    private Uri imageUri; // URI de la imagen seleccionada
    private boolean pantallaRegistroAbierta;
    private static final int PICK_IMAGE = 1;
    private static final int TAKE_PHOTO = 2;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    private String nacionalidad;
    private String profileImagePath;
    private ImageView logoImageView;
    private boolean terminosCondicionesAceptados = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // Esto cierra todas las actividades en la pila
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getColor(R.color.Azul)));

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
        fotoPerfilButton = findViewById(R.id.botonFotoPerfil);
        fotoPerfilImageView = findViewById(R.id.fotoPerfilImageView);
        logoImageView = findViewById(R.id.logoImageView);
        terminosButton = findViewById(R.id.terminosButton);

        // Variables para el menu drop down
        String[] item = {"Argentina", "Bolivia", "Brasil", "Chile", "Colombia", "Costa Rica",
                "Cuba", "Ecuador", "El Salvador", "Granada", "Guatemala", "Guyana", "Haití",
                "Honduras", "Jamaica", "México", "Nicaragua", "Panamá", "Paraguay", "Perú",
                "República Dominicana", "Surinam", "Trinidad y Tobago", "Uruguay", "Venezuela"};
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener((adapterView, view, i, l) -> {
            nacionalidad = adapterView.getItemAtPosition(i).toString();
        });

        // Configura el listener para el botón de seleccionar fecha
        selectDateButton.setOnClickListener(view -> {
            Log.d("RegistroActivity", "Botón de selección de fecha presionado");
            mostrarDatePickerDialog();
        });

        // Configura el listener para el botón de cancelar
        buttonCancel.setOnClickListener(view -> {
            pantallaRegistroAbierta = false;
            //Esto esta crasheando la aplicacion, preguntar para que es y arreglarlo
//            finishAffinity(); // Esto cierra todas las actividades en la pila
            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(intent); //se abre la nueva ventana
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

            if (checkEspaciosObligatorios() && (checkProfanidades() && isValidPassword(password)
                    && passwordConfirmationMatch(password, confirmacionPassword))) {
                // Se envia al usuario a la pagina principal
                // Se envia al usuario a la pagina principal
                String messageSend = "func: reg" + ", userNombre: " + userNombre + ", userApellido: " + userApellido + ", fechaNacimiento: " + fechaNacimiento +
                        ", username: " + username + ", password: " + password + " email: " + userEmail + ", descripcion: " + descripcion + ", instrucciones: "
                        + instrucciones + ", nombreProfe: " + nombreProfe + ", apodo: " + apodo + ", equipo: " +
                        equipo + ", numeroTarjeta: " + numeroTarjeta + ", expiracionTarjeta: " + expiracionTarjeta +
                        ", cvvTarjeta: " + cvvTarjeta;
                Socket.sendMessage(messageSend);
            }
            else {
                Toast.makeText(this, "No se ha creado el usuario", Toast.LENGTH_SHORT).show();
            }
        });

        fotoPerfilButton.setOnClickListener(view -> {
            ImagePicker.with(RegistroActivity.this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });

        //Se escuchan los mensajes unicamente cuando la pantalla de registro esta abierta
        new Thread(() -> {
            while (pantallaRegistroAbierta == true) {
                // Escuchar continuamente los mensajes del servidor
                if (com.example.miprimeraplicacion.Socket.message != null) {
                    procesarMensaje();
                    //textViewChat.append("Servidor: " + message + "\n");
                }
            }

        }).start();

        terminosButton.setOnClickListener(view -> {
            mostrarPopupTerminos();
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
                || isEmpty(professorNameEditText) || nacionalidad == null
                || isEmpty(childhoodNicknameEditText) || isEmpty(favoriteTeamEditText)
                || isEmpty(cardNumberEditText) || isEmpty(cardExpiryEditText)
                || isEmpty(cardCvcEditText)) {

            //Se notifica al usuario las casillas que faltan, se quita si ya esta llena
            if (isEmpty(nameEditText)) {
                nameEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {nameEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(lastNameEditText)) {
                lastNameEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {lastNameEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(nicknameEditText)) {
                nicknameEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {nicknameEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(emailEditText)) {
                emailEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {emailEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(passwordEditText)) {
                passwordEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {passwordEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(nicknameEditText)) {
                nicknameEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {nicknameEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(professorNameEditText)) {
                professorNameEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {professorNameEditText.setBackgroundResource(R.color.white);}
            if (nacionalidad == null) {
                autoCompleteTextView.setBackgroundResource(R.drawable.edittext_background);
            } else {autoCompleteTextView.setBackgroundResource(R.color.white);}
            if (isEmpty(childhoodNicknameEditText)) {
                childhoodNicknameEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {childhoodNicknameEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(favoriteTeamEditText)) {
                favoriteTeamEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {favoriteTeamEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(cardNumberEditText)) {
                cardNumberEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {cardNumberEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(cardExpiryEditText)) {
                cardExpiryEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {cardExpiryEditText.setBackgroundResource(R.color.white);}
            if (isEmpty(cardCvcEditText)) {
                cardCvcEditText.setBackgroundResource(R.drawable.edittext_background);
            } else {cardCvcEditText.setBackgroundResource(R.color.white);}
            if (terminosCondicionesAceptados == false) {
                Toast.makeText(this, "Aceptar terminos y condiciones", Toast.LENGTH_SHORT).show();
            }


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

    /**
     * Se procesan los mensajes del servidor
     * SIEMPRE al final de cada if poner "Socket.message=null"
     */
    private void procesarMensaje(){

        // Se procesa el mensaje recibido del servidor.
        // "1" equivale a true, "0" equivale a false
        runOnUiThread(() -> {
            if (com.example.miprimeraplicacion.Socket.message != null) {
                String message = com.example.miprimeraplicacion.Socket.message;
                if ("1".equals(message)) {
                    // Abrir nueva ventana si el mensaje es "1"
                    pantallaRegistroAbierta = false;
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Usuario registrado")
                            .setMessage("El usuario ha sido registrado exitosamente")
                            .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                            .create()
                            .show();
                    //Intent intent = new Intent(RegistroActivity.this, PrincipalActivity.class);
                    //startActivity(intent); //se abre la nueva ventana
                    Socket.message = null; //se hace el mensaje de entrada null para recibir el siguiente mensaje
                } else if ("0".equals(message)) {
                    // Mostrar mensaje de credenciales incorrectas
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                    builder.setTitle("Error al registrarse")
                            .setMessage("Por favor llenar los datos correctamente")
                            .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                            .create()
                            .show();
                    Socket.message = null; //se hace el mensaje de entrada null para recibir el siguiente mensaje

                } else {
                    // Manejar otros mensajes si es necesario
                    Socket.message = null; //se hace el mensaje de entrada null para recibir el siguiente mensaje

                }
            }
            else {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        fotoPerfilImageView.setImageURI(uri);
        profileImagePath = saveImageToImagesDir(RegistroActivity.this, uri, "profile_pic");
    }

    public static String saveImageToImagesDir(Context context, Uri imageUri, String fileName) {
        if (imageUri == null || context == null) return null;

        File imagesDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Images");
        if (!imagesDir.exists() && !imagesDir.mkdirs()) {
            return null; // Failed to create the directory
        }

        File imageFile = new File(imagesDir, fileName);
        try (InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
             FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            return imageFile.getAbsolutePath(); // Return the file path
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Failed to save the image
        }
    }

    public void setImageFromPath(ImageView imageView, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            // Convert the absolute path to a Uri
            Uri imageUri = Uri.parse("file://" + imagePath);

            // Set the image URI to the ImageView
            imageView.setImageURI(imageUri);
        } else {
            // Handle the case where the path is null or empty
            imageView.setImageResource(android.R.color.transparent); // Set a placeholder or clear the image
        }
    }

    public static String getImagePathFromName(Context context, String fileName) {
        if (context == null || fileName == null || fileName.isEmpty()) return null;

        // Directory where images are saved
        File imagesDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Images");

        if (imagesDir.exists()) {
            // Search for the file by name in the directory
            File imageFile = new File(imagesDir, fileName);
            if (imageFile.exists()) {
                return imageFile.getAbsolutePath(); // Return the file path if it exists
            }
        }

        return null; // Return null if the file is not found
    }

    private void mostrarPopupTerminos() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View popupView = inflater.inflate(R.layout.popup_terminos, null);

        TextView tvTerminosContenido = popupView.findViewById(R.id.tvTerminosContenido);

        String terminosTexto = "Términos y Condiciones de Uso de Intellihome\n\n"
                + "Fecha de entrada en vigor: [Fecha]\n\n"
                + "Bienvenido a Intellihome. Al acceder y utilizar nuestra aplicación, usted acepta "
                + "cumplir con los siguientes términos y condiciones. Si no está de acuerdo con estos "
                + "términos, le solicitamos que no utilice nuestra aplicación.\n\n"
                + "1. Definiciones\n"
                + "• Intellihome: Se refiere a la aplicación móvil y/o plataforma web que facilita el "
                + "alquiler de propiedades.\n"
                + "• Usuario: Cualquier persona que se registre y utilice Intellihome.\n"
                + "• Propiedad: Cualquier inmueble disponible para alquiler a través de Intellihome.\n\n"
                + "2. Aceptación de Términos\n"
                + "Al registrarse y utilizar Intellihome, usted acepta estos términos y condiciones en su "
                + "totalidad. Nos reservamos el derecho a modificar estos términos en cualquier momento, "
                + "por lo que recomendamos revisarlos periódicamente.\n\n"
                + "3. Registro de Usuario\n"
                + "Para utilizar algunas funcionalidades de la aplicación, es necesario crear una cuenta. "
                + "El Usuario se compromete a proporcionar información veraz y actualizada. Es "
                + "responsabilidad del Usuario mantener la confidencialidad de sus credenciales de acceso.\n\n"
                + "4. Uso de la Aplicación\n"
                + "• Intellihome permite a los Usuarios buscar y alquilar propiedades.\n"
                + "• El Usuario se compromete a utilizar la aplicación solo para fines lícitos y de "
                + "acuerdo con las leyes aplicables.\n\n"
                + "5. Propiedades\n"
                + "• Las propiedades listadas en Intellihome son ofrecidas por propietarios y "
                + "agentes inmobiliarios.\n"
                + "• Intellihome no es responsable de la veracidad, disponibilidad o estado de las "
                + "propiedades.\n\n"
                + "6. Comisiones y Pagos\n"
                + "• Intellihome puede cobrar comisiones por los servicios ofrecidos. Las tarifas "
                + "serán comunicadas claramente antes de confirmar cualquier transacción.\n"
                + "• El Usuario acepta realizar los pagos a través de los métodos habilitados en la "
                + "aplicación.\n\n"
                + "7. Política de Cancelación\n"
                + "Las políticas de cancelación varían según cada propiedad y serán indicadas en el "
                + "anuncio correspondiente. Es responsabilidad del Usuario revisar estas políticas antes "
                + "de realizar una reserva.\n\n"
                + "8. Limitación de Responsabilidad\n"
                + "Intellihome no será responsable de daños directos, indirectos, incidentales o "
                + "consecuentes que surjan del uso de la aplicación o de la imposibilidad de uso.\n\n"
                + "9. Propiedad Intelectual\n"
                + "Todo el contenido de Intellihome, incluyendo textos, gráficos y logotipos, está "
                + "protegido por derechos de propiedad intelectual. Queda prohibida la reproducción o "
                + "distribución sin autorización.\n\n"
                + "10. Modificaciones a los Términos\n"
                + "Nos reservamos el derecho de modificar estos términos en cualquier momento. Las "
                + "modificaciones entrarán en vigor al ser publicadas en la aplicación. El uso continuado "
                + "de Intellihome tras la publicación de cambios implica la aceptación de los mismos.\n\n"
                + "11. Legislación Aplicable\n"
                + "Estos términos se rigen por las leyes de [país/estado]. Cualquier disputa relacionada "
                + "con el uso de Intellihome será resuelta en los tribunales de [ciudad/estado].\n\n"
                + "12. Contacto\n"
                + "Para cualquier pregunta o inquietud sobre estos términos, por favor contáctenos en "
                + "[correo electrónico].";

        tvTerminosContenido.setText(terminosTexto);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);
        builder.setCancelable(false);

        Button btnAceptar = popupView.findViewById(R.id.btnAceptar);
        Button btnCancelar = popupView.findViewById(R.id.btnCancelar);
        AlertDialog dialog = builder.create();

        btnAceptar.setOnClickListener(v -> {
            terminosCondicionesAceptados = true;
            dialog.dismiss();
            // Acciones al aceptar los términos
        });

        btnCancelar.setOnClickListener(v -> {
            dialog.dismiss();
            // Finaliza la actividad actual
        });

        dialog.show();
    }
}
