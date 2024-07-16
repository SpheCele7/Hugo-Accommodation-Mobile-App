package com.example.studentaccomidation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class applicationItemAdapter extends RecyclerView.Adapter<applicationItemAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<applicationItem> applicationItems;

    public applicationItemAdapter(Context context, ArrayList<applicationItem> applicationItems) {
        this.context = context;
        this.applicationItems = applicationItems;
    }

    @NonNull
    @Override
    public applicationItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.applications_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull applicationItemAdapter.MyViewHolder holder, int position) {
        applicationItem applicationItem = applicationItems.get(position);

        if (applicationItem.getPayer() != null) {
            holder.sPayerTv.setText("Payer: " + applicationItem.getPayer());
        } else {
            Log.d("Adapter", "Payer data is null");
        }

        if (applicationItem.getDate() != null) {
            holder.sApplicationDateTv.setText("Date: " + applicationItem.getDate());
        } else {
            Log.d("Adapter", "Date data is null");
        }

        if (applicationItem.getRequirements() != null) {
            holder.sResRequirementsTv.setText("Requirements: " + applicationItem.getRequirements().toString());
        } else {
            holder.sResRequirementsTv.setText("Requirements: None");
        }

        if (applicationItem.getStatus() != null) {
            holder.sStatusTv.setText("Status: " + applicationItem.getStatus());
        } else {
            Log.d("Adapter", "status data is null");
        }

        if (applicationItem.getResName() != null) {
            holder.sResNameTv.setText("Residence: " + applicationItem.getResName());
        } else {
            holder.sResNameTv.setText("Residence: ");
            Log.d("Adapter", "resname data is null");
        }

        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicationItem.setStatus("Cancelled");
                updateStatusInFirestore(applicationItem);
            }
        });
    }

    private void updateStatusInFirestore(applicationItem item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("applications").document(item.getDocumentId());
        docRef.update("status", item.getStatus())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Application Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return applicationItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sPayerTv, sResRequirementsTv, sApplicationDateTv, sResNameTv, sStatusTv;
        Button cancelButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sPayerTv =itemView.findViewById(R.id.sPayerTv);
            sResRequirementsTv =itemView.findViewById(R.id.sResRequirementsTv);
            sApplicationDateTv = itemView.findViewById(R.id.sApplicationDate);
            sResNameTv = itemView.findViewById(R.id.sResName);
            sStatusTv = itemView.findViewById(R.id.sStatusTv);
            cancelButton = itemView.findViewById(R.id.cancelBtn);
        }
    }
}