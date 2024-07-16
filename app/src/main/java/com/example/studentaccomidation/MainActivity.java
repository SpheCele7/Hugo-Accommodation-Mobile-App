package com.example.studentaccomidation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.studentaccomidation.databinding.ActivityMainBinding;
import com.example.studentaccomidation.fragments.ApplicationsFragment;
import com.example.studentaccomidation.fragments.NotificationsFragment;
import com.example.studentaccomidation.fragments.HomeFragment;
import com.example.studentaccomidation.fragments.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    String email;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            email = user.getEmail();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference userInfo = db.collection("users");
            userInfo.whereEqualTo("email", email)
                    .whereEqualTo("role", "Admin")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // If there is a user document with the given email and role "Admin"
                                    // Redirect to AdminActivity
                                    Intent intent = new Intent(MainActivity.this, Admins.class);
                                    startActivity(intent);
                                    finish(); // Optionally, finish the current activity
                                    // Exit the loop as we found an admin user
                                }
                            }
                        }
                    });
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showHomeFragment();
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId= menuItem.getItemId();
                if (itemId == R.id.item_home){
                    showHomeFragment();

                }else if (itemId == R.id.item_notifications){
                    notificationsFragment();

                }else if (itemId == R.id.item_favourite){
                    showApplicationsFragment();

                }else if (itemId == R.id.item_profile){
                    showProfileFragment();

                }
                return true;
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void showHomeFragment(){
        binding.toolbarTitleTv.setText("Home");
        HomeFragment homeFragment= new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(), homeFragment,"HomeFragment");
        fragmentTransaction.commit();

    }
    private void notificationsFragment(){
        binding.toolbarTitleTv.setText("Notifications");
        NotificationsFragment notificationsFragment= new NotificationsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(), notificationsFragment,"notificationsFragment");
        fragmentTransaction.commit();

    }
    private void showApplicationsFragment(){
        binding.toolbarTitleTv.setText("Applications");
        ApplicationsFragment applicationsFragment = new ApplicationsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(), applicationsFragment,"ApplicationsFragment");
        fragmentTransaction.commit();

    }
    private void showProfileFragment(){
        binding.toolbarTitleTv.setText("Profile");
        ProfileFragment profileFragment= new ProfileFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentsFl.getId(), profileFragment,"ProfileFragment");
        fragmentTransaction.commit();
    }
}
