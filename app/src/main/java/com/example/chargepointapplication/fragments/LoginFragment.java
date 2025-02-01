package com.example.chargepointapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chargepointapplication.DatabaseHelper;
import com.example.chargepointapplication.MainActivity;
import com.example.chargepointapplication.R;

public class LoginFragment extends Fragment {
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = rootView.findViewById(R.id.emailEditText);
        passwordEditText = rootView.findViewById(R.id.passwordEditText);
        loginButton = rootView.findViewById(R.id.loginButton);
        registerButton = rootView.findViewById(R.id.registerButton);

        databaseHelper = new DatabaseHelper(getContext());

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            boolean isValid = databaseHelper.validateUser(email, password);

            if (isValid) {
                String role = databaseHelper.getUserRole(email);
                ((MainActivity) getActivity()).showDashboardActivity(role);
            } else {
                Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
        registerButton.setOnClickListener(v -> {
            ((MainActivity) getActivity()).showRegistrationFragment();
        });
        return rootView;
    }
}
