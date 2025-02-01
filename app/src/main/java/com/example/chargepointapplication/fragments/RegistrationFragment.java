package com.example.chargepointapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chargepointapplication.DatabaseHelper;
import com.example.chargepointapplication.MainActivity;
import com.example.chargepointapplication.Models.User;
import com.example.chargepointapplication.R;

public class RegistrationFragment extends Fragment {
    private EditText emailEditText, passwordEditText, usernameEditText;
    private Spinner roleSpinner;
    private Button signupButton;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        emailEditText = rootView.findViewById(R.id.emailEditText);
        passwordEditText = rootView.findViewById(R.id.passwordEditText);
        usernameEditText = rootView.findViewById(R.id.usernameEditText);
        roleSpinner = rootView.findViewById(R.id.roleSpinner);
        signupButton = rootView.findViewById(R.id.registerButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.role_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        databaseHelper = new DatabaseHelper(getContext());

        signupButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(username)) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                User newUser = new User(email, password, username, role);
                databaseHelper.addUser(newUser);

                Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).showLoginFragment();  // Go back to login
            }
        });

        return rootView;
    }
}

