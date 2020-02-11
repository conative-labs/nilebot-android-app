package com.soleeklab.nilebot.features.home.farms;

import android.os.Handler;
import android.os.Looper;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.GetFarmsQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.RemoveFarmMutation;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

public class FarmsPresenter implements FarmsContract.Presenter {

    private FarmsContract.View mView;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    @Inject
    LocalRepo localRepo;

    @Inject
    public FarmsPresenter() {
    }


    @Override
    public void registerView(FarmsContract.View view) {
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
    public void getFarms() {
        mView.showProgress();
        GetFarmsQuery getFarmsQuery = GetFarmsQuery.builder().build();
        ApolloClientBuilder.getApolloClient(true).query(getFarmsQuery).enqueue(new ApolloCallback<>(new ApolloCall.Callback<GetFarmsQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetFarmsQuery.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().user() != null) {
                        mView.initFarms(response.data().user().farms());
                    } else {
                        mView.logoutUser();
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
        }, uiHandler));


    }


    @Override
    public void removeFarm(String id) {

        RemoveFarmMutation removeFarmMutation = RemoveFarmMutation.builder().id(id).build();
        ApolloClientBuilder.getApolloClient(true).mutate(removeFarmMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<RemoveFarmMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<RemoveFarmMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().removeFarm().equals(Status.SUCCESS)) {
                        mView.hideDialog();
                        getFarms();
                    } else {
                        mView.showAlert(localRepo.getTranslation(response.data().removeFarm().rawValue()));
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

    private FarmsContract.View DUMMY_VIEW = new FarmsContract.View() {
        @Override
        public void initFarms(List<GetFarmsQuery.Farm> farms) {

        }

        @Override
        public void hideDialog() {

        }

        @Override
        public void logoutUser() {

        }

        @Override
        public void setPresenter(FarmsContract.Presenter presenter) {

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
