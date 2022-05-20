package com.pyrolink.allbikes;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    public static final LatLng annecy = new LatLng(45.899919, 6.128141);

    private ActivityMainBinding _binding;
    private MapFragment mapView;
    private GoogleMap map;

    private WaterPoint _selected;

    private Map<Marker, WaterPoint> _markers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        _binding.filtersHidden.setOnClickListener(view -> showFilters(true));
        _binding.filters.setOnTitleClick(view -> showFilters(false));
        _binding.filters.setOnNote(i -> Toast.makeText(this, String.valueOf(i), Toast.LENGTH_SHORT).show());

        _markers = new HashMap<>();

        mapView = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapView.getMapAsync(googleMap ->
        {
            googleMap.setOnMarkerClickListener(this::onMarker);
            googleMap.setOnInfoWindowCloseListener(this::onInfoClose);

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

    private void showFilters(boolean visibility)
    {
        _binding.filtersHidden.setVisibility(visibility ? View.INVISIBLE : View.VISIBLE);
        _binding.filters.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean _reshow;

    @SuppressWarnings("ConstantConditions")
    private boolean onMarker(@NonNull Marker marker)
    {
        _binding.data.setVisibility(View.VISIBLE);

        WaterPoint wp = _markers.get(marker);
        _selected = wp;

        if (wp instanceof WaterPointCommu)
        {
            WaterPointCommu wpc = (WaterPointCommu) wp;

            User author = wpc.getAuthor();
            if (author == null)
                wpc.loadAuthor(user ->
                {
                    marker.setSnippet(user.getFirstName());
                    if (marker.isInfoWindowShown())
                        reshowInfo(marker);
                });
            else if (marker.getSnippet() == null)
            {
                marker.setSnippet(author.getFirstName());
            }

            _binding.stars.setVisibility(View.VISIBLE);

            Integer note = wpc.getNote();
            if (note == null)
                wpc.loadNotes(ignored ->
                {
                    if (wp == _selected)
                        _binding.stars.setStars(wpc.getNote());
                });
            else
            {
                _binding.stars.setStars(note);
            }
        }
        else
        {
            _binding.stars.setVisibility(View.INVISIBLE);
        }

        _binding.title.setText(wp.getTitle());
        _binding.accessibility.setText(String.valueOf(wp.getAccessibility()));

        if (wp.getImg() == null)
            Picasso.get()
                    .load("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Instagram_icon.png/2048px-Instagram_icon.png")
                    .into(setPcImg(wp));
        else
            _binding.img.setImageBitmap(wp.getImg());

        return false;
    }


    private void reshowInfo(Marker marker)
    {
        _reshow = true;

        marker.hideInfoWindow();
        marker.showInfoWindow();

        _reshow = false;
    }

    private void onInfoClose(Marker marker)
    {
        if (_reshow)
            return;

        _selected = null;
        _binding.data.setVisibility(View.INVISIBLE);
    }

    private Target setPcImg(WaterPoint wp)
    {
        return new Target()
        {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
            {
                wp.setImg(bitmap);

                if (wp == _selected)
                    _binding.img.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { }
        };
    }
}