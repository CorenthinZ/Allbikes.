package com.pyrolink.allbikes.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pyrolink.allbikes.Callback2;

import java.util.Map;

public class FirestoreDb
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
