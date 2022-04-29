package com.pyrolink.allbikes.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pyrolink.allbikes.Callback;
import com.pyrolink.allbikes.Callback2;
import com.pyrolink.allbikes.model.WaterPoint;

import java.util.Map;

public class AppDatabase
{

    @NonNull
    public static FirebaseFirestore getDb() { return FirebaseFirestore.getInstance(); }

    @NonNull
    public static Task<QuerySnapshot> readAll(String collection, Callback2<String, Map<String, Object>> onObject)
    {
        return getDb().collection(collection).get().addOnCompleteListener(task ->
        {
            if (!task.isSuccessful())
            {
                Log.w("", "Error getting documents !", task.getException());
                return;
            }

            for (QueryDocumentSnapshot document : task.getResult())
                onObject.call(document.getId(), document.getData());
        });
    }
}
