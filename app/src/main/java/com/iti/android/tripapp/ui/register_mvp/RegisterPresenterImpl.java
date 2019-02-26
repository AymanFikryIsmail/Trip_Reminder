package com.iti.android.tripapp.ui.register_mvp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iti.android.tripapp.helpers.FireBaseHelper;
import com.iti.android.tripapp.model.UserDTO;
import com.iti.android.tripapp.utils.PrefManager;

/**
 * Created by ayman on 2019-02-24.
 */

public class RegisterPresenterImpl implements RegisterPresenter
{
    private RegisterView registerView;
    private PrefManager prefManager;
    private Context context;
    public RegisterPresenterImpl(RegisterView registerView)
    {
        this.registerView = registerView;
        this.context= (Context) registerView;
        prefManager=new PrefManager(context);
    }
    @Override
    public void handleRegister(final UserDTO user, final FirebaseAuth mAuth)
    {
        if ((TextUtils.isEmpty(user.getEmail()) || TextUtils.isEmpty(user.getPassword())))
        {
            registerView.showValidationErrorMsg();
            registerView.hideProgress();
        }
        else {

            mAuth.createUserWithEmailAndPassword(user.getEmail() ,user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Registered Successfully.",
                                Toast.LENGTH_SHORT).show();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        user.setId(currentUser.getUid());
                        FireBaseHelper fireBaseHelper =new FireBaseHelper();
                        fireBaseHelper.addUserToFirebase(user);
                        prefManager.setUserData(user);
                        prefManager.setUserId(currentUser.getUid());
                        registerView.registerSuccessFully();
                        registerView.hideProgress();
                    } else {
                        registerView.registerFail();
                        registerView.hideProgress();

                    }
                }});

            }

    }
}