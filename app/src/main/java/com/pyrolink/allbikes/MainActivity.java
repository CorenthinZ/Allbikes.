package com.pyrolink.allbikes;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pyrolink.allbikes.databinding.ActivityMainBinding;
import com.pyrolink.allbikes.model.User;
import com.pyrolink.allbikes.model.WaterPoint;
import com.pyrolink.allbikes.model.WaterPointCommu;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    public static final LatLng annecy = new LatLng(45.899919, 6.128141);

    private ActivityMainBinding _binding;
    private MapFragment mapView;
    private GoogleMap map;

    private Map<Marker, WaterPoint> _markers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        _markers = new HashMap<>();

        mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapView.getMapAsync(googleMap ->
        {
            googleMap.setOnMarkerClickListener(this::onMarker);

            map = googleMap;
            map.getUiSettings().setMyLocationButtonEnabled(false);

            WaterPoint.readAll(wp ->
            {
                MarkerOptions mo = new MarkerOptions().position(
                                new LatLng(wp.getLocation().getLatitude(), wp.getLocation().getLongitude()))
                        .title(wp.getTitle());

                _markers.put(map.addMarker(mo), wp);
            });

            CameraPosition.Builder cam = new CameraPosition.Builder().target(annecy).zoom(14.0f);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cam.build());
            map.moveCamera(cameraUpdate);
        });
    }

    boolean onMarker(@NonNull Marker marker)
    {
        WaterPoint wp = _markers.get(marker);
        if (wp instanceof WaterPointCommu)
        {
            WaterPointCommu wpc = (WaterPointCommu) wp;

            User author = wpc.getAuthor();
            if (author == null)
                wpc.loadAuthor(this, user ->
                {
                    marker.setSnippet(user.getFirstName());
                    if (marker.isInfoWindowShown())
                    {
                        marker.hideInfoWindow();
                        marker.showInfoWindow();
                    }
                });
            else if (marker.getSnippet() == null)
            {
                marker.setSnippet(author.getFirstName());
            }

            _binding.stars.setVisibility(View.VISIBLE);

            for (int i = 0; i < 5; i++)
                ((ImageView) _binding.stars.getChildAt(i)).setImageResource(
                        i <= wpc.getNote() ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        }
        else
        {
            _binding.stars.setVisibility(View.INVISIBLE);
        }

        _binding.title.setText(wp.getTitle());
        _binding.accessibility.setText(String.valueOf(wp.getAccessibility()));

        return false;
    }
}