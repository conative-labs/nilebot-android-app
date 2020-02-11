package com.soleeklab.nilebot.features.home.settings.profile;

import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.GetUserQuery;
import com.soleeklab.nilebot.LoginMutation;
import com.soleeklab.nilebot.ModifyUserMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class ProfilePresenter implements ProfileContract.Presenter {

    Handler uiHandler = new Handler(Looper.getMainLooper());
    Handler uiHandler1 = new Handler(Looper.getMainLooper());


    private ProfileContract.View mView;

    @Inject
    LocalRepo localRepo;

    @Inject
    public ProfilePresenter() {
    }

    @Override
    public void registerView(ProfileContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {

    }

    @Override
    public void start() {

    }

    @Override
    public void getUser() {
        mView.showProgress();
        GetUserQuery getUserQuery = GetUserQuery.builder().build();
        ApolloClientBuilder.getApolloClient(true).query(getUserQuery).enqueue(new ApolloCallback<>(new ApolloCall.Callback<GetUserQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetUserQuery.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().user() != null) {
                        GetUserQuery.User user = response.data().user();
                        localRepo.saveUser(new LoginMutation.User(user.__typename(), user.name(), user.email(),
                                new LoginMutation.Phone("","","")));

                        mView.setUserData(user);

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
        }, uiHandler1));

    }



}
