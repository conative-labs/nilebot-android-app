package com.soleeklab.nilebot.features.home.settings.devices;


import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.AddBotMutation;
import com.soleeklab.nilebot.GetBotsQuery;
import com.soleeklab.nilebot.ModifyBotMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.RemoveBotMutation;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.inject.Inject;

public class DevicesPresenter implements DevicesContract.Presenter {


    Handler uiHandler = new Handler(Looper.getMainLooper());
    Handler uiHandlerAdd = new Handler(Looper.getMainLooper());
    Handler uiHandlerRemove = new Handler(Looper.getMainLooper());
    Handler uiHandlerModify = new Handler(Looper.getMainLooper());

    @Inject
    LocalRepo localRepo;

    DevicesContract.View mView;

    @Inject
    public DevicesPresenter() {
    }

    @Override
    public void registerView(DevicesContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {

    }

    @Override
    public void start() {

    }

    @Override
    public void getBots(boolean showProgress) {

        if (showProgress)
            mView.showProgress();

        GetBotsQuery getBotsQuery = GetBotsQuery.builder().build();


        ApolloClientBuilder.getApolloClient(true).query(getBotsQuery).enqueue(new ApolloCallback<>(new ApolloCall.Callback<GetBotsQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetBotsQuery.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().user() != null) {
                        mView.initBots(response.data().user().bots());
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
        }, uiHandler));
    }

    @Override
    public void addBot(String name, int rate) {

        mView.showProgress();

        AddBotMutation addBotMutation = AddBotMutation.builder().name(name).rate(rate).build();

        ApolloClientBuilder.getApolloClient(true).mutate(addBotMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<AddBotMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<AddBotMutation.Data> response) {
//                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().addBot() != null) {
                        mView.successNewBot(response.data().addBot().id(), response.data().addBot().token());

                    } else {
                        mView.hideProgress();
                        mView.showAlert(R.string.error_signup);
                    }

                } else {
                    mView.hideProgress();
                    mView.showAlert(R.string.server_error);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mView.hideProgress();
                mView.showAlert(R.string.error_occured);
            }
        }, uiHandlerAdd));
    }

    @Override
    public void editBot(String id, String name, int rate, String timeZone) {
        mView.showProgress();
        ModifyBotMutation modifyBotMutation = ModifyBotMutation.builder().id(id).name(name).rate(rate).build();
        ApolloClientBuilder.getApolloClient(true).mutate(modifyBotMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<ModifyBotMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<ModifyBotMutation.Data> response) {
//                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().modifyBot() != null) {
                        if (response.data().modifyBot().equals(Status.SUCCESS)) {

                            mView.successEditBot();
//                            mView.showToast(localRepo.getTranslation(response.data().modifyBot().rawValue()));

                        } else {
                            mView.hideProgress();
                            mView.showAlert(localRepo.getTranslation(response.data().modifyBot().rawValue()));
                        }

                    } else {
                        mView.hideProgress();
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
        }, uiHandlerAdd));

    }

    @Override
    public void removeBot(String id) {
        mView.showProgress();

        RemoveBotMutation removeBotMutation = RemoveBotMutation.builder().id(id).build();
        ApolloClientBuilder.getApolloClient(true).mutate(removeBotMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<RemoveBotMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<RemoveBotMutation.Data> response) {
//                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().removeBot().equals(Status.SUCCESS)) {
                        mView.successDeleteBot();
                    } else {
                        mView.hideProgress();
                        mView.showAlert(localRepo.getTranslation(response.data().removeBot().rawValue()));
                    }

                } else {
                    mView.hideProgress();
                    mView.showAlert(R.string.server_error);
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mView.hideProgress();
                mView.showAlert(R.string.error_occured);
            }
        }, uiHandlerRemove));


    }

}
