package com.iti.android.tripapp.screens;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.GoogleAuthProvider;
import com.iti.android.tripapp.R;
import com.iti.android.tripapp.utils.PrefManager;

public class SignInActivity extends AppCompatActivity {

    Button btnSignIn ;
    TextView btnRegister ;
    private FirebaseAuth mAuth;
    PrefManager prefManager;

    private static final String TAG = "MainActivity";
    private SignInButton googleSignInButton;
    private GoogleApiClient googleApiClient;
    GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        prefManager=new PrefManager(this);
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

                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            prefManager.setUserId(currentUser.getUid());
                            Toast.makeText(SignInActivity.this, "SignedIn Successfully.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignInActivity.this, MainActivity.class);
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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton = findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account);

            } catch (ApiException e) {
                e.printStackTrace();
            }

        }
    }

    private void handleSignInResult(GoogleSignInAccount account){
        String idToken = account.getIdToken();
        String name = account.getDisplayName();
        String email = account.getEmail();
        // you can store user data to SharedPreference
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if(task.isSuccessful()){
                            Toast.makeText(SignInActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            gotoProfile();
                        }else{
                            Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }




    private void gotoProfile(){
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null).
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

}
