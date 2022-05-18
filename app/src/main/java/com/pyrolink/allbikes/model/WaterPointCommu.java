package com.pyrolink.allbikes.model;

import android.content.Context;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.pyrolink.allbikes.Callback;

import java.util.ArrayList;
import java.util.Map;

public class WaterPointCommu extends WaterPoint
{
    private boolean _certified;
    private final DocumentReference _authorRef;
    private User _author;

    public WaterPointCommu(String id, String title, String accessibility, String imgUrl, GeoPoint location,
                           boolean certified, DocumentReference author)
    {
        super(id, title, accessibility, imgUrl, location);

        _certified = certified;
        _authorRef = author;
    }


    public boolean isCertified()
    {
        return _certified;
    }

    public void setCertified(boolean certified)
    {
        this._certified = certified;
    }

    public User getAuthor()
    {
        return _author;
    }

    @SuppressWarnings({ "unchecked", "ConstantConditions" })
    public void loadAuthor(Context context, Callback<User> callback)
    {
        // if ()
        // _author = User.USERS.getOrDefault(_authorRef.getId(), null);

        if (_author != null)
            return;

        // {firstName=Corenthin, lastName=Zozor, password=xzetrchg,
        // birthdate=Timestamp(seconds=1034978400, nanoseconds=0),
        // city=Revel, leisure=[Info], email=38cz74@gmail.com}

        _authorRef.get().addOnCompleteListener(task ->
        {
            if (!task.isSuccessful())
            {
                Log.w("", "Error getting documents !", task.getException());
                return;
            }

            DocumentSnapshot result = task.getResult();
            Map<String, Object> data = result.getData();
            setAuthor(new User(result.getId(), (String) data.get("firstName"), (String) data.get("lastName"),
                    (String) data.get("email"), (String) data.get("city"), ((Timestamp) data.get("birthdate")).toDate(),
                    (ArrayList<String>) data.get("leisure")));

            callback.call(_author);
        });
    }

    private void setAuthor(User author)
    {
        _author = author;
    }
}
