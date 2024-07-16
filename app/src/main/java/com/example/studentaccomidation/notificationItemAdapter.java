package com.example.studentaccomidation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class notificationItemAdapter extends RecyclerView.Adapter<notificationItemAdapter.MyViewHolder>{
    Context context;
    ArrayList<notificationItem> notificationItems;

    public notificationItemAdapter(Context context, ArrayList<notificationItem> notificationItems) {
        this.context = context;
        this.notificationItems = notificationItems;
    }

    @NonNull
    @Override
    public notificationItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notifications_item,parent,false);
        return new notificationItemAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull notificationItemAdapter.MyViewHolder holder, int position) {
        notificationItem notificationItem = notificationItems.get(position);

        holder.description.setText(notificationItem.description);
        holder.overview.setText(notificationItem.overview);
    }

    @Override
    public int getItemCount() {
        return notificationItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView description, overview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            overview = itemView.findViewById(R.id.overviewTv);
            description = itemView.findViewById(R.id.descriptionTv);
        }
    }
}
