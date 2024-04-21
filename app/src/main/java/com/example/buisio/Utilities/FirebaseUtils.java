package com.example.buisio.Utilities;

import android.content.Context;
import android.util.Log;

import com.example.buisio.Interface.FirebaseDataListener;
import com.example.buisio.Models.ClassList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FirebaseUtils {
    private static FirebaseDatabase firebaseDatabase;

    private static FirebaseAuth aAuth;

    public static FirebaseDatabase getDatabase() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance("https://buisio-default-rtdb.europe-west1.firebasedatabase.app");
        }
        return firebaseDatabase;
    }
}
