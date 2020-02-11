package com.soleeklab.nilebot.features.home.farms.addFarm;

import com.soleeklab.nilebot.BasePresenter;
import com.soleeklab.nilebot.BaseView;

import java.util.ArrayList;

public interface AddFarmContract {


    interface View extends BaseView<Presenter> {

        void removeErrors();

        void showInvalidFarmName();

        void showInbalidFarmType();

        void showInvalidFarmAddress();

        void showInvalidFarmLocation();

        void showInvalidFarmArea();

        void showLargeFarmArea();

        void addFarmSuccress();

        void initFarmTypes(ArrayList<String> farmTypesList);

        void initAreaUnitTypes(ArrayList<String> areaUnitTypes);

        void showRequiredunit();
    }


    interface Presenter extends BasePresenter<View> {

        void getFarmTypes();

        void getUnitAraes();

        void addFarm(String farmName,
                     String farmType,
                     float area,
                     String areaUnit,
                     String lat,
                     String lng,
                     String address,
                     String timezone
        );

        void editFarm(String id,
                      String farmName,
                      String farmType,
                      float area,
                      String areaUnit,
                      String lat,
                      String lng,
                      String address,
                      String timezone
        );


    }
}
