package com.soleeklab.nilebot.features.home.pond.pondDetails.PondSensorsReadings;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;
import com.soleeklab.nilebot.GetSensorsReadingByPondIDQuery;
import com.soleeklab.nilebot.data.models.SensorSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PondSensorsReadingsParentContract {

    interface View extends BaseView<Presenter> {

        void initList(Map<String, List<GetSensorsReadingByPondIDQuery.Parameter>> map, ArrayList<SensorSummary> sensorSummaryArrayList);

        void hideDialog();
    }


    interface Presenter extends BasePresenter<View> {

        void getSensorsByPondId(String fromTime, String toTime, ArrayList<String> sensorsIds, boolean isDialog);
    }
}
