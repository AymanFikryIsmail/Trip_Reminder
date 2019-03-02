package com.iti.android.tripapp.view_model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.iti.android.tripapp.model.UserDTO;

/**
 * Created by ayman on 2019-02-22.
 */

public class LoginViewModel extends ViewModel {
//
    public LiveData<String> EmailAddress = new MutableLiveData<>();
    public LiveData<String> Password = new MutableLiveData<>();
    public LiveData<String> phone = new MutableLiveData<>();
    public LiveData<String> name = new MutableLiveData<>();
        private MutableLiveData<UserDTO> userMutableLiveData;
//
    public  LiveData<UserDTO> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public void onClick(View view) {

//        UserDTO loginUser = new UserDTO(name.get(),EmailAddress.get(), Password.get(), ic_phone.get()
//        );
//        userMutableLiveData.set(loginUser);

    }


}
