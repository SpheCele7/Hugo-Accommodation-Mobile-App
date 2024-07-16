package com.example.studentaccomidation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentaccomidation.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Admins extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<adminApplicationItem> adminApplicationItems;
    adminApplicationItemAdapter adminApplicationItemAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admins);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        Button logoutBtn = findViewById(R.id.adminLogoutBtn);

        recyclerView = findViewById(R.id.adminRecycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Admins.this));

        db = FirebaseFirestore.getInstance();
        adminApplicationItems = new ArrayList<adminApplicationItem>();
        adminApplicationItemAdapter = new adminApplicationItemAdapter(Admins.this, adminApplicationItems);

        recyclerView.setAdapter(adminApplicationItemAdapter);

        try {
            eventChangeListener();
        } catch (Exception e) {
            Log.e("Firestore error ", e.getMessage());
            throw new RuntimeException(e);
        }

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Admins.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Admins.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void eventChangeListener() throws Exception{
        db.collection("applications").orderBy("student_email", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("firestore error", Objects.requireNonNull(error.getMessage()));
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(Admins.this, "Error getting data", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (value == null || value.isEmpty()) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            return;
                        }

                        // Track the number of pending tasks
                        int totalTasks = value.getDocumentChanges().size();
                        AtomicInteger completedTasks = new AtomicInteger(0);

                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                adminApplicationItem applicationItem = documentChange.getDocument().toObject(adminApplicationItem.class);
                                db.collection("users").whereEqualTo("email", applicationItem.getStudent_email())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String fullName = document.getString("name") + " " + document.getString("LName");
                                                        applicationItem.setFullName(fullName);
                                                    }
                                                } else {
                                                    Log.d("Error getting documents: ", String.valueOf(task.getException()));
                                                }
                                                // Increment completed tasks count
                                                int count = completedTasks.incrementAndGet();
                                                // Check if all tasks are completed
                                                if (count == totalTasks) {
                                                    // All tasks completed, update RecyclerView
                                                    adminApplicationItemAdapter.notifyDataSetChanged();
                                                    if (progressDialog.isShowing()) {
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            }
                                        });
                                db.collection("residences").document(applicationItem.getResidenceId())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        // Retrieve the "name" field
                                                        String resName = document.getString("name");
                                                        applicationItem.setResName(resName);
                                                    }
                                                } else {
                                                    Log.d("get failed with ", String.valueOf(task.getException()));
                                                }
                                                // Increment completed tasks count
                                                int count = completedTasks.incrementAndGet();
                                                // Check if all tasks are completed
                                                if (count == totalTasks) {
                                                    // All tasks completed, update RecyclerView
                                                    adminApplicationItemAdapter.notifyDataSetChanged();
                                                    if (progressDialog.isShowing()) {
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            }
                                        });
                                applicationItem.setDocumentId(documentChange.getDocument().getId());
                                adminApplicationItems.add(applicationItem);
                            }
                        }
                    }
                });
    }
}