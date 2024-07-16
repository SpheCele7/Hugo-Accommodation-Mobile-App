package com.example.studentaccomidation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studentaccomidation.fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginOptionsActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_options);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            if (user != null) {
                Intent intent = new Intent(LoginOptionsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            Button button=findViewById(R.id.skipBtn);
           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent intent=new Intent(LoginOptionsActivity.this,MainActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
               }
           });
            Button buttonSU=findViewById(R.id.button);
            buttonSU.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(LoginOptionsActivity.this,Register.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

            Button buttonL=findViewById(R.id.Loginbutton);
            buttonL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(LoginOptionsActivity.this,Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });



            return insets;


        });
    }
}