package com.example.miprimeraplicacion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Casa {
    String precio;
    List<String> reglas;
    String capacidadMaxima;
    List<String> amenidades;
    String nombrePropiedad;
}

class ListaCasas {
    public static List<Casa> listaCasas = new ArrayList<>();
}

class CasaSeleccionada {
    static String precio;
    static List<String> reglas;
    static String capacidadMaxima;
    static List<String> amenidades;
    static String nombrePropiedad;

    public static String convertirListaEnString(List<String> lista) {
        if (lista == null || lista.isEmpty()) {
            return ""; // Retornar un String vacío si la lista es nula o vacía
        }
        return String.join(", ", lista); // Unir los elementos con una coma
    }
}

