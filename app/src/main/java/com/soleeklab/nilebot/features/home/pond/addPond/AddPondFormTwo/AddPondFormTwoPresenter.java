package com.soleeklab.nilebot.features.home.pond.addPond.AddPondFormTwo;

import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.AddPondMutation;

import com.soleeklab.nilebot.GetPondsByFarmIDQuery;
import com.soleeklab.nilebot.ModifyPondMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.models.MyCache;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.FishFeedType;

import com.soleeklab.nilebot.type.FishTypeEnum;
import com.soleeklab.nilebot.type.IFishTypes;
import com.soleeklab.nilebot.type.PondType;
import com.soleeklab.nilebot.type.Status;
import com.soleeklab.nilebot.type.WaterSourceType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

public class AddPondFormTwoPresenter implements AddPondFormTwoContract.Presenter {


    private AddPondFormTwoContract.View mView;

    @Inject
    MyCache myCache;

    @Inject
    LocalRepo localRepo;

    @Inject
    public AddPondFormTwoPresenter() {
    }

    @Override
    public void registerView(AddPondFormTwoContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {

    }

    @Override
    public void start() {

        getWaterSources();
        getFishTypes();
        getFishFeedTypes();
    }

    @Override
    public void getWaterSources() {
        ArrayList<String> pondTypesList = new ArrayList<>();

        for (WaterSourceType waterSourceType : WaterSourceType.values())
            pondTypesList.add(waterSourceType.rawValue());

        mView.initWaterSources(pondTypesList);
    }

    @Override
    public void getFishTypes() {
        ArrayList<String> fishTypesList = new ArrayList<>();

        for (FishTypeEnum fishTypeEnum : FishTypeEnum.values()) {
            fishTypesList.add(fishTypeEnum.rawValue());
        }

        mView.initFishTypes(fishTypesList);
    }

    @Override
    public void getFishFeedTypes() {
        ArrayList<String> fishFeedTypeList = new ArrayList<>();

        for (FishFeedType fishFeedType : FishFeedType.values())
            fishFeedTypeList.add(fishFeedType.rawValue());

        mView.initFishFeedTypes(fishFeedTypeList);
    }

    @Override
    public void addPond(boolean isAddMore, ArrayList<String> waterSources, ArrayList<IFishTypes> fishTypes, String feedType) {
        mView.showProgress();

        mView.removeErrors();
        boolean isValid = true;
        boolean isFishTypeCountThere = true;
        boolean isFishTypeCountLarge = true;


        for (IFishTypes iFishTypes : fishTypes) {
            if (iFishTypes.count() == 0) {
                isFishTypeCountThere = false;
                break;
            }
        }

        for (IFishTypes iFishTypes : fishTypes) {
            if (iFishTypes.count() > 999999) {
                isFishTypeCountLarge = false;
                break;
            }
        }

        if (waterSources.size() == 0) {
            mView.showInvalidWaterSource();
            isValid = false;
        } else if (fishTypes.size() == 0) {
            mView.showInvalidFishType();
            isValid = false;
        } else if (!isFishTypeCountThere) {
            mView.showInvalidFishTypeCount();
            isValid = false;
        } else if (!isFishTypeCountLarge) {
            mView.showInvalidFishTypeLarge();
            isValid = false;
        } else if (feedType.length() < 2) {
            isValid = false;
            mView.showInvalidFishFeedType();
        }

        if (!isValid) {
            mView.hideProgress();
            return;
        }

        ArrayList<WaterSourceType> waterSourceTypeArrayList = new ArrayList<>();
        for (String waterSource : waterSources)
            waterSourceTypeArrayList.add(WaterSourceType.safeValueOf(waterSource));


//
        AddPondMutation addPondMutation = AddPondMutation.builder()
                .farmID(myCache.getAddPondData().getFarmId())
                .name(myCache.getAddPondData().getPondName())
                .type(PondType.safeValueOf(myCache.getAddPondData().getPondType()))
                .length((double) myCache.getAddPondData().getLength())
                .width((double) myCache.getAddPondData().getWidth())
                .depth((double) myCache.getAddPondData().getHeight())
                .aerationEquipment(myCache.getAddPondData().isEquipment())
                .waterSources(waterSourceTypeArrayList)
                .fishTypes(fishTypes)
                .fishFeedType(FishFeedType.safeValueOf(feedType))
                .build();

        ApolloClientBuilder.getApolloClient(true).mutate(addPondMutation).enqueue(new ApolloCall.Callback<AddPondMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<AddPondMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().addPond() != null) {
                        mView.addPondSuccess(isAddMore);
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
    public void editPond(String pondId, ArrayList<String> waterSources, ArrayList<IFishTypes> fishTypes, String feedType) {

        mView.showProgress();
        mView.removeErrors();

        boolean isValid = true;
        boolean isFishTypeCountThere = true;


        for (IFishTypes iFishTypes : fishTypes) {
            if (iFishTypes.count() == 0) {
                isFishTypeCountThere = false;
                break;
            }
        }

        if (waterSources.size() == 0) {
            mView.showInvalidWaterSource();
            isValid = false;
        } else if (fishTypes.size() == 0) {
            mView.showInvalidFishType();
            isValid = false;
        } else if (!isFishTypeCountThere) {
            mView.showInvalidFishTypeCount();
            isValid = false;
        } else if (feedType.length() < 2) {
            isValid = false;
            mView.showInvalidFishFeedType();
        }

        if (!isValid) {
            mView.hideProgress();
            return;
        }

        ArrayList<WaterSourceType> waterSourceTypeArrayList = new ArrayList<>();
        for (String waterSource : waterSources)
            waterSourceTypeArrayList.add(WaterSourceType.safeValueOf(waterSource));

        if (!myCache.getAddPondData().isRectangle())
            myCache.getAddPondData().setLength(0f);


        ModifyPondMutation modifyPondMutation = ModifyPondMutation.builder()
                .id(pondId)
                .name(myCache.getAddPondData().getPondName())
                .type(PondType.safeValueOf(myCache.getAddPondData().getPondType()))
                .length((double) myCache.getAddPondData().getLength())
                .width((double) myCache.getAddPondData().getWidth())
                .depth((double) myCache.getAddPondData().getHeight())
                .aerationEquipment(myCache.getAddPondData().isEquipment())
                .waterSources(waterSourceTypeArrayList)
                .fishTypes(fishTypes)
                .fishFeedType(FishFeedType.safeValueOf(feedType))
                .build();

        ApolloClientBuilder.getApolloClient(true).mutate(modifyPondMutation).enqueue(new ApolloCall.Callback<ModifyPondMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<ModifyPondMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().modifyPond().equals(Status.SUCCESS)) {
                        mView.addPondSuccess(false);
                    } else {
                        mView.showAlert(localRepo.getTranslation(response.data().modifyPond().rawValue()));
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
}
