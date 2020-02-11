package com.soleeklab.nilebot.features.auth.forgotpassword.resetPassword;

import android.os.Handler;
import android.os.Looper;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.PasswordResetMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.RequestPasswordResetMutation;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.IPhoneNumber;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import static com.soleeklab.nilebot.utils.Utils.isNumeric;

public class ResetPasswordPresenter implements ResetPasswordContract.Presenter {

    Handler uiHandler = new Handler(Looper.getMainLooper());

    @Inject
    LocalRepo localRepo;

    private ResetPasswordContract.View mView;

    @Inject
    public ResetPasswordPresenter() {
    }

    @Override
    public void registerView(ResetPasswordContract.View view) {
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
    public void resetPassword(String prefix, String credential, String code, String password, String confirmPassword) {

        mView.showProgress();
        mView.removeErrors();
        boolean isValid = true;

        if (password.length() < 8) {
            mView.showInvalidPassword();
            isValid = false;

        } else if (confirmPassword == null || !confirmPassword.equals(password)) {
            mView.showInvalidConfirmPassword();
            isValid = false;
        }

        if (!isValid) {
            mView.hideProgress();
            return;
        }


        PasswordResetMutation passwordResetMutation;
        if (isNumeric(credential)) {
            if (credential.substring(0, 1).equals("0"))
                credential = credential.substring(1);
            passwordResetMutation = PasswordResetMutation.builder()
                    .phone(IPhoneNumber.builder().prefix("+" + prefix).number(credential).build())
                    .code(code)
                    .password(password)
                    .build();
        } else {
            passwordResetMutation = PasswordResetMutation.builder()
                    .email(credential)
                    .code(code)
                    .password(password)
                    .build();
        }


        ApolloClientBuilder.getApolloClient(false).mutate(passwordResetMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<PasswordResetMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<PasswordResetMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().passwordReset() != null && response.data().passwordReset().equals(Status.SUCCESS)) {
                        mView.navigateToLogin(Status.SUCCESS.rawValue());

                    } else if (response.data().passwordReset() != null) {
                        mView.showAlert(localRepo.getTranslation(response.data().passwordReset().rawValue()));
                    } else {
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

    private ResetPasswordContract.View DUMMY_VIEW = new ResetPasswordContract.View() {
        @Override
        public void navigateToLogin(String message) {

        }

        @Override
        public void removeErrors() {

        }

        @Override
        public void showInvalidPassword() {

        }

        @Override
        public void showInvalidConfirmPassword() {

        }

        @Override
        public void showInvalidCredtials(String error) {

        }

        @Override
        public void setPresenter(ResetPasswordContract.Presenter presenter) {

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
