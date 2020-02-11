package com.soleeklab.nilebot.features.auth.signup;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;

public interface SignUpContract {

    interface View extends BaseView<Presenter> {

        void showInvalidName();

        void showInvalidPhone();

        void showInvalidEmail();

        void showInvalidPassword();

        void showInvalidCinfirmPassword();

        void showAcceptTerms();

        void removeErrors();

        void navigateToLogin();

    }

    interface Presenter extends BasePresenter<View> {

        void signup(String name, String phone, String email, String country, String password, String confirmPassword, boolean acceptTerms);

        void terms();

    }
}
