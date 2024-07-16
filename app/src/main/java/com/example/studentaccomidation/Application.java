package com.example.studentaccomidation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Application extends AppCompatActivity {

    Button submitBtn;
    CheckBox requirementsCb, agreeTCCb;
    TextView fullNameTV, studentNoTV, residenceTV, institutionTV;
    TextInputEditText payerEdt;
    String student_email, residenceID, status, payer, name, LName, institution, studentNo, dob, course, gender, address, interests, nationalId;
    String date;
    LocalDate unformattedDate;
    Boolean agreeTc, requirements;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_application);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        submitBtn = findViewById(R.id.submitAppl);
        requirementsCb = findViewById(R.id.req);
        agreeTCCb = findViewById(R.id.agreeTC);
        fullNameTV = findViewById(R.id.nameAppTv);
        payerEdt = findViewById(R.id.payerEdt);
        studentNoTV = findViewById(R.id.studentNoAppTv);
        residenceTV = findViewById(R.id.resNameAppTv);
        institutionTV = findViewById(R.id.universityAppTv);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        CollectionReference userInfo = db.collection("users");

        if (user != null) {
            student_email = user.getEmail();

            userInfo.whereEqualTo("email", student_email)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            name = documentSnapshot.getString("name");
                            LName = documentSnapshot.getString("LName");
                            institution = documentSnapshot.getString("institution");
                            studentNo = documentSnapshot.getString("student_number");
                            dob = documentSnapshot.getString("birth_date");
                            course = documentSnapshot.getString("course");
                            gender = documentSnapshot.getString("gender");
                            address = documentSnapshot.getString("home_address");
                            interests = documentSnapshot.getString("interests");
                            nationalId = documentSnapshot.getString("national_id");
                            residenceID = getIntent().getStringExtra("documentId");

                            fullNameTV.setText(name != null && LName != null ? "Application for " + name + " " + LName : "");
                            institutionTV.setText(institution != null ? institution : "");
                            studentNoTV.setText(studentNo != null ? "Student Number: " + studentNo : "");
                        }
                    });

        }

        requirementsCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the requirements boolean variable when the checkbox state changes
            requirements = isChecked;
        });

        agreeTCCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update the agreeTc boolean variable when the checkbox state changes
            agreeTc = isChecked;
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payer = String.valueOf(payerEdt.getText());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    unformattedDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    date = unformattedDate.format(formatter);
                }

                status = "Pending";
                if (!agreeTc){
                    Toast.makeText(Application.this, "Accept terms and conditions to continue",
                            Toast.LENGTH_SHORT).show();
                }
                else if (student_email == null || name == null || LName == null || institution == null || studentNo == null || dob == null || course == null || gender == null || address == null || interests == null || nationalId == null) {
                    Toast.makeText(Application.this, "Please update profile,\n Information missing",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Application.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Map<String, Object> application = new HashMap<>();
                    application.put("payer", payer);
                    application.put("requirements", requirements);
                    application.put("status", status);
                    application.put("date", date);
                    application.put("residenceId", residenceID);
                    application.put("student_email", student_email);
                    db.collection("applications")
                            .add(application)
                            .addOnCompleteListener(documentReferenceTask -> {
                                if (documentReferenceTask.isSuccessful()) {
                                    Toast.makeText(Application.this, "Application Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Application.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If adding user data to Firestore fails
                                    Toast.makeText(Application.this, "Application failed.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}