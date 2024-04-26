package com.example.taskmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.ViewHolder> {
    private List<Task> taskList;
    private Context context;
    private OnTaskListener onTaskListener;

    public TaskViewAdapter(List<Task> taskList, Context context, OnTaskListener onTaskListener) {
        this.taskList = taskList;
        this.context = context;
        this.onTaskListener = onTaskListener;
    }

    @NonNull
    @Override
    public TaskViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.task_row,parent,false);
        return new ViewHolder(itemView, onTaskListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.row_name_textview.setText(taskList.get(position).getName());
        holder.row_date_textview.setText(taskList.get(position).getDue_date());
        holder.row_desc_textview.setText(taskList.get(position).getDesc());
        holder.row_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task removedItem = taskList.get(position);
                taskList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView row_name_textview, row_date_textview, row_desc_textview;
        Button row_edit_button, row_delete_button;
        CardView row_cardview;
        OnTaskListener onTaskListener;

        public ViewHolder(@NonNull View itemView, OnTaskListener onTaskListener) {
            super(itemView);
            row_name_textview = itemView.findViewById(R.id.row_name_textview);
            row_date_textview = itemView.findViewById(R.id.row_date_textview);
            row_desc_textview = itemView.findViewById(R.id.row_desc_textview);
            row_edit_button = itemView.findViewById(R.id.row_edit_button);
            row_delete_button = itemView.findViewById(R.id.row_delete_button);
            this.onTaskListener = onTaskListener;

            row_edit_button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTaskListener.onTaskClick(getAdapterPosition());
        }
    }

    public interface OnTaskListener{
        void onTaskClick(int position);
    }
}
