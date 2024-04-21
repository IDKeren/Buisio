package com.example.buisio.ui.schedule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buisio.Interface.ClassInteractionListener;
import com.example.buisio.Models.ClassModel;
import com.example.buisio.R;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;
import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ViewHolder> {
    private List<ClassModel> classes = new ArrayList<>();
    private ClassInteractionListener listener;
    private Context context;
    private LayoutInflater mInflater;



    public ClassesAdapter(Context context, List<ClassModel> data,ClassInteractionListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.classes = data;
        this.context = context;
        this.listener = listener;
    }
    @Override
    public ClassesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      //  Context context = parent.getContext();
      //  LayoutInflater inflater = LayoutInflater.from(context);
      //  View classesView = inflater.inflate(R.layout.item_class, parent, false);
      //  ViewHolder viewHolder = new ViewHolder(classesView);
        View view = mInflater.inflate(R.layout.item_class, parent, false);

        //return viewHolder;
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ClassesAdapter.ViewHolder holder, int position) {
        ClassModel classInfo = classes.get(position);
        holder.bind(classInfo);

    }

    @Override
    public int getItemCount() {

        return classes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setClasses(List<ClassModel> newClasses) {
        classes.clear();
        classes.addAll(newClasses);
        notifyDataSetChanged();
    }

    public void updateClass(ClassModel updatedClass, int position) {
        classes.set(position, updatedClass);
        notifyItemChanged(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView classNameTextView,classTimeTextView,classInstructorTextView,classBookingTextView,classNumOfMembersTextView;// Assuming an item layout with a TextView
        Button addClassButton,cancelBookingButton;


        ViewHolder(View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.classNameTextView);
            classTimeTextView = itemView.findViewById(R.id.classTimeTextView);
            classInstructorTextView = itemView.findViewById(R.id.classInstructorTextView);
            classNumOfMembersTextView = itemView.findViewById(R.id.classNumOfMembersTextView);
            classBookingTextView = itemView.findViewById(R.id.classBookingTextView);
            addClassButton = itemView.findViewById(R.id.addClassButtonAdapter);
            cancelBookingButton = itemView.findViewById(R.id.cancelClassButtonAdapter);

            addClassButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEnrollRequested(classes.get(position).getId(), position);
                    toggleButtons(false);
                }
            });

            cancelBookingButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onCancelBookingRequested(classes.get(position).getId(), position);
                    toggleButtons(true);
                }
            });
        }
        private void toggleButtons(boolean showEnroll) {
            addClassButton.setVisibility(showEnroll ? View.VISIBLE : View.GONE);
            cancelBookingButton.setVisibility(showEnroll ? View.GONE : View.VISIBLE);
        }
        void bind(ClassModel classInfo) {
            classNameTextView.setText(classInfo.getName());
            classTimeTextView.setText(classInfo.getTime());
            classInstructorTextView.setText(classInfo.getClassInstructor());
            classNumOfMembersTextView.setText(classInfo.getNumOfUsers() + "/10 Booked");

            String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            boolean isEnrolled = classInfo.getEnrolledUsers() != null && classInfo.getEnrolledUsers().containsKey(currentUser);
            classBookingTextView.setVisibility(isEnrolled ? View.VISIBLE : View.GONE);
            toggleButtons(!isEnrolled);

        }
    }
}