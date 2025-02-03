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
