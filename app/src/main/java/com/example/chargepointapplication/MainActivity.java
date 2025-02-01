package com.example.chargepointapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chargepointapplication.fragments.LoginFragment;
import com.example.chargepointapplication.fragments.RegistrationFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
            fragmentTransaction.commit();
        }
    }

    public void showRegistrationFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new RegistrationFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
  public void showLoginFragment() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void showDashboardActivity(String role) {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        intent.putExtra("USER_ROLE", role);  // Pass user role (user/admin)
        startActivity(intent);
        finish();
    }
}
