package com.example.buisio.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buisio.Models.ClassModel;
import com.example.buisio.R;
import com.example.buisio.ui.feed.FeedAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView textViewWelcome;
    private FeedAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewActivities);
        textViewWelcome = view.findViewById(R.id.textViewWelcome);

        // Initialize Firebase DatabaseReference
        databaseReference = FirebaseDatabase.getInstance().getReference("classes");

        setupRecyclerView();
        loadClasses();
        return view;
    }

    private void setupRecyclerView() {
        adapter = new FeedAdapter(getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadClasses() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ClassModel> classes = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ClassModel classModel = snapshot.getValue(ClassModel.class);
                    classes.add(classModel);
                }
                adapter.setClasses(classes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
