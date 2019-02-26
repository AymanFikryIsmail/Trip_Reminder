package com.iti.android.tripapp.ui.register_mvp;

/**
 * Created by ayman on 2019-02-24.
 */

public interface RegisterView {
    void showProgress();

    void hideProgress();

    void showValidationErrorMsg();
    void registerSuccessFully();
    void registerFail();
}
