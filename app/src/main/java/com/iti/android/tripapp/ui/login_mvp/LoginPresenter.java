package com.iti.android.tripapp.ui.login_mvp;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by ayman on 2019-02-24.
 */

public interface LoginPresenter {

    void handleLogin(String email, String password, FirebaseAuth mAuth,Context context);
}
