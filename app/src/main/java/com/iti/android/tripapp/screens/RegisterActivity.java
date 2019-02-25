package com.iti.android.tripapp.screens;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.iti.android.tripapp.helpers.FireBaseHelper;
import com.iti.android.tripapp.model.UserDTO;
import com.iti.android.tripapp.utils.PrefManager;
import com.iti.android.tripapp.view_model.LoginViewModel;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Button register ;
    private FirebaseAuth mAuth;

    PrefManager prefManager;


    private LoginViewModel loginViewModel;
//    private ActivityRegisterBinding  binding;

     EditText  mEmailField , mPasswordField , mNameField ,mMobileField ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
//        binding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);
////        binding.setLifecycleOwner(this);
//        binding.setLoginViewModel(loginViewModel);

        prefManager=new PrefManager(this);

//        loginViewModel.getUser().observe(this, new Observer<UserDTO>() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onChanged(@Nullable UserDTO loginUser) {
//                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getEmail())) {
//                    binding.editTextEmail.setError("Enter an E-Mail Address");
//                    binding.editTextEmail.requestFocus();
//                } else if (!loginUser.isEmailValid()) {
//                    binding.editTextEmail.setError("Enter a Valid E-mail Address");
//                    binding.editTextEmail.requestFocus();
//                } else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).getPassword())) {
//                    binding.editTextPassword.setError("Enter a Password");
//                    binding.editTextPassword.requestFocus();
//                } else if (!loginUser.isPasswordLengthGreaterThan5()) {
//                    binding.editTextPassword.setError("Enter at least 6 Digit password");
//                    binding.editTextPassword.requestFocus();
//                } else {
//                    register();
//                }
//            }
//        });

        mEmailField = findViewById(R.id.editTextEmail);
          mPasswordField = findViewById(R.id.editTextPassword);
          mNameField = findViewById(R.id.editTextName);
          mMobileField = findViewById(R.id.editTextMobile);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
register();
            }
        });
    }

    void register(){
        mAuth = FirebaseAuth.getInstance();
        final String email =mEmailField.getText().toString();// binding.editTextEmail.getText().toString() ;
        final String password = mPasswordField.getText().toString();//binding.editTextPassword.getText().toString() ;
         final String name = mNameField.getText().toString() ;
        final String mobile  = mPasswordField.getText().toString() ;

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registered Successfully.",
                            Toast.LENGTH_SHORT).show();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    UserDTO user=new UserDTO(name,email,password,mobile);
//                    UserDTO user=new UserDTO(currentUser.getUid(),binding.editTextName.getText().toString(),email,password
//                            ,
//                            binding.editTextMobile.getText().toString(),""
//                            );
                    FireBaseHelper fireBaseHelper =new FireBaseHelper();
                    fireBaseHelper.addUserToFirebase(user);
                    prefManager.setUserData(user);
                    prefManager.setUserId(currentUser.getUid());
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity (i);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Authentication failed , please try again",
                            Toast.LENGTH_SHORT).show();

                }
            }});
    }
}

