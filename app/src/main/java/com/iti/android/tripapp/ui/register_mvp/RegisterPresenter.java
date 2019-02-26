package com.iti.android.tripapp.ui.register_mvp;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.iti.android.tripapp.model.UserDTO;

/**
 * Created by ayman on 2019-02-24.
 */

public interface RegisterPresenter {

    void handleRegister(UserDTO user, FirebaseAuth mAuth);
}
