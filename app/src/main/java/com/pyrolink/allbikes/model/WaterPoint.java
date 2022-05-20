package com.pyrolink.allbikes.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.GeoPoint;
import com.pyrolink.allbikes.Callback;

import com.pyrolink.allbikes.database.FirestoreDb;

public class WaterPoint
{
    private final String _id;
    private String _title;
    private Accessibility _accessibility;
    private String _imgUrl;
    private Bitmap _img;
    private GeoPoint _location;

    public WaterPoint(String id, String title, String accessibility, String imgUrl, GeoPoint location)
    {
        _id = id;
        _title = title;

        _accessibility = Accessibility.get(accessibility);

        _imgUrl = imgUrl;
        _location = location;
    }


    @SuppressWarnings("ConstantConditions")
    public static void readAll(Callback<WaterPoint> callback)
    {
        // KbM02jnBFDB81fLaZYSF =>
        // {accessibility=, pictureUrl=, coordinates=GeoPoint { latitude=-1.0, longitude=1.0 }, title=}
        FirestoreDb.readAll("WaterPoint", (id, wp) ->
        {
            WaterPoint waterPoint = wp.get("certified") == null ?
                    new WaterPoint(id, (String) wp.get("title"), (String) wp.get("accessibility"),
                            (String) wp.get("pictureUrl"), (GeoPoint) wp.get("coordinates")) :
                    new WaterPointCommu(id, (String) wp.get("title"), (String) wp.get("accessibility"),
                            (String) wp.get("pictureUrl"), (GeoPoint) wp.get("coordinates"),
                            (Boolean) wp.get("certified"), (DocumentReference) wp.get("author"));
            callback.call(waterPoint);
        });
    }

    double GetDistance() { throw new UnsupportedOperationException("Not implemented"); }

    // region Getters / Setters

    public String getId()
    {
        return _id;
    }

    public String getTitle()
    {
        return _title;
    }

    public void setTitle(String title)
    {
        this._title = title;
    }

    public Accessibility getAccessibility()
    {
        return _accessibility;
    }

    public void setAccessibility(Accessibility accessibility)
    {
        this._accessibility = accessibility;
    }

    public String getImgUrl()
    {
        return _imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this._imgUrl = imgUrl;
    }

    public GeoPoint getLocation()
    {
        return _location;
    }

    public void setLocation(GeoPoint location)
    {
        this._location = location;
    }

    public Bitmap getImg()
    {
        return _img;
    }

    public void setImg(Bitmap img)
    {
        this._img = img;
    }

    // endregion

    @NonNull
    public String toString()
    {
        return _id + " " + _title + " " + _accessibility + " " + _imgUrl + " " + _location;
    }
}
