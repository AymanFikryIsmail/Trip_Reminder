package com.iti.android.tripapp.screens;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.iti.android.tripapp.R;

public class RegisterActivity extends AppCompatActivity {

    Button register ;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText mEmailField = findViewById(R.id.editTextEmail);
        final EditText mPasswordField = findViewById(R.id.editTextPassword);
        final EditText mNameField = findViewById(R.id.editTextName);
        final EditText mMobileField = findViewById(R.id.editTextMobile);

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();
                String email = mEmailField.getText().toString() ;
                String password = mPasswordField.getText().toString() ;
                // String name = mNameField.getText().toString() ;
                //String mobile = mPasswordField.getText().toString() ;

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registered Successfully.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity (i);


                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed , please try again",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }});

            }
        });
    }
}

