package com.iti.android.tripapp.screens;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.iti.android.tripapp.R;

public class SignInActivity extends AppCompatActivity {

    Button btnSignIn ;
    Button btnRegister ;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnSignIn = findViewById(R.id.signIn);
        btnRegister = findViewById(R.id.createAccount);
        final EditText mEmailField = findViewById(R.id.editTextEmail);
        final EditText mPasswordField = findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();



        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailField.getText().toString() ;
                String password = mPasswordField.getText().toString() ;

                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(SignInActivity.this , new  OnCompleteListener<AuthResult>()
                {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(SignInActivity.this, "SignedIn Successfully.",
                                    Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity (i);


                        } else {
                            Toast.makeText(SignInActivity.this, "Authentication failed , please try again",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                });

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity (i);
            }
        });

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null).
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
