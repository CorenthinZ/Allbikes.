package com.pyrolink.allbikes.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Predicate;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pyrolink.allbikes.R;
import com.pyrolink.allbikes.databinding.ActivityMapBinding;
import com.pyrolink.allbikes.interfaces.Callback;
import com.pyrolink.allbikes.interfaces.Callback2;
import com.pyrolink.allbikes.model.Accessibility;
import com.pyrolink.allbikes.model.User;
import com.pyrolink.allbikes.model.WaterPoint;
import com.pyrolink.allbikes.model.WaterPointCommu;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity
{
    private static final int LOCATION_PERMISSION = 2001;

    public static final LatLng annecy = new LatLng(45.899919, 6.128141);

    private ActivityMapBinding _binding;
    private MapFragment mapView;
    private GoogleMap map;

    private WaterPoint _selected;

    private Map<Marker, WaterPoint> _markers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestPermissions(
                new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION },
                LOCATION_PERMISSION);

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_map);

        _binding.filtersHidden.setOnClickListener(view -> showFilters());
        _binding.filters.setOnTitleClick(view -> hideFilters());
        _binding.filters.setOnDistance(this::onDistance);
        _binding.filters.setOnNote(this::onStarFiltered);
        _binding.filters.setOnAccessibility(this::onAccessibilityFiltered);
        _binding.search.setOnSearch(this::onSearch);

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

            onLocation(location ->
            {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraPosition.Builder cam = new CameraPosition.Builder().target(latLng);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cam.build());
                map.moveCamera(cameraUpdate);
            });
        });
    }

    private void hideFilters() { setFiltersVisibility(false); }

    private void showFilters()
    {
        for (Marker marker : _markers.keySet())
            if (marker.isInfoWindowShown())
            {
                marker.hideInfoWindow();
                break;
            }

        setFiltersVisibility(true);
    }

    private void setFiltersVisibility(boolean visibility)
    {
        _binding.filtersHidden.setVisibility(visibility ? View.INVISIBLE : View.VISIBLE);
        _binding.filters.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean _reshow;

    @SuppressWarnings("ConstantConditions")
    private boolean onMarker(@NonNull Marker marker)
    {
        _binding.data.setVisibility(View.VISIBLE);

        if (_binding.filters.getVisibility() == View.VISIBLE)
            hideFilters();

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

    // region Filters

    private void forEachMarkers(Callback2<Marker, WaterPoint> callback) { forEachMarkers(null, callback); }

    private void forEachMarkers(Predicate<WaterPoint> condition, Callback2<Marker, WaterPoint> callback)
    {
        for (Map.Entry<Marker, WaterPoint> marker : _markers.entrySet())
            if (condition == null || condition.test(marker.getValue()))
                callback.call(marker.getKey(), marker.getValue());
    }

    private void onSearch(final String search)
    {
        forEachMarkers((marker, waterPoint) -> marker.setVisible(
                waterPoint.getTitle().toLowerCase().contains(search.toLowerCase())));
    }

    private void onLocation(Callback<Location> onLocation)
    {
        LocationManager locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(MapActivity.this, "Autorisation de localisation requise", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] providers = new String[]{ LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER };
        boolean ok = false;

        for (String provider : providers)
            if (locMgr.isProviderEnabled(provider))
            {
                ok = true;
                break;
            }

        if (!ok)
        {
            Toast.makeText(this, "La localisaton est désactivée !", Toast.LENGTH_SHORT).show();
            return;
        }

        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_FINE);

        locMgr.requestSingleUpdate(c, location ->
        {

        }, Looper.getMainLooper());
    }

    private void onDistance(int distance)
    {
        onLocation(location ->
        {
            forEachMarkers((marker, waterPoint) ->
            {
                Location wpLoc = new Location((String) null);
                wpLoc.setLatitude(waterPoint.getLocation().getLatitude());
                wpLoc.setLongitude(waterPoint.getLocation().getLongitude());

                float delta = wpLoc.distanceTo(location);

                marker.setVisible(delta <= distance * 1_000);
            });
            String tmp = location.getLatitude() + ", " + location.getLongitude();
            Toast.makeText(MapActivity.this, tmp, Toast.LENGTH_SHORT).show();
        });
    }

    private void onAccessibilityFiltered(Accessibility accessibility, boolean checked)
    {
        forEachMarkers(waterPoint -> waterPoint.getAccessibility() == accessibility,
                (marker, waterPoint) -> marker.setVisible(checked));
    }

    private void onStarFiltered(int i)
    {
        forEachMarkers(waterPoint -> waterPoint instanceof WaterPointCommu, (marker, waterPoint) ->
        {
            WaterPointCommu waterPointCommu = (WaterPointCommu) waterPoint;

            Integer note = waterPointCommu.getNote();
            if (note == null)
                waterPointCommu.loadNotes(notes -> marker.setVisible(waterPointCommu.getNote() <= i));
            else
                marker.setVisible(waterPointCommu.getNote() <= i);
        });
    }

    // endregion
}