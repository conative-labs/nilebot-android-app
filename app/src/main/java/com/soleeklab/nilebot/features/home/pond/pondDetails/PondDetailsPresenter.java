package com.soleeklab.nilebot.features.home.pond.pondDetails;

import android.os.Handler;
import android.os.Looper;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.GetSensorsByPondIDQuery;
//import com.soleeklab.nilebot.ModifySensorsMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PondDetailsPresenter implements PondDetailsContract.Presenter {


    private PondDetailsContract.View mView;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    @Inject
    LocalRepo localRepo;

    @Inject
    public PondDetailsPresenter() {
    }

    @Override
    public void registerView(PondDetailsContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {
        this.mView = DUMMY_VIEW;
    }

    @Override
    public void start() {

    }


    @Override
    public void getSensors(String pondId, boolean showProgress) {
        if (showProgress)
            mView.showProgress();
        ArrayList<String> pondIds = new ArrayList<>();
        pondIds.add(pondId);

        GetSensorsByPondIDQuery getSensorsByPondIDQuery = GetSensorsByPondIDQuery.builder().id(pondIds).build();

        ApolloClientBuilder.getApolloClient(true).query(getSensorsByPondIDQuery).enqueue(new ApolloCall.Callback<GetSensorsByPondIDQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetSensorsByPondIDQuery.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().ponds() != null) {

                        ArrayList<GetSensorsByPondIDQuery.Parameter> sensorArrayList = new ArrayList<>();
                        for (GetSensorsByPondIDQuery.Pond pond : response.data().ponds())
                            sensorArrayList.addAll(pond.parameters());

                        mView.initSensors(sensorArrayList);
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




    private PondDetailsContract.View DUMMY_VIEW = new PondDetailsContract.View() {
        @Override
        public void initSensors(List<GetSensorsByPondIDQuery.Parameter> sensors) {

        }

        @Override
        public void refresh() {

        }

        @Override
        public void successDeleteSensor() {

        }

        @Override
        public void setPresenter(PondDetailsContract.Presenter presenter) {

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
