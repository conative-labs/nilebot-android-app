package com.soleeklab.nilebot.features.home.settings.changePassword;

import android.os.Handler;
import android.os.Looper;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.ModifyUserPasswordMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class ChangePasswordPresenter implements ChangePasswordContract.Presenter {

    Handler uiHandler = new Handler(Looper.getMainLooper());
    @Inject
    LocalRepo localRepo;

    private ChangePasswordContract.View mView;

    @Inject
    public ChangePasswordPresenter() {
    }


    @Override
    public void registerView(ChangePasswordContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {

    }

    @Override
    public void start() {

    }

    @Override
    public void updatePassword(String oldPassword, String newPassword, String confirmNewPassword) {
        mView.showProgress();
        mView.removeErrors();
        boolean isValid = true;


        if (oldPassword.length() < 8) {
            mView.showInvalidOldPassword();
            isValid = false;

        } else if (newPassword.length() < 8) {
            mView.showInvalidNewPassword();
            isValid = false;

        } else if (confirmNewPassword == null || !confirmNewPassword.equals(newPassword)) {
            mView.showInvalidConfirmPassword();
            isValid = false;
        }

        if (!isValid) {
            mView.hideProgress();
            return;
        }


        ModifyUserPasswordMutation modifyUserPasswordMutation = ModifyUserPasswordMutation.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword).build();

        ApolloClientBuilder.getApolloClient(true).mutate(modifyUserPasswordMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<ModifyUserPasswordMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<ModifyUserPasswordMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().modifyUserPassword().equals(Status.SUCCESS)) {
                        mView.successUpdates(response.data().modifyUserPassword().rawValue());
                    } else {
                        mView.showAlert(localRepo.getTranslation(response.data().modifyUserPassword().rawValue()));
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
}
