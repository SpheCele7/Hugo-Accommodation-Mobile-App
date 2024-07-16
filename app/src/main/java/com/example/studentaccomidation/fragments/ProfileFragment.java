package com.example.studentaccomidation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.studentaccomidation.Login;
import com.example.studentaccomidation.MainActivity;
import com.example.studentaccomidation.R;
import com.example.studentaccomidation.updateProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore userInformation;
    String name;
    String LName;
    String email;
    String course;
    String dob;
    String institution;
    String gender;
    String nationalId;
    String studentNo;
    String interests;
    String address;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView fullNameTv = view.findViewById(R.id.fullNameTv);
        TextView emailTv = view.findViewById(R.id.emailTv);
        TextView courseTv = view.findViewById(R.id.courseTv);
        TextView dobTv = view.findViewById(R.id.birth_dateTv);
        TextView institutionTv = view.findViewById(R.id.institutionTv);
        TextView genderTv = view.findViewById(R.id.genderTv);
        TextView nationalIdTv = view.findViewById(R.id.national_idTv);
        TextView studentNoTv = view.findViewById(R.id.student_numberTv);
        TextView interestsTv = view.findViewById(R.id.interestsTv);
        TextView addressTv = view.findViewById(R.id.home_addressTv);
        Button accountBtn = view.findViewById(R.id.account);
        Button logoutBtn = view.findViewById(R.id.logout);
        Button updateProfileBtn = view.findViewById(R.id.updateProfile);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userInfo = db.collection("users");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            email=user.getEmail();
            accountBtn.setVisibility(View.GONE);

            userInfo.whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            name = documentSnapshot.getString("name");
                            LName = documentSnapshot.getString("LName");
                            dob = documentSnapshot.getString("birth_date");
                            course = documentSnapshot.getString("course");
                            gender = documentSnapshot.getString("gender");
                            address = documentSnapshot.getString("home_address");
                            institution = documentSnapshot.getString("institution");
                            interests = documentSnapshot.getString("interests");
                            nationalId = documentSnapshot.getString("national_id");
                            studentNo = documentSnapshot.getString("student_number");

                            fullNameTv.setText(name != null && LName != null ? name + " " + LName : "");
                            emailTv.setText(email != null ? email : "");
                            courseTv.setText(course != null ? course : "");
                            dobTv.setText(dob != null ? dob : "");
                            institutionTv.setText(institution != null ? institution : "");
                            genderTv.setText(gender != null ? gender : "");
                            nationalIdTv.setText(nationalId != null ? nationalId : "");
                            studentNoTv.setText(studentNo != null ? studentNo : "");
                            interestsTv.setText(interests != null ? interests : "");
                            addressTv.setText(address != null ? address : "");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        Toast.makeText(requireContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show();
                        Toast.makeText(requireContext(), "Update profile", Toast.LENGTH_SHORT).show();
                    });
        }
        else{
            Toast.makeText(requireContext(), "Please Login/Create account", Toast.LENGTH_SHORT).show();
            logoutBtn.setVisibility(View.GONE);
            updateProfileBtn.setVisibility(View.GONE);
        }

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), Login.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(requireContext(), "Logout Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), updateProfile.class);
                startActivity(intent);
            }
        });

        return view;
    }
}