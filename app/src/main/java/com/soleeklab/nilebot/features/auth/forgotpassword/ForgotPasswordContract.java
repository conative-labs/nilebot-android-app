package com.soleeklab.nilebot.features.auth.forgotpassword;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.features.auth.login.LoginContract;

public interface ForgotPasswordContract {

    interface View extends BaseView<Presenter> {

        void showInvalidPhone();

        void handleWatcher();

        void removeErrors();

        void navigateToConfirmcode();
    }


    interface Presenter extends BasePresenter<View> {

        void forgotPassword(String prefix,String credential);


    }
}
