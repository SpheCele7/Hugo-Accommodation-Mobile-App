package com.example.studentaccomidation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Calendar;

public class updateProfile extends AppCompatActivity {

    TextInputEditText editTextName,editTextLName,editTextBirthDate,editTextNationalId,editTextInstitution,editTextHomeAddress,editTextCourse,editTextInterests,editTextGender,editTextStudentNumber;
    FirebaseFirestore db;
    Button updateBtn;
    FirebaseUser user;
    FirebaseAuth auth;
    String email, userId;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(updateProfile.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            userId = documentSnapshot.getId();
                        }
                    });
        }

        editTextName = findViewById(R.id.updateName);
        editTextLName = findViewById(R.id.updateLName);
        editTextBirthDate = findViewById(R.id.updateBirthDate);
        editTextNationalId = findViewById(R.id.updateIdNum);
        editTextInstitution = findViewById(R.id.updateInstitution);
        editTextHomeAddress = findViewById(R.id.updateHomeAddress);
        editTextCourse = findViewById(R.id.updateCourse);
        editTextInterests = findViewById(R.id.updateInterests);
        editTextGender = findViewById(R.id.updateGender);
        editTextStudentNumber = findViewById(R.id.updateStudentNumber);
        updateBtn = findViewById(R.id.updateBtn);

        editTextBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        updateBtn.setOnClickListener(v -> updateUserProfile());



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void updateUserProfile() {
        String name = Objects.requireNonNull(editTextName.getText()).toString().trim();
        String lastName = Objects.requireNonNull(editTextLName.getText()).toString().trim();
        String birtDate = Objects.requireNonNull(editTextBirthDate.getText()).toString().trim();
        String nationalId = Objects.requireNonNull(editTextNationalId.getText()).toString().trim();
        String institution = Objects.requireNonNull(editTextInstitution.getText()).toString().trim();
        String homeAddress = Objects.requireNonNull(editTextHomeAddress.getText()).toString().trim();
        String course = Objects.requireNonNull(editTextCourse.getText()).toString().trim();
        String interests = Objects.requireNonNull(editTextInterests.getText()).toString().trim();
        String gender = Objects.requireNonNull(editTextGender.getText()).toString().trim();
        String studentNumber = Objects.requireNonNull(editTextStudentNumber.getText()).toString().trim();

        Map<String, Object> userData = new HashMap<>();
        if (name != null){
            userData.put("name", name);
        }
        if (lastName != null) {
            userData.put("LName", lastName);
        }
        if (birtDate != null) {
            userData.put("birth_date", birtDate);
        }
        if (nationalId != null) {
            userData.put("national_id", nationalId);
        }
        if (institution != null) {
            userData.put("institution", institution);
        }
        if (homeAddress != null) {
            userData.put("home_address", homeAddress);
        }
        if (course != null) {
            userData.put("course", course);
        }
        if (interests != null) {
            userData.put("interests", interests);
        }
        if (gender != null) {
            userData.put("gender", gender);
        }
        if (studentNumber != null) {
            userData.put("student_number", studentNumber);
        }

        DocumentReference userRef = db.collection("users").document(userId);
        userRef.update(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(updateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(updateProfile.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(updateProfile.this, "Failed to update profile: " + email +e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Update the EditText with the selected date
                        editTextBirthDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                },
                year, month, dayOfMonth);

        // Set the maximum date to today's date
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }
}

