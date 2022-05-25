package com.pyrolink.allbikes.model;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.pyrolink.allbikes.interfaces.Callback;
import com.pyrolink.allbikes.database.FirestoreDb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WaterPointCommu extends WaterPoint
{
    private boolean _certified;
    private final DocumentReference _authorRef;
    private User _author;

    private List<Note> _notes;

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
    public void loadAuthor(Callback<User> callback)
    {
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

    public List<Note> getNotes() { return _notes; }

    @SuppressWarnings("ConstantConditions")
    public void loadNotes(Callback<List<Note>> callback)
    {
        if (_notes != null)
            return;

        _notes = new ArrayList<>();
        FirestoreDb.readWhere("Note", "waterPointCommu",
                FirestoreDb.getDb().collection("WaterPoint").document(super.getId()),
                (id, map) -> _notes.add(new Note(id, (int) (long) map.get("note"))))
                .addOnCompleteListener(task -> callback.call(_notes));
    }

    public Integer getNote()
    {
        if (_notes == null)
            return null;

        if (_notes.size() == 0)
            return -1;

        double d = 0;
        for (Note note : _notes)
            d += note.getNote();
        return (int) d / _notes.size();
    }
}
