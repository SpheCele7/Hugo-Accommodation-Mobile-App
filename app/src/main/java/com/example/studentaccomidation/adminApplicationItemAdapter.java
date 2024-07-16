package com.example.studentaccomidation;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class adminApplicationItemAdapter extends RecyclerView.Adapter<adminApplicationItemAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<adminApplicationItem> applicationItems;

    public adminApplicationItemAdapter(Context context, ArrayList<adminApplicationItem> applicationItems) {
        this.context = context;
        this.applicationItems = applicationItems;
    }

    @NonNull
    @Override
    public adminApplicationItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.admin_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        adminApplicationItem applicationItem = applicationItems.get(position);

        if (applicationItem.getPayer() != null) {
            holder.payerTv.setText("Payer: " + applicationItem.getPayer());
        }else {
            Log.d("Adapter", "Payer data is null");
        }

        if (applicationItem.getDate() != null) {
            holder.applicationDateTv.setText("Date: " + applicationItem.getDate());
        }else {
            Log.d("Adapter", "Date data is null");
        }

        if (applicationItem.getRequirements() != null) {
            holder.resRequirementsTv.setText("Requirements: " + applicationItem.getRequirements().toString());
        }else {
            holder.resRequirementsTv.setText("Requirements: None");
        }

        if (applicationItem.getStatus() != null) {
            holder.statusTv.setText("Status: " + applicationItem.getStatus());
        }else {
            Log.d("Adapter", "status data is null");
        }

        if (applicationItem.getResName() != null) {
            holder.resNameTv.setText("Residence: " + applicationItem.getResName());
        }else {
            holder.resNameTv.setText("Residence: ");
            Log.d("Adapter", "resname data is null");
        }

        if (applicationItem.getFullName() != null) {
            holder.fullNameTv.setText("Name: " + applicationItem.getFullName());
        }else {
            Log.d("Adapter", "fullname data is null");
        }


        holder.pendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicationItem.setStatus("Pending");
                updateStatusInFirestore(applicationItem);
            }
        });

        holder.approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicationItem.setStatus("Approved");
                updateStatusInFirestore(applicationItem);
            }
        });

        holder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applicationItem.setStatus("Rejected");
                updateStatusInFirestore(applicationItem);
            }
        });
    }

    private void updateStatusInFirestore(adminApplicationItem item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("applications").document(item.getDocumentId());
        docRef.update("status", item.getStatus())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String overview = null, description=null;
                        if (Objects.equals(item.getStatus(), "Approved")){
                            overview="Hooray! Application Approved";
                            description="Your application for " + item.getResName() + " has been approved see the applications tab.";
                        }else if(Objects.equals(item.getStatus(), "Rejected")){
                            overview="Unfortunate, Application Rejected";
                            description="Your application for " + item.getResName() + " has been rejected see the applications tab.";
                        }
                        if(overview!=null & description!=null) {
                            Map<String, Object> notification = new HashMap<>();
                            notification.put("overview", overview);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                String date;
                                LocalDate unformattedDate;
                                unformattedDate = LocalDate.now();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                date = unformattedDate.format(formatter);
                                notification.put("date", date);
                            }
                            notification.put("description", description);
                            notification.put("student_email", item.getStudent_email());
                            notification.put("applicationId", item.getDocumentId());
                            db.collection("notifications")
                                    .add(notification)
                                    .addOnCompleteListener(documentReferenceTask -> {
                                        if (documentReferenceTask.isSuccessful()) {
                                            Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // If adding user data to Firestore fails
                                            Toast.makeText(context, "Updating failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors here
                    }
                });

    }

    @Override
    public int getItemCount() {
        return applicationItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView payerTv, resRequirementsTv, applicationDateTv, fullNameTv, resNameTv, statusTv;
        Button pendingButton, approveButton, rejectButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTv =itemView.findViewById(R.id.studentName);
            payerTv =itemView.findViewById(R.id.payerTv);
            resRequirementsTv =itemView.findViewById(R.id.resRequirementsTv);
            applicationDateTv = itemView.findViewById(R.id.applicationDate);
            resNameTv = itemView.findViewById(R.id.resName);
            statusTv = itemView.findViewById(R.id.statusTv);
            pendingButton = itemView.findViewById(R.id.pendingBtn);
            approveButton = itemView.findViewById(R.id.approveBtn);
            rejectButton = itemView.findViewById(R.id.rejectBtn);
        }
    }
}
