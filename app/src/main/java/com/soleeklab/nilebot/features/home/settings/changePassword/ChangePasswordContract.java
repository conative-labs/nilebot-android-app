package com.soleeklab.nilebot.features.home.settings.changePassword;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;

public interface ChangePasswordContract {

    interface View extends BaseView<Presenter> {

        void showInvalidOldPassword();

        void showInvalidNewPassword();

        void showInvalidConfirmPassword();

        void removeErrors();

        void successUpdates(String msg);
    }


    interface Presenter extends BasePresenter<View> {

        void updatePassword(String oldPassword, String newPassword, String confirmNewPassword);

    }

}
