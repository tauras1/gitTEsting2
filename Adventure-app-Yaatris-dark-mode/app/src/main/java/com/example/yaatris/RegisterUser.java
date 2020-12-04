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

import java.util.Date;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener  {

    //defining view objects
    EditText name;
    EditText phone;
    EditText editTextEmail;
    EditText editTextPassword, confirmPassword;
    Button buttonSignup;
    ProgressDialog progressDialog;
    String Name,Email,Password, Phone, RegisterConfirmPassword;
    DatabaseReference mdatabase;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        //initializing views
        name = (EditText)findViewById(R.id.editTextTextPersonName);
        phone = (EditText)findViewById(R.id.editTextPhone);
        editTextEmail = (EditText)findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = (EditText)findViewById(R.id.signupPassword);
        buttonSignup = (Button)findViewById(R.id.buttonSignup);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);

        //authentication
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");



    }

    @Override
    public void onClick(View view) {
        //calling register method on click
        registerUser();
    }

    private void registerUser() {

        //getting details from edit texts
        Name = name.getText().toString().trim();
        Phone = phone.getText().toString().trim();
        Email = editTextEmail.getText().toString().trim();
        Password = editTextPassword.getText().toString().trim();
        RegisterConfirmPassword = confirmPassword.getText().toString().trim();


        //checking if email and passwords are empty
        if (TextUtils.isEmpty(Name)) {
            name.setError(getString(R.string.enterName));
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
            editTextEmail.setError("Please enter email");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(Password)) {
            editTextPassword.setError("Enter password");
            editTextPassword.requestFocus();
            return;
        }

        if (Password.length()<6){
            editTextPassword.setError("Password must be longer than 6 characters");
            editTextPassword.requestFocus();
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

        //creating a new user
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(Name, Phone, Email, new Date().getTime());

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //display a failure message
                                        Toast.makeText(RegisterUser.this, "Check the credentials again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });
    }
}



//checking if success
//                        if (task.isSuccessful()) {
//                            //display some message here
//
//                            User user = new User(Name, Phone, Email, new Date().getTime());                           }
//
//                            progressDialog.dismiss();
//                            Toast.makeText(RegisterUser.this, "Successfully registered", Toast.LENGTH_LONG).show();
//                        } else {
//                            //display some message here
//                            Toast.makeText(RegisterUser.this, "Registration Error", Toast.LENGTH_LONG).show();
//                        }