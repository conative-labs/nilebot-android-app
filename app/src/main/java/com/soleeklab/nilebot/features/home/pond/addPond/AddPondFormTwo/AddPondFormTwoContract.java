package com.soleeklab.nilebot.features.home.pond.addPond.AddPondFormTwo;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.GetPondsByFarmIDQuery;
import com.soleeklab.nilebot.type.IFishTypes;

import java.util.ArrayList;

public interface AddPondFormTwoContract {

    interface View extends BaseView<Presenter> {

        void removeErrors();

        void showInvalidWaterSource();

        void showInvalidFishType();

        void showInvalidFishTypeCount();

        void showInvalidFishTypeLarge();

        void showInvalidFishFeedType();

        void initWaterSources(ArrayList<String> waterSources);

        void initFishTypes(ArrayList<String> fishTypes);

        void initFishFeedTypes(ArrayList<String> fishFeedTypes);

        void addPondSuccess(boolean isAddMore);
    }


    interface Presenter extends BasePresenter<View> {

        void getWaterSources();

        void getFishTypes();

        void getFishFeedTypes();

        void addPond(boolean isAddMore, ArrayList<String> waterSources, ArrayList<IFishTypes> fishTypes, String feedType);

        void editPond(String pondId, ArrayList<String> waterSources, ArrayList<IFishTypes> fishTypes, String feedType);


    }
}
