package com.example.studentaccomidation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextInputEditText editTextName,editTextLName,editTextEmail,editTextPassword,editTextConfirmPassword;
    Button buttonReg;
    ProgressBar progressBar;
    TextView clickToLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Register.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextName = findViewById(R.id.name);
        editTextLName = findViewById(R.id.LName);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);
        buttonReg = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progressBar);
        clickToLogin = findViewById(R.id.clickToLogin);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name, LName, email, password, confirmPassword;
                name = String.valueOf(editTextName.getText());
                LName = String.valueOf(editTextLName.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                confirmPassword = String.valueOf(editTextConfirmPassword.getText());

                if (TextUtils.isEmpty(name)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(LName)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter your last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Confirm your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this, "Passwords must be the same", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name", name);
                                    user.put("LName", LName);
                                    user.put("email", email);
                                    db.collection("users")
                                            .add(user)
                                            .addOnCompleteListener(documentReferenceTask -> {
                                                if (documentReferenceTask.isSuccessful()) {
                                                    Toast.makeText(Register.this, "Account created.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // If adding user data to Firestore fails
                                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                }
                                    });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        clickToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}