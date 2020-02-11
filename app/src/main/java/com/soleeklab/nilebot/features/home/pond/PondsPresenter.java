package com.soleeklab.nilebot.features.home.pond;

import android.os.Handler;
import android.os.Looper;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.GetPondsByFarmIDQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.RemovePondMutation;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.PondType;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PondsPresenter implements PondsContract.Presenter {

    Handler uiHandler = new Handler(Looper.getMainLooper());
    private PondsContract.View mView;

    @Inject
    LocalRepo localRepo;

    @Inject
    public PondsPresenter() {

    }

    @Override
    public void registerView(PondsContract.View view) {
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
    public void getPondTypes() {
        ArrayList<String> pondTypesList = new ArrayList<>();

        for (PondType pondType : PondType.values())
            pondTypesList.add(pondType.rawValue());

        mView.initPondTypes(pondTypesList);
    }

    @Override
    public void getPonds(String farmId) {
        mView.showProgress();

        ArrayList<String> ids = new ArrayList<>();
        ids.add(farmId);

        GetPondsByFarmIDQuery getPondsByFarmIDQuery = GetPondsByFarmIDQuery.builder().id(ids).build();

        ApolloClientBuilder.getApolloClient(true).query(getPondsByFarmIDQuery).enqueue(new ApolloCall.Callback<GetPondsByFarmIDQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetPondsByFarmIDQuery.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().farms() != null) {
                        mView.initPonds(response.data().farms().get(0).ponds());
                    } else {
                        mView.showAlert(R.string.error_signup);
                    }

                } else {
                    mView.showAlert(R.string.server_error);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mView.hideProgress();
                mView.showAlert(R.string.error_occured);
            }
        });
    }

    @Override
    public void removePond(String pondId, String farmId) {

        RemovePondMutation removePondMutation = RemovePondMutation.builder().id(pondId).build();

        ApolloClientBuilder.getApolloClient(true).mutate(removePondMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<RemovePondMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<RemovePondMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().removePond().equals(Status.SUCCESS)) {
                        mView.hideDialog();
                        getPonds(farmId);
                    } else {
                        mView.showAlert(localRepo.getTranslation(response.data().removePond().rawValue()));
                    }

                } else
                    mView.showAlert(R.string.server_error);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mView.hideProgress();
                mView.showAlert(R.string.error_occured);
            }
        }, uiHandler));
    }

    private PondsContract.View DUMMY_VIEW = new PondsContract.View() {
        @Override
        public void initPondTypes(ArrayList<String> pondTypesList) {

        }

        @Override
        public void initPonds(List<GetPondsByFarmIDQuery.Pond> farms) {

        }

        @Override
        public void hideDialog() {

        }

        @Override
        public void setPresenter(PondsContract.Presenter presenter) {

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
