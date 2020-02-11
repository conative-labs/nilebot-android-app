package com.soleeklab.nilebot.features.home.pond.addPond.addPondFormOne;

import com.soleeklab.nilebot.data.api.AddPondData;
import com.soleeklab.nilebot.type.PondType;

import java.util.ArrayList;

import javax.inject.Inject;

public class AddPondFormOnePresenter implements AddPondFormOneContract.Presenter {


    private AddPondFormOneContract.View mView;

    @Inject
    public AddPondFormOnePresenter() {
    }

    @Override
    public void registerView(AddPondFormOneContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {
        this.mView = DUMMY_VIEW;
    }

    @Override
    public void start() {
        getPondTypes();
    }

    @Override
    public void getPondTypes() {
        ArrayList<String> pondTypesList = new ArrayList<>();

        for (PondType pondType : PondType.values())
            pondTypesList.add(pondType.rawValue());

        mView.initPondTypes(pondTypesList);
    }

    @Override
    public void nextPond(String farmId, String pondName, String pondType, boolean isRectangle, Float length, Float width, Float Height, boolean equipment) {
        mView.removeErrors();

        boolean isValid = true;
        if (pondName.length() < 1) {
            mView.showInvalidPondName();
            isValid = false;
        } else if (pondType.length() < 2) {
            mView.showInbalidPondType();
            isValid = false;
        } else if (isRectangle && length == 0) {
            isValid = false;
            mView.showInvalidLength();
        } else if (length > 999999) {
            isValid = false;
            mView.showLargeLength();
        } else if (isRectangle && width == 0) {
            isValid = false;
            mView.showInvalidWidth();
        } else if (width > 999999) {
            isValid = false;
            mView.showLargeWidth();
        } else if (Height == 0) {
            isValid = false;
            mView.showInvalidHeight();
        } else if (Height > 999999) {
            isValid = false;
            mView.showLargeHeight();
        }

        if (!isValid) {
            return;
        }


        AddPondData addPondData = new AddPondData();
        addPondData.setFarmId(farmId);
        addPondData.setPondName(pondName);
        addPondData.setPondType(pondType);

        addPondData.setLength(length);
        addPondData.setWidth(width);
        addPondData.setHeight(Height);

        addPondData.setRectangle(isRectangle);
        addPondData.setEquipment(equipment);
        mView.goToNextForm(addPondData);

    }

    private AddPondFormOneContract.View DUMMY_VIEW = new AddPondFormOneContract.View() {
        @Override
        public void removeErrors() {

        }

        @Override
        public void showInvalidPondName() {

        }

        @Override
        public void showInbalidPondType() {

        }

        @Override
        public void showInvalidLength() {

        }

        @Override
        public void showLargeLength() {

        }

        @Override
        public void showInvalidWidth() {

        }

        @Override
        public void showLargeWidth() {

        }

        @Override
        public void showInvalidHeight() {

        }

        @Override
        public void showLargeHeight() {

        }

        @Override
        public void goToNextForm(AddPondData addPondData) {

        }

        @Override
        public void initPondTypes(ArrayList<String> pondTypesList) {

        }

        @Override
        public void setPresenter(AddPondFormOneContract.Presenter presenter) {

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
