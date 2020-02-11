package com.soleeklab.nilebot.features.home.farms;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.GetFarmsQuery;

import java.util.List;

public interface FarmsContract {


    interface View extends BaseView<Presenter> {

        void initFarms(List<GetFarmsQuery.Farm> farms);

        void hideDialog();

        void logoutUser();
    }


    interface Presenter extends BasePresenter<View> {

        void getFarms();

        void removeFarm(String id);


    }
}
