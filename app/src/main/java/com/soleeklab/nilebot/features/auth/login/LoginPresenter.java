package com.soleeklab.nilebot.features.auth.login;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.LoginMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.IPhoneNumber;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import static com.soleeklab.nilebot.utils.Utils.isNumeric;

public class LoginPresenter implements LoginContract.Presenter {


    @Inject
    LocalRepo localRepo;

    LoginContract.View mView;

    @Inject
    public LoginPresenter() {
    }

    @Override
    public void registerView(LoginContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {

    }

    @Override
    public void start() {

    }

    @Override
    public void login(String prefix,String credential, String passsword) {

        mView.showProgress();

        mView.removeErrors();
        boolean isValid = true;

        //Data Validation

        if (credential.length() < 2) {
            mView.showInvalidPhone();
            isValid = false;

        } else if (passsword.length() < 8) {
            mView.showInvalidPassword();
            isValid = false;

        }

        if (!isValid) {
            mView.hideProgress();
            return;
        }

        LoginMutation loginMutation;
        if (isNumeric(credential)) {
            if (credential.substring(0, 1).equals("0"))
                credential = credential.substring(1);
            loginMutation = LoginMutation.builder()
                    .phone(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build())
                    .password(passsword)
                    .build();
        } else {
            loginMutation = LoginMutation.builder()
                    .email(credential)
                    .password(passsword)
                    .build();
        }


        ApolloClientBuilder.getApolloClient(false).mutate(loginMutation).enqueue(new ApolloCall.Callback<LoginMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<LoginMutation.Data> response) {

                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().login() != null) {
                        if(response.data().login().status().equals(Status.SUCCESS)){
                            localRepo.saveUserToken(response.data().login().token());
                            localRepo.saveUser(response.data().login().user());
                            mView.navigateToHome();
                        }else if(response.data().login().status().equals(Status.NUMBERNOTVERIFIED)){
                            mView.navigateToVerifyPhone();
                        }else{
                            mView.showAlert(localRepo.getTranslation(response.data().login().status().rawValue()));
                        }
                    } else {
                        mView.showAlert(localRepo.getTranslation(Status.FAILURE.rawValue()));
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


}
