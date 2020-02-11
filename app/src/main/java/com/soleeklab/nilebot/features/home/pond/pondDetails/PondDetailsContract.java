package com.soleeklab.nilebot.features.home.pond.pondDetails;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.GetSensorsByPondIDQuery;

import java.util.List;

public interface PondDetailsContract {


    interface View extends BaseView<Presenter> {

        void initSensors(List<GetSensorsByPondIDQuery.Parameter> sensors);

        void refresh();

        void successDeleteSensor();
    }


    interface Presenter extends BasePresenter<View> {

        void getSensors(String pondId, boolean showProgress);

    }
}
