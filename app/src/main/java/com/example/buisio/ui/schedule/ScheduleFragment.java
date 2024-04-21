package com.example.buisio.ui.schedule;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buisio.Interface.ClassInteractionListener;
import com.example.buisio.Models.ClassModel;
import com.example.buisio.R;
import com.example.buisio.Utilities.FirebaseUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ScheduleFragment extends Fragment implements ClassInteractionListener {

    private CalendarView calendarView;
    private RecyclerView classesRecyclerView;
    private ClassesAdapter classesAdapter;
    private DatabaseReference databaseReference;
    private Button addClassButton;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        initializeViews(view);
        setupListeners();
        setupButtonVisibility();
        databaseReference = FirebaseUtils.getDatabase().getReference("classes");
        return view;
    }
    @Override
    public void onAddClassRequested() {
        // Open dialog or new activity to add class
        showAddClassDialog();
    }

    @Override
    public void onEnrollRequested(String classId,int position) {
        enrollUserToClass(classId, position);
    }

    @Override
    public void onCancelBookingRequested(String classId, int position) {
        cancelBooking(classId, FirebaseAuth.getInstance().getCurrentUser().getUid(), position);
    }

    private void showAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_class, null);

        final TextInputEditText classNameEditText = view.findViewById(R.id.className);
        final TextInputEditText classDateEditText = view.findViewById(R.id.classDate);
        final TextInputEditText classTimeEditText = view.findViewById(R.id.classTime);
        final TextInputEditText classInstructorEditText = view.findViewById(R.id.classInstructor);

        builder.setView(view)
                .setPositiveButton("Add", (dialog, id) -> {
                    String className = classNameEditText.getText().toString();
                    String classDate = classDateEditText.getText().toString();
                    String classTime = classTimeEditText.getText().toString();
                    String classIns = classInstructorEditText.getText().toString();
                    addNewClass(className, classDate, classTime,classIns);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initializeViews(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        classesRecyclerView = view.findViewById(R.id.classesRecyclerView);
        classesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        classesAdapter = new ClassesAdapter(getContext(),new ArrayList<>(),this);
        classesRecyclerView.setAdapter(classesAdapter);
        addClassButton = view.findViewById(R.id.addClassButton);
    }

    private void setupListeners() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selectedDate = Calendar.getInstance(Locale.getDefault());
            selectedDate.set(year, month, dayOfMonth);
            StringBuilder str = new StringBuilder();
            str.append(year).append('-')
                    .append(String.format("%02d", month+1)).append('-')
                    .append(String.format("%02d", dayOfMonth));
            updateClassList(str.toString());
        });
        addClassButton.setOnClickListener(v -> showAddClassDialog());
    }

    private void setupButtonVisibility() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getUid().toString().equals("85apn3a39iPMAHz5rdxOCsKxoqc2") ) {
            addClassButton.setVisibility(View.VISIBLE);
        } else {
            addClassButton.setVisibility(View.GONE);
        }
    }

    private void addNewClass(String className,String classDate,String classTime, String classIns) {

        String Id = databaseReference.push().getKey();

        if (Id != null) {
            ClassModel newClass = new ClassModel(Id,className,classDate,classTime, classIns);
            databaseReference.child(Id).setValue(newClass)
                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Class added successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to add class", Toast.LENGTH_SHORT).show());
        }else {
            Toast.makeText(getContext(), "Error generating unique key for class", Toast.LENGTH_SHORT).show();
        }
    }


    // Method to update the class list based on the selected date
    private void updateClassList(String date) {
        List<ClassModel> classes = new ArrayList<>();

        databaseReference.orderByChild("date").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.d("ScheduleFragment", "No classes found for this date");
                    classesAdapter.setClasses(classes);
                    classesAdapter.notifyDataSetChanged();
                    return;
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ClassModel classInfo = snapshot.getValue(ClassModel.class);
                    classes.add(classInfo);
                }
                classesAdapter.setClasses(classes);
                classesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ScheduleFragment", "Failed to load class data.", databaseError.toException());
            }
        });
    }

    private void enrollUserToClass(String classId, final int position) {
        DatabaseReference classRef = databaseReference.child(classId);

        classRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                ClassModel c = mutableData.getValue(ClassModel.class);
                if (c == null) {
                    return Transaction.success(mutableData);
                }

                if (c.getEnrolledUsers() == null) {
                    c.setEnrolledUsers(new HashMap<>());
                }
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (!c.getEnrolledUsers().containsKey(userId)) {
                    c.getEnrolledUsers().put(userId, true);
                    c.setNumOfUsers(c.getNumOfUsers() + 1);  // Increment the count
                }

                mutableData.setValue(c);  // Commit the updated class
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (committed) {
                    ClassModel updatedClass = dataSnapshot.getValue(ClassModel.class);
                    classesAdapter.updateClass(updatedClass, position); // Update the UI
                } else {
                    Log.e("ScheduleFragment", "Failed to enroll in class.");
                }
            }
        });


    }



    private void cancelBooking(String classId, String userId, int position) {
        DatabaseReference classRef = databaseReference.child(classId);

        classRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData){
            ClassModel c = mutableData.getValue(ClassModel.class);
            if (c == null) {
                // If null, there's nothing to cancel, return success without changes.
                return Transaction.success(mutableData);
            }

            if (c.getEnrolledUsers() != null && c.getEnrolledUsers().containsKey(userId)) {
                // Check if the user is actually enrolled
                int currentUsers = c.getNumOfUsers();
                if (currentUsers > 0) {
                    // Decrement the number of users
                    c.setNumOfUsers(currentUsers - 1);
                }
                // Remove the user from the enrolled users map
                c.getEnrolledUsers().remove(userId);
            }

            // Set the updated class back to the database
            mutableData.setValue(c);
            return Transaction.success(mutableData);
        }
            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                if (committed) {
                    Toast.makeText(getContext(), "Booking cancelled successfully.", Toast.LENGTH_SHORT).show();
                    ClassModel updatedClass = dataSnapshot.getValue(ClassModel.class);
                    if (updatedClass != null) {
                        classesAdapter.updateClass(updatedClass, position);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to cancel booking.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
