package com.example.miprimeraplicacion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapaDialogFragment extends DialogFragment {

    private MapView mapView;
    private TextView textoUbicaciones;

    // Método para pasar el TextView desde la actividad
    public void setTextoUbicaciones(TextView textoUbicaciones) {
        this.textoUbicaciones = textoUbicaciones;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View rootView = inflater.inflate(R.layout.fragment_mapa_dialog, container, false);

        // Configuración del MapView
        mapView = rootView.findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        // Establecer la ubicación inicial (puedes cambiarlo a tu ubicación deseada)
        GeoPoint startPoint = new GeoPoint(9.748917, -83.753428);
        mapView.getController().setZoom(10);
        mapView.getController().setCenter(startPoint);

        // Listener para tocar en el mapa
        mapView.setOnClickListener(e -> {
            // Obtener las coordenadas del toque
            GeoPoint selectedPoint = (GeoPoint) mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());

            // Mostrar marcador en el mapa
            Marker marker = new Marker(mapView);
            marker.setPosition(selectedPoint);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle("Ubicación seleccionada");

            // Limpiar y agregar marcador
            mapView.getOverlays().clear();
            mapView.getOverlays().add(marker);

            // Actualizar el TextView con las coordenadas
            if (textoUbicaciones != null) {
                textoUbicaciones.setText("Ubicación seleccionada: Lat: " + selectedPoint.getLatitude() + ", Lon: " + selectedPoint.getLongitude());
            }

            // Cerrar el diálogo después de seleccionar la ubicación
            dismiss();
        });

        return rootView;
    }
}
