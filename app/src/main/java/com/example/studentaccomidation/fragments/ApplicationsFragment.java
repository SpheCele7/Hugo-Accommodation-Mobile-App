 package com.example.studentaccomidation.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentaccomidation.R;
import com.example.studentaccomidation.applicationItem;
import com.example.studentaccomidation.applicationItemAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


 public class ApplicationsFragment extends Fragment {

     RecyclerView applicationsRecyclerView;
     ArrayList<applicationItem> applicationItems;
     applicationItemAdapter applicationItemAdapter;
     FirebaseFirestore db;
     String email;
     FirebaseAuth auth;
     FirebaseUser user;
     ProgressDialog progressDialog;

    public ApplicationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applications, container, false);

        progressDialog=new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        applicationsRecyclerView = view.findViewById(R.id.applicationsRecyclerView);
        applicationsRecyclerView.setHasFixedSize(true);
        applicationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            applicationItems = new ArrayList<applicationItem>();
            applicationItemAdapter = new applicationItemAdapter(requireContext(), applicationItems);

            applicationsRecyclerView.setAdapter(applicationItemAdapter);

            eventChangeListener();
        }

        return view;
    }

     private void eventChangeListener() {
         db.collection("applications").whereEqualTo("student_email", email)
                 .addSnapshotListener(new EventListener<QuerySnapshot>() {
                     @Override
                     public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                         if (error != null) {
                             if (progressDialog.isShowing()) {
                                 progressDialog.dismiss();
                             }
                             Toast.makeText(requireContext(), "Error getting data", Toast.LENGTH_SHORT).show();
                             return;
                         }
                         for (DocumentChange documentChange : value.getDocumentChanges()) {
                             if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                 applicationItem applicationItem = documentChange.getDocument().toObject(applicationItem.class);
                                 String residenceId = applicationItem.getResidenceId();
                                 db.collection("residences").document(residenceId)
                                         .get()
                                         .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                             @Override
                                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                 if (task.isSuccessful()) {
                                                     DocumentSnapshot document = task.getResult();
                                                     if (document.exists()) {
                                                         String resName = document.getString("name");
                                                         applicationItem.setResName(resName);
                                                         applicationItem.setDocumentId(documentChange.getDocument().getId());
                                                         applicationItems.add(applicationItem);
                                                         applicationItemAdapter.notifyDataSetChanged();
                                                     } else {
                                                         Log.d("Residence not found", "No such document");
                                                     }
                                                 } else {
                                                     Log.d("get failed with ", String.valueOf(task.getException()));
                                                 }
                                                 if (progressDialog.isShowing()) {
                                                     progressDialog.dismiss();
                                                 }
                                             }
                                         });
                             }
                         }
                     }
                 });
     }
 }