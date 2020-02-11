package com.soleeklab.nilebot.features.auth.login;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;

public interface LoginContract {


    interface View extends BaseView<Presenter> {

        void showInvalidPhone();

        void showInvalidPassword();

        void handleWatcher();

        void navigateToHome();

        void navigateToVerifyPhone();

        void navigateToForgotPassword();

        void showInvalidCredentials(String error);

        void removeErrors();
    }


    interface Presenter extends BasePresenter<View> {

        void login(String prefix,String credential, String passsword);


    }

}
