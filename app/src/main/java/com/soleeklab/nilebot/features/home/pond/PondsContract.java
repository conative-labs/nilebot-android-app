package com.soleeklab.nilebot.features.home.pond;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.GetPondsByFarmIDQuery;

import java.util.ArrayList;
import java.util.List;

public interface PondsContract {


    interface View extends BaseView<Presenter> {

        void initPondTypes(ArrayList<String> pondTypesList);

        void initPonds(List<GetPondsByFarmIDQuery.Pond> farms);

        void hideDialog();


    }


    interface Presenter extends BasePresenter<View> {

        void getPondTypes();

        void getPonds(String farmId);

        void removePond(String pondId, String farmId);

    }
}
