package com.iti.android.tripapp.ui.login_mvp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ayman on 2019-02-24.
 */

public class LoginPresenterImpl implements LoginPresenter
{
    private LoginView loginView;
    public LoginPresenterImpl(LoginView loginView)
    {
        this.loginView = loginView;
    }
    @Override
    public void handleLogin(String email, String password, FirebaseAuth mAuth, Context context)
    {
        if ((TextUtils.isEmpty(email) || TextUtils.isEmpty(password)))
        {
            loginView.showValidationErrorMsg();
            loginView.hideProgress();

        }
        else {

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener((Activity) context, new  OnCompleteListener<AuthResult>()
            {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loginView.loginSuccessFully();
                        loginView.hideProgress();

                    } else {
                       loginView.loginFail();
                        loginView.hideProgress();

                    }
                }

            });
            }

    }
}