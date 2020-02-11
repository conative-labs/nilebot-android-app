package com.soleeklab.nilebot.features.home.settings.profile;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.GetUserQuery;


public interface ProfileContract {

    interface View extends BaseView<Presenter> {

        void setUserData(GetUserQuery.User userData);
    }


    interface Presenter extends BasePresenter<View> {

        void getUser();
    }
}
