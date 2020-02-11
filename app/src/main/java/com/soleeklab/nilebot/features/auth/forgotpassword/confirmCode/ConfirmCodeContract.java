package com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;

public interface ConfirmCodeContract {


    interface View extends BaseView<Presenter> {

        void startTimer();

        void handleTextWatcher();

        void disableViews();

        void enableViews();

        void navigateToLogin();

        void showInvalidCredtials(String error);

        void resendApiSuccess();

        void verificationSuccess(String message);
    }


    interface Presenter extends BasePresenter<View> {

        void verifyCode(int screentype,String prefix ,String credential, String code);



        void resendCode(int screenType, String prefix,String credential,String name ,String password,String email,String language);



    }
}
