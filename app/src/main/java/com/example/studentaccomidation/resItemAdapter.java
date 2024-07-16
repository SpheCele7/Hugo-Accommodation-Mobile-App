package com.example.studentaccomidation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class resItemAdapter extends RecyclerView.Adapter<resItemAdapter.MyViewHolder> {
    static Context context;
    static ArrayList<resItem> resItems;

    public resItemAdapter(Context context, ArrayList<resItem> resItems) {
        this.context = context;
        this.resItems = resItems;
    }
    public void filterList(ArrayList<resItem> filteredList) {
        resItems = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public resItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.res_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull resItemAdapter.MyViewHolder holder, int position) {
        resItem resItem = resItems.get(position);

        Picasso.get().load(resItem.getPicture()).into(holder.imageView);
        holder.resName.setText(resItem.name);
        holder.resAddress.setText(resItem.address);
        holder.resEmail.setText(resItem.email);
        holder.resPhone.setText(resItem.telephone);
    }






    @Override
    public int getItemCount() {
        return resItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView resName, resAddress, resEmail, resPhone;
        Button applyButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            resName=itemView.findViewById(R.id.resName);
            resAddress=itemView.findViewById(R.id.resAddressTv);
            resEmail=itemView.findViewById(R.id.resEmailTv);
            resPhone=itemView.findViewById(R.id.resTelNumber);
            applyButton = itemView.findViewById(R.id.applyBtn);
            applyButton.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // Get the position of the item clicked
            if (position != RecyclerView.NO_POSITION) {
                // Retrieve the clicked item
                resItem clickedItem = resItems.get(position);
                // Retrieve the document ID of the residence building
                String documentId = clickedItem.getDocumentId();

                // Assuming you want to start an activity for the application form
                // You can pass the document ID to the activity via Intent
                Intent intent = new Intent(context, Application.class);
                intent.putExtra("documentId", documentId);
                context.startActivity(intent);
            }


        }
    }
}
