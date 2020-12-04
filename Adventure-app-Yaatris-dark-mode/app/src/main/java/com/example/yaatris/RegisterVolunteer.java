package com.example.yaatris;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterVolunteer extends AppCompatActivity implements View.OnClickListener {

    EditText name, email, phone, activities,locations, password, confirmPassword;
    Button register;
    ProgressDialog progressDialog;
    String Name,Email, Password, Locations,Activities, Phone, RegisterConfirmPassword;
    DatabaseReference mdatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_volunteer);

        //initializing views
        name = (EditText)findViewById(R.id.volName);
        phone = (EditText)findViewById(R.id.volPhone);
        email = (EditText)findViewById(R.id.volEmail);
        locations = (EditText)findViewById(R.id.volLocations);
        activities = (EditText)findViewById(R.id.volActivities);
        password = (EditText)findViewById(R.id.volPassword);
        confirmPassword = (EditText) findViewById(R.id.volPasswordConfirm);
        register = (Button)findViewById(R.id.volRegisterButton);

        //attaching listener to button
        register.setOnClickListener((View.OnClickListener) this);
        progressDialog = new ProgressDialog(this);

        //authentication
        mAuth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Volunteers");
    }

    @Override
    public void onClick(View view) {
        //calling register method on click
        registerVol();
    }

    private void registerVol() {

        //getting details from edit texts
        Name = name.getText().toString().trim();
        Phone = phone.getText().toString().trim();
        Email = email.getText().toString().trim();
        Locations = locations.getText().toString().trim();
        Activities = activities.getText().toString().trim();
        Password = password.getText().toString().trim();
        RegisterConfirmPassword = confirmPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if (TextUtils.isEmpty(Name)) {
            name.setError("Enter your name");
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

        if (TextUtils.isEmpty(Locations)) {
            locations.setError("Enter locations");
            locations.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Activities)) {
            activities.setError("Enter locations");
            activities.requestFocus();
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
                            Volunteer volunteer = new Volunteer(Name, Phone, Email,Activities, Locations);

                            FirebaseDatabase.getInstance().getReference("Volunteers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(volunteer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterVolunteer.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //display a failure message
                                        Toast.makeText(RegisterVolunteer.this, "Check the credentials again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }
}