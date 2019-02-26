package com.iti.android.tripapp.ui.login_mvp;

/**
 * Created by ayman on 2019-02-24.
 */

public interface LoginView {
    void showProgress();

    void hideProgress();

    void showValidationErrorMsg();
    void loginSuccessFully();
    void loginFail();
}
