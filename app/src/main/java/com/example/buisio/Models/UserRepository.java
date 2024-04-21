package com.example.buisio.Models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.buisio.Models.ClassModel;
import com.example.buisio.Utilities.FirebaseUtils;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class UserRepository {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public UserRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseUtils.getDatabase().getReference();
    }

    public LiveData<String> getUserNameLiveData() {
        MutableLiveData<String> userName = new MutableLiveData<>();
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userName.setValue(dataSnapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            userName.setValue("Anonymous");
                        }
                    });
        } else {
            userName.setValue("Guest");
        }
        return userName;
    }


}
