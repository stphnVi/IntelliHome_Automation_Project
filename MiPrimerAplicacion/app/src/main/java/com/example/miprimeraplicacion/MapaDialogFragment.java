package com.example.miprimeraplicacion;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapaDialogFragment extends DialogFragment {

    private MapView mapView; // Referencia al MapView
    private GeoPoint ubicacionSeleccionada; // Variable para almacenar la ubicación seleccionada

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Configurar OSMDroid
        Context context = getActivity().getApplicationContext();
        Configuration.getInstance().setUserAgentValue(context.getPackageName());

        // Inflar el diseño del fragmento
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_mapa_dialog, null);
        builder.setView(rootView);

        // Inicializar el MapView
        mapView = rootView.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Configurar el mapa para mostrar una ubicación inicial (San José Costa Rica)
        mapView.getController().setZoom(10);
        GeoPoint startPoint = new GeoPoint(9.9281, -84.0907);
        mapView.getController().setCenter(startPoint);

        // Botón para confirmar la ubicación seleccionada
        Button btnConfirmar = rootView.findViewById(R.id.btnConfirmarUbicacion);
        btnConfirmar.setOnClickListener(v -> {
            // Obtener el centro del mapa como la ubicación seleccionada
            ubicacionSeleccionada = (GeoPoint) mapView.getMapCenter();

            // Mostrar las coordenadas seleccionadas
            String ubicacionTexto = "Lat: " + ubicacionSeleccionada.getLatitude() +
                    ", Lng: " + ubicacionSeleccionada.getLongitude();
            Toast.makeText(getActivity(), "Ubicación confirmada: " + ubicacionTexto, Toast.LENGTH_SHORT).show();

            // Crear un Intent para pasar las coordenadas a RegistroCasa
            Intent intent = new Intent(getActivity(), RegistroCasa.class);
            intent.putExtra("latitud", ubicacionSeleccionada.getLatitude());
            intent.putExtra("longitud", ubicacionSeleccionada.getLongitude());
            startActivity(intent);

            dismiss(); // Cerrar el diálogo después de confirmar
        });

        return builder.create();
    }
}

