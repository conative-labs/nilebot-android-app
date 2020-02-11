package com.soleeklab.nilebot.features.home.pond.addPond.addPondFormOne;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.data.api.AddPondData;

import java.util.ArrayList;

public interface AddPondFormOneContract {

    interface View extends BaseView<Presenter> {

        void removeErrors();

        void showInvalidPondName();

        void showInbalidPondType();

        void showInvalidLength();
        void showLargeLength();

        void showInvalidWidth();
        void showLargeWidth();

        void showInvalidHeight();
        void showLargeHeight();


        void goToNextForm(AddPondData addPondData);

        void initPondTypes(ArrayList<String> pondTypesList);
    }


    interface Presenter extends BasePresenter<View> {

        void getPondTypes();

        void nextPond(String farmId, String pondName, String pondType, boolean isRectangle, Float length, Float width, Float Height, boolean equipment);
    }
}
