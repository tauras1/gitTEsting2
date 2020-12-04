package com.example.yaatris;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Account extends Fragment implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;

    private EditText Email;
    private EditText Password;
    private Button LogInButton;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    String email, password;
    ProgressDialog dialog;
    public static final String userEmail="";

    public Account() {
        // Required empty public constructor
    }

    public static Account newInstance() {
        Account fragment = new Account();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        //initializing views
        Email = (EditText) v.findViewById(R.id.loginEmail);
        Password = (EditText) v.findViewById(R.id.loginPassword);
        LogInButton= (Button) v.findViewById(R.id.buttonLogin);





        dialog = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //attaching listener to button
//        LogInButton.setOnClickListener((View.OnClickListener) this);

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Calling EditText is empty or no method.
                userSign();
            }
        });


        ImageButton userReg = (ImageButton) v.findViewById(R.id.userImgRegBtn);
        userReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), RegisterUser.class);
                startActivity(in);
            }
        });

        ImageButton companyReg = (ImageButton) v.findViewById(R.id.compRegImgBtn);
        companyReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in2 = new Intent(getActivity(), RegisterCompany.class);
                startActivity(in2);
            }
        });

        ImageButton volReg = (ImageButton) v.findViewById(R.id.volRegImgBtn);
        volReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in3 = new Intent(getActivity(), RegisterVolunteer.class);
                startActivity(in3);
            }
        });


        return v;
    }

    private void userSign() {
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Enter the correct Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Enter the correct password", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Logging in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    @Override
    public void onClick(View view) {
        //calling register method on click
    }

}