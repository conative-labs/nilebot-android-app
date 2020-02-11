package com.soleeklab.nilebot.features.home.farms.addFarm;


import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.AddFarmMutation;
import com.soleeklab.nilebot.ModifyFarmMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.AreaUnitType;
import com.soleeklab.nilebot.type.FarmType;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

public class AddFarmPresenter implements AddFarmContract.Presenter {


    private AddFarmContract.View mView;

    @Inject
    LocalRepo localRepo;

    @Inject
    public AddFarmPresenter() {
    }

    @Override
    public void registerView(AddFarmContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {
        this.mView = DUMMY_VIEW;
    }

    @Override
    public void start() {
        getFarmTypes();
        getUnitAraes();
    }

    @Override
    public void getFarmTypes() {

        ArrayList<String> farmTypesList = new ArrayList<>();

        for (FarmType farmType : FarmType.values())
            farmTypesList.add(farmType.rawValue());

        mView.initFarmTypes(farmTypesList);
    }

    @Override
    public void getUnitAraes() {

        ArrayList<String> areaUnitTypes = new ArrayList<>();

        for (AreaUnitType areaUnitType : AreaUnitType.values())
            areaUnitTypes.add(areaUnitType.rawValue());

        mView.initAreaUnitTypes(areaUnitTypes);

    }


    @Override
    public void addFarm(String farmName, String farmType, float area, String areaUnit, String lat, String lng, String address, String timezone) {

        mView.showProgress();
        mView.removeErrors();

        boolean isValid = true;
        if (farmName.length() < 2) {
            mView.showInvalidFarmName();
            isValid = false;
        } else if (farmType.length() < 2) {
            mView.showInbalidFarmType();
            isValid = false;
        } else if (lat.equals("0.0")) {
            mView.showInvalidFarmLocation();
            isValid = false;
        } else if (area == 0) {
            mView.showInvalidFarmArea();
            isValid = false;
        } else if (area > 999999) {
            mView.showLargeFarmArea();
            isValid = false;
        } else if (areaUnit.equals("")) {
            mView.showRequiredunit();
            isValid = false;
        }

        if (!isValid) {
            mView.hideProgress();
            return;
        }


        AddFarmMutation addFarmMutation = AddFarmMutation.builder()
                .name(farmName)
                .type(FarmType.safeValueOf(farmType))
                .area((double) area)
                .areaUnit(AreaUnitType.safeValueOf(areaUnit))
                .latitude(lat)
                .longitude(lng)
                .address("")
                .timezome(timezone)
                .build();

        ApolloClientBuilder.getApolloClient(true).mutate(addFarmMutation).enqueue(new ApolloCall.Callback<AddFarmMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<AddFarmMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().addFarm() != null) {
                        mView.addFarmSuccress();
                    } else {
                        mView.showAlert(R.string.error_signup);
                    }

                } else
                    mView.showAlert(R.string.server_error);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mView.hideProgress();
                mView.showAlert(R.string.error_occured);
            }
        });


    }

    @Override
    public void editFarm(String id, String farmName, String farmType, float area, String areaUnit, String lat, String lng, String address, String timezone) {
        mView.showProgress();
        mView.removeErrors();
        boolean isValid = true;
        if (farmName.length() < 2) {
            mView.showInvalidFarmName();
            isValid = false;
        } else if (farmType.length() < 2) {

            mView.showInbalidFarmType();
            isValid = false;
        }

//        else if (address.length() < 2) {
//            mView.showInvalidFarmAddress();
//            isValid = false;
//        }
//
        else if (lat.equals("0.0")) {
            mView.showInvalidFarmLocation();
            isValid = false;

        } else if (area == 0) {
            mView.showInvalidFarmArea();
            isValid = false;
        } else if (area > 999999) {
            mView.showLargeFarmArea();
            isValid = false;
        } else if (areaUnit.equals("")) {
            mView.showRequiredunit();
            isValid = false;
        }

        if (!isValid) {
            mView.hideProgress();
            return;
        }


        ModifyFarmMutation modifyFarmMutation = ModifyFarmMutation.builder()
                .id(id)
                .name(farmName)
                .type(FarmType.safeValueOf(farmType))
                .area((double) area)
                .areaUnit(AreaUnitType.safeValueOf(areaUnit))
                .latitude(lat)
                .longitude(lng)
                .address(address)
                .timezome(timezone)
                .build();

        ApolloClientBuilder.getApolloClient(true).mutate(modifyFarmMutation).enqueue(new ApolloCall.Callback<ModifyFarmMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<ModifyFarmMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().modifyFarm().equals(Status.SUCCESS)) {
                        mView.addFarmSuccress();
                    } else {
                        mView.showAlert(localRepo.getTranslation(response.data().modifyFarm().rawValue()));
                    }

                } else
                    mView.showAlert(R.string.server_error);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mView.hideProgress();
                mView.showAlert(R.string.error_occured);
            }
        });


    }


    private AddFarmContract.View DUMMY_VIEW = new AddFarmContract.View() {
        @Override
        public void removeErrors() {

        }

        @Override
        public void showInvalidFarmName() {

        }

        @Override
        public void showInbalidFarmType() {

        }

        @Override
        public void showInvalidFarmAddress() {

        }

        @Override
        public void showInvalidFarmLocation() {

        }

        @Override
        public void showInvalidFarmArea() {

        }

        @Override
        public void showLargeFarmArea() {

        }

        @Override
        public void addFarmSuccress() {

        }

        @Override
        public void initFarmTypes(ArrayList<String> farmTypesList) {

        }

        @Override
        public void initAreaUnitTypes(ArrayList<String> areaUnitTypes) {

        }

        @Override
        public void showRequiredunit() {

        }

        @Override
        public void setPresenter(AddFarmContract.Presenter presenter) {

        }

        @Override
        public void showProgress() {

        }

        @Override
        public void hideProgress() {

        }

        @Override
        public void showError(String error) {

        }

        @Override
        public void showAlert(String message) {

        }

        @Override
        public void showAlert(int message) {

        }

        @Override
        public void showNoConnectionAlert() {

        }

        @Override
        public void showError() {

        }

        @Override
        public void showNoConnection() {

        }

        @Override
        public void showToast(String message) {

        }
    };
}
