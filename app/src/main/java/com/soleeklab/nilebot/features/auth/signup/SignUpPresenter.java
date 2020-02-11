package com.soleeklab.nilebot.features.auth.signup;

import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.CreateMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.IPhoneNumber;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class SignUpPresenter implements SignUpContract.Presenter {

    SignUpContract.View mView;
    Handler uiHandler = new Handler(Looper.getMainLooper());


    @Inject
    LocalRepo localRepo;

    @Inject
    public SignUpPresenter() {

    }

    @Override
    public void terms() {

    }

    @Override
    public void registerView(SignUpContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {

    }

    @Override
    public void start() {

    }

    @Override
    public void signup(String name, String phone, String email, String prefix, String password, String confirmPassword, boolean acceptTerms) {
        mView.showProgress();
        mView.removeErrors();
        boolean isValid = true;
        if (name.length() < 4) {
            mView.showInvalidName();
            isValid = false;
        } else if (phone.length() < 8 || phone.length() > 15) {
            mView.showInvalidPhone();
            isValid = false;
        } else if (password.length() < 8) {
            mView.showInvalidPassword();
            isValid = false;

        } else if (confirmPassword == null || !confirmPassword.equals(password)) {
            mView.showInvalidCinfirmPassword();
            isValid = false;
        } else if (!acceptTerms) {
            mView.showAcceptTerms();
            isValid = false;
        }

        if (!isValid) {
            mView.hideProgress();
            return;
        }

        if (phone.substring(0, 1).equals("0"))
            phone = phone.substring(1);

        CreateMutation createMutation = CreateMutation.builder()
                .name(name)
                .email(email)
                .phone(IPhoneNumber.builder().prefix("+"+prefix).number(phone).build())
                .password(password).build();
//
//
        ApolloClientBuilder.getApolloClient(false).mutate(createMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<CreateMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<CreateMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if(response.data().addUser() != null){
                        if (response.data().addUser().equals(Status.SUCCESS)) {
                            mView.navigateToLogin();
//                        mView.showToast(localRepo.getTranslation(response.data().addUser().rawValue()));

                        } else {
                            mView.showAlert(localRepo.getTranslation(response.data().addUser().rawValue()));

                        }
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
