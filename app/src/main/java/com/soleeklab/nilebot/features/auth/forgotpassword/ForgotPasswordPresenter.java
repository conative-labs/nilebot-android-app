package com.soleeklab.nilebot.features.auth.forgotpassword;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.CreateMutation;
import com.soleeklab.nilebot.LoginMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.RequestPasswordResetMutation;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.features.auth.login.LoginContract;
import com.soleeklab.nilebot.type.IPhoneNumber;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import static com.soleeklab.nilebot.utils.Utils.isNumeric;

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {

    private ForgotPasswordContract.View mView;
    Handler uiHandler = new Handler(Looper.getMainLooper());


    @Inject
    LocalRepo localRepo;

    @Inject
    public ForgotPasswordPresenter() {
    }

    @Override
    public void registerView(ForgotPasswordContract.View view) {
        this.mView = view;
    }

    @Override
    public void unregisterView() {

    }

    @Override
    public void start() {

    }

    @Override
    public void forgotPassword(String prefix, String credential) {

        mView.showProgress();

        mView.removeErrors();
        boolean isValid = true;
        //Data Validation
        if (credential.length() < 2) {
            mView.showInvalidPhone();
            isValid = false;

        }
        if (!isValid) {
            mView.hideProgress();
            return;
        }


        RequestPasswordResetMutation requestPasswordResetMutation;
        if (isNumeric(credential)) {
            if (credential.substring(0, 1).equals("0"))
                credential = credential.substring(1);
            requestPasswordResetMutation = RequestPasswordResetMutation.builder()
                    .phone(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build())
                    .build();
        } else {
            requestPasswordResetMutation = RequestPasswordResetMutation.builder()
                    .email(credential)
                    .build();
        }


        ApolloClientBuilder.getApolloClient(false).mutate(requestPasswordResetMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<RequestPasswordResetMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<RequestPasswordResetMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().requestPasswordReset() != null && response.data().requestPasswordReset().equals(Status.SUCCESS)) {
                        mView.navigateToConfirmcode();
                    } else if(response.data().requestPasswordReset() != null){
                        mView.showAlert(localRepo.getTranslation(response.data().requestPasswordReset().rawValue()));
                    }else{
                        mView.showAlert(R.string.error_occured);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                mView.hideProgress();
                mView.showAlert(R.string.error_occured);

            }
        }, uiHandler));

    }

}
