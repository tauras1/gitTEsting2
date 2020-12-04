package com.example.yaatris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class RegisterCompany extends AppCompatActivity implements View.OnClickListener {

    EditText name, email, phone, address, password, confirmPassword;
    Button register;
    ProgressDialog progressDialog;
    String Name,Email, Password, Address, Phone, RegisterConfirmPassword;
    DatabaseReference mdatabase;
    FirebaseAuth mAuth;
    private Button btnToggleDark;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_company);
        btnToggleDark
                = findViewById(R.id.btnToggleDark);

        SharedPreferences sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn
                = sharedPreferences
                .getBoolean(
                        "isDarkModeOn", false);
        if (isDarkModeOn) {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_YES);
            btnToggleDark.setText(
                    "Light Mode");
        }
        else {
            AppCompatDelegate
                    .setDefaultNightMode(
                            AppCompatDelegate
                                    .MODE_NIGHT_NO);
            btnToggleDark
                    .setText(
                            "Dark Mode");
        }
        btnToggleDark.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View view)
                    {
                        // When user taps the enable/disable
                        // dark mode button
                        if (isDarkModeOn) {

                            // if dark mode is on it
                            // will turn it off
                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_NO);
                            // it will set isDarkModeOn
                            // boolean to false
                            editor.putBoolean(
                                    "isDarkModeOn", false);
                            editor.apply();

                            // change text of Button
                            btnToggleDark.setText(
                                    "Dark Mode");
                        }
                        else {

                            // if dark mode is off
                            // it will turn it on
                            AppCompatDelegate
                                    .setDefaultNightMode(
                                            AppCompatDelegate
                                                    .MODE_NIGHT_YES);

                            // it will set isDarkModeOn
                            // boolean to true
                            editor.putBoolean(
                                    "isDarkModeOn", true);
                            editor.apply();

                            // change text of Button
                            btnToggleDark.setText(
                                    "Light Mode");
                        }
                    }
                });



        //initializing views
        name = (EditText)findViewById(R.id.companyName);
        phone = (EditText)findViewById(R.id.companyPhone);
        email = (EditText)findViewById(R.id.companyEmail);
        address = (EditText)findViewById(R.id.companyAddress);
        password = (EditText)findViewById(R.id.companyPassword);
        confirmPassword = (EditText) findViewById(R.id.companyPasswordConfirm);
        register = (Button)findViewById(R.id.buttonRegCompany);

        //attaching listener to button
        register.setOnClickListener((View.OnClickListener) this);
        progressDialog = new ProgressDialog(this);

        //authentication
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Companies");
    }

    @Override
    public void onClick(View view) {
        //calling register method on click
        registerComp();
    }

    private void registerComp() {

        //getting details from edit texts
        Name = name.getText().toString().trim();
        Phone = phone.getText().toString().trim();
        Email = email.getText().toString().trim();
        Address = address.getText().toString().trim();
        Password = password.getText().toString().trim();
        RegisterConfirmPassword = confirmPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if (TextUtils.isEmpty(Name)) {
            name.setError("Enter company name");
            name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Phone)) {
            phone.setError("Enter phone number");
            phone.requestFocus();
            return;
        }


        if (Phone.length()<10){
            phone.setError("Phone no must be 10 of characters");
            phone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Email)) {
            email.setError("Please enter email");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError(getString(R.string.input_error_email_invalid));
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Password)) {
            password.setError("Enter password");
            password.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Address)) {
            address.setError("Enter Address");
            address.requestFocus();
            return;
        }

        if (Password.length()<6){
            confirmPassword.setError("Password must be longer than 6 characters");
            confirmPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(RegisterConfirmPassword)) {
            confirmPassword.setError("Enter confirmation password");
            confirmPassword.requestFocus();
            return;
        }

        if (!RegisterConfirmPassword.equals(Password))
        {
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
            return;
        }



        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //creating a new company
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Company company = new Company(Name, Phone, Email, Address);

                            FirebaseDatabase.getInstance().getReference("Companies")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(company).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterCompany.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //display a failure message
                                        Toast.makeText(RegisterCompany.this, "Check the credentials again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }
}