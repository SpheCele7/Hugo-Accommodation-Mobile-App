package com.example.studentaccomidation.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.studentaccomidation.R;
import com.example.studentaccomidation.notificationItem;
import com.example.studentaccomidation.notificationItemAdapter;
import com.example.studentaccomidation.resItem;
import com.example.studentaccomidation.resItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class NotificationsFragment extends Fragment {

    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayList<notificationItem> notificationItems;
    notificationItemAdapter notificationItemAdapter;
    FirebaseFirestore db;
    String email;
    FirebaseUser user;
    FirebaseAuth auth;

    public NotificationsFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        progressDialog=new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        recyclerView = view.findViewById(R.id.notificationsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }
        notificationItems = new ArrayList<notificationItem>();
        notificationItemAdapter = new notificationItemAdapter(requireContext(), notificationItems);

        recyclerView.setAdapter(notificationItemAdapter);
        eventChangeListener();

        return view;
    }

    private void eventChangeListener() {
        db.collection("notifications").whereEqualTo("student_email", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Toast.makeText(requireContext(), "Error getting data", Toast.LENGTH_SHORT).show();
                        }
                        assert value != null;
                        for (DocumentChange documentChange: value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                notificationItems.add(documentChange.getDocument().toObject(notificationItem.class));
                            }
                        }
                        notificationItemAdapter.notifyDataSetChanged();
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }
}