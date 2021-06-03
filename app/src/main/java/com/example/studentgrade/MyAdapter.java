package com.example.studentgrade;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends ListAdapter<Student, MyAdapter.MyViewHolder> {

    MyAdapter() {
        super(new DiffUtil.ItemCallback<Student>() {
            @Override
            public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return (oldItem.getName().equals(newItem.getName())
                        && oldItem.getGrade().equals(newItem.getGrade())
                        && oldItem.getInfo().equals(newItem.getInfo()));
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Student student = getItem(position);
        holder.textViewNumber.setText(String.valueOf(student.getId()));
        holder.textViewName.setText(student.getName());
        holder.textViewGrade.setText(student.getGrade());
        holder.textViewInfo.setText(student.getInfo());
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber, textViewName, textViewGrade, textViewInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewGrade = itemView.findViewById(R.id.textViewGrade);
            textViewInfo = itemView.findViewById(R.id.textViewInfo);
        }
    }
}
