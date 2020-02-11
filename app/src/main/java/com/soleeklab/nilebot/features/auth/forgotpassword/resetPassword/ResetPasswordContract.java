package com.soleeklab.nilebot.features.auth.forgotpassword.resetPassword;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode.ConfirmCodeContract;

public interface ResetPasswordContract {

    interface View extends BaseView<Presenter> {

        void navigateToLogin(String message);

        void removeErrors();

        void showInvalidPassword();

        void showInvalidConfirmPassword();

        void showInvalidCredtials(String error);
    }


    interface Presenter extends BasePresenter<View> {

        void resetPassword(String prefix, String credential, String code, String password,String confirmPassword);


    }
}
