package com.iti.android.tripapp.ui.register_mvp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.iti.android.tripapp.R;
import com.iti.android.tripapp.model.UserDTO;
import com.iti.android.tripapp.ui.main_mvp.MainActivity;
import com.iti.android.tripapp.utils.PrefManager;
import com.iti.android.tripapp.view_model.LoginViewModel;

public class RegisterActivity extends AppCompatActivity implements RegisterView {
    private RegisterPresenter presenter;
    Button register ;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    PrefManager prefManager;
    private LoginViewModel loginViewModel;
//    private ActivityRegisterBinding  binding;
     EditText  mEmailField , mPasswordField , mNameField ,mMobileField ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        prefManager=new PrefManager(this);
        initializeView();

//        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
//        binding = DataBindingUtil.setContentView(RegisterActivity.this, R.layout.activity_register);
////        binding.setLifecycleOwner(this);
//        binding.setLoginViewModel(loginViewModel);
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


    }
    private void initializeView()
    {
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
        progressBar = findViewById(R.id.progress);

    }
    void register(){
        mAuth = FirebaseAuth.getInstance();
        final String email =mEmailField.getText().toString();// binding.editTextEmail.getText().toString() ;
        final String password = mPasswordField.getText().toString();//binding.editTextPassword.getText().toString() ;
         final String name = mNameField.getText().toString() ;
        final String mobile  = mPasswordField.getText().toString() ;
        UserDTO user=new UserDTO(name,email,password,mobile);
        showProgress();
        presenter.handleRegister(user, mAuth);
       //                    UserDTO user=new UserDTO(currentUser.getUid(),binding.editTextName.getText().toString(),email,password
//                            ,binding.editTextMobile.getText().toString(),"");
    }
    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void showValidationErrorMsg() {
        Toast.makeText(RegisterActivity.this, "Complete All fields correctly",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerSuccessFully() {
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity (i);
        finish();
    }

    @Override
    public void registerFail() {
        Toast.makeText(RegisterActivity.this, "Authentication failed , please try again",
                Toast.LENGTH_SHORT).show();

    }
}

