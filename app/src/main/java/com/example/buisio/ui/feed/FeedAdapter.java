package com.example.buisio.ui.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buisio.Models.ClassModel;
import com.example.buisio.R;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ClassViewHolder> {
    private List<ClassModel> classes;
    private LayoutInflater inflater;

    public FeedAdapter(Context context, List<ClassModel> classes) {
        this.inflater = LayoutInflater.from(context);
        this.classes = classes;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_class, parent, false);
        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
        ClassModel classModel = classes.get(position); // Use classModel instead of classes
        holder.bind(classModel);
    }

    @Override
    public int getItemCount() {
        return classes != null ? classes.size() : 0;
    }

    public void setClasses(List<ClassModel> newClasses) {
        this.classes = newClasses;
        notifyDataSetChanged();
    }

    static class ClassViewHolder extends RecyclerView.ViewHolder {
        TextView classNameTextView;
        TextView classDateTextView;
        TextView classInstructorTextView;

        public ClassViewHolder(View itemView) {
            super(itemView);
            classNameTextView = itemView.findViewById(R.id.classNameTextView);
          //  classDateTextView = itemView.findViewById(R.id.classDateTextView);
            classInstructorTextView = itemView.findViewById(R.id.classInstructorTextView);
        }

        void bind(ClassModel classModel) {
            classNameTextView.setText(classModel.getName());
            classDateTextView.setText(classModel.getDate());
          //  classInstructorTextView.setText(classModel.getInstructor());
        }
    }
}
