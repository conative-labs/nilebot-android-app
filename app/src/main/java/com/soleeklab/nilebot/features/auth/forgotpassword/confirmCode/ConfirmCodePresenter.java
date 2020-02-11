package com.soleeklab.nilebot.features.auth.forgotpassword.confirmCode;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.AddPhoneMutation;
import com.soleeklab.nilebot.CreateMutation;
import com.soleeklab.nilebot.LoginMutation;
import com.soleeklab.nilebot.ModifyUserPhoneMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.RequestPasswordResetMutation;
import com.soleeklab.nilebot.VerifyPasswordResetMutation;
import com.soleeklab.nilebot.VerifyPhoneMutation;
import com.soleeklab.nilebot.VerifyUserEmailMutation;
import com.soleeklab.nilebot.VerifyUserPhoneMutation;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.IPhoneNumber;
import com.soleeklab.nilebot.type.LanguageType;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import static com.soleeklab.nilebot.utils.Utils.isNumeric;

public class ConfirmCodePresenter implements ConfirmCodeContract.Presenter {

    Handler uiHandler = new Handler(Looper.getMainLooper());

    private ConfirmCodeContract.View mView;

    @Inject
    LocalRepo localRepo;

    @Inject
    public ConfirmCodePresenter() {
    }


    @Override
    public void registerView(ConfirmCodeContract.View view) {
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
    public void verifyCode(int screenType, String prefix, String credential, String code) {
        mView.showProgress();

        if (screenType == ConfirmCodeFragment.resetPasswordScreen) {
            VerifyPasswordResetMutation verifyPasswordResetMutation;

            if (isNumeric(credential)) {
                if (credential.substring(0, 1).equals("0"))
                    credential = credential.substring(1);
                verifyPasswordResetMutation = VerifyPasswordResetMutation.builder()
                        .phone(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build())
                        .code(code)
                        .build();
            } else {
                verifyPasswordResetMutation = VerifyPasswordResetMutation.builder()
                        .email(credential)
                        .code(code)
                        .build();
            }

            ApolloClientBuilder.getApolloClient(false).mutate(verifyPasswordResetMutation).enqueue(new ApolloCall.Callback<VerifyPasswordResetMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<VerifyPasswordResetMutation.Data> response) {
                    mView.hideProgress();
                    if (response.data() != null) {
                        if (response.data().verifyPasswordReset() != null && response.data().verifyPasswordReset().equals(Status.SUCCESS)) {
                            mView.verificationSuccess(Status.SUCCESS.rawValue());
                        } else {
                            mView.showAlert(localRepo.getTranslation(response.data().verifyPasswordReset().rawValue()));
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    mView.hideProgress();
                    mView.showAlert(R.string.error_occured);
                }
            });
        }

        else if (screenType == ConfirmCodeFragment.signUpScreen ||
                screenType == ConfirmCodeFragment.loginScreen ) {

            VerifyUserPhoneMutation verifyUserPhoneMutation;

            if (credential.substring(0, 1).equals("0"))
                credential = credential.substring(1);
            verifyUserPhoneMutation = VerifyUserPhoneMutation.builder()
                    .phone(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build())
                    .code(code)
                    .build();

            ApolloClientBuilder.getApolloClient(false).mutate(verifyUserPhoneMutation).enqueue(new ApolloCall.Callback<VerifyUserPhoneMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<VerifyUserPhoneMutation.Data> response) {
                    mView.hideProgress();
                    if (response.data() != null) {
                        if (response.data().verifyUserPhone() != null && response.data().verifyUserPhone().equals(Status.SUCCESS)) {
                            mView.verificationSuccess(Status.SUCCESS.rawValue());
                        } else {
                            mView.showAlert(localRepo.getTranslation(response.data().verifyUserPhone().rawValue()));
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    mView.hideProgress();
                    mView.showAlert(R.string.error_occured);
                }
            });


        }


        else if( screenType == ConfirmCodeFragment.editPhoneScreen){
            VerifyUserPhoneMutation verifyUserPhoneMutation;

            if (credential.substring(0, 1).equals("0"))
                credential = credential.substring(1);
            verifyUserPhoneMutation = VerifyUserPhoneMutation.builder()
                    .phone(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build())
                    .code(code)
                    .build();

            ApolloClientBuilder.getApolloClient(true).mutate(verifyUserPhoneMutation).enqueue(new ApolloCall.Callback<VerifyUserPhoneMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<VerifyUserPhoneMutation.Data> response) {
                    mView.hideProgress();
                    if (response.data() != null) {
                        if (response.data().verifyUserPhone() != null && response.data().verifyUserPhone().equals(Status.SUCCESS)) {
                            mView.verificationSuccess(Status.SUCCESS.rawValue());
                        } else if(response.data().verifyUserPhone() != null) {
                            mView.showAlert(localRepo.getTranslation(response.data().verifyUserPhone().rawValue()));
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
            });
        }

        else if (screenType == ConfirmCodeFragment.addPhoneScreen) {

            VerifyPhoneMutation verifyPhoneMutation = VerifyPhoneMutation.builder()
                    .number(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build())
                    .code(code).build();

            ApolloClientBuilder.getApolloClient(true).mutate(verifyPhoneMutation).enqueue(new ApolloCall.Callback<VerifyPhoneMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<VerifyPhoneMutation.Data> response) {
                    mView.hideProgress();
                    if (response.data() != null) {
                        if (response.data().verifyPhone() != null) {
                            if (response.data().verifyPhone().equals(Status.SUCCESS)) {
                                mView.verificationSuccess(localRepo.getTranslation(Status.SUCCESS.rawValue()));
                            } else  {
                                mView.showAlert(localRepo.getTranslation(response.data().verifyPhone().rawValue()));
                            }
                        }else{
                            mView.showAlert(R.string.server_error);
                        }
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

    @Override
    public void resendCode(int screenType, String prefix, String credential, String name, String password, String email,String language) {

        if (screenType == ConfirmCodeFragment.resetPasswordScreen) {

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


            ApolloClientBuilder.getApolloClient(false).mutate(requestPasswordResetMutation).enqueue(new ApolloCall.Callback<RequestPasswordResetMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<RequestPasswordResetMutation.Data> response) {
                    mView.hideProgress();
                    if (response.data() != null) {
                        if (response.data().requestPasswordReset() != null && response.data().requestPasswordReset().equals(Status.SUCCESS)) {
                            mView.resendApiSuccess();
                        } else if(response.data().requestPasswordReset() != null) {
                            mView.showAlert(localRepo.getTranslation(response.data().requestPasswordReset().rawValue()));
                        }else {
                            mView.showAlert(R.string.error_occured);
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    mView.hideProgress();
                    mView.showAlert(R.string.error_occured);

                }
            });
        } else if (screenType == ConfirmCodeFragment.loginScreen) {
            LoginMutation loginMutation;
            if (isNumeric(credential)) {
                if (credential.substring(0, 1).equals("0"))
                    credential = credential.substring(1);
                loginMutation = LoginMutation.builder()
                        .phone(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build())
                        .password(password)
                        .build();
            } else {
                loginMutation = LoginMutation.builder()
                        .email(credential)
                        .password(password)
                        .build();
            }


            ApolloClientBuilder.getApolloClient(false).mutate(loginMutation).enqueue(new ApolloCall.Callback<LoginMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<LoginMutation.Data> response) {

                    mView.hideProgress();
                    if (response.data() != null) {
                        if (response.data().login() != null) {
                            if (response.data().login().status().equals(Status.NUMBERNOTVERIFIED)) {
                                mView.resendApiSuccess();
                                mView.showAlert(localRepo.getTranslation(Status.SUCCESS.rawValue()));
                            } else {
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
        } else if (screenType == ConfirmCodeFragment.signUpScreen) {
            CreateMutation createMutation = CreateMutation.builder()
                    .name(name)
                    .email(email)
                    .phone(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build())
                    .password(password).build();
//
//
            ApolloClientBuilder.getApolloClient(false).mutate(createMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<CreateMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<CreateMutation.Data> response) {
                    mView.hideProgress();
                    if (response.data() != null) {
                        mView.resendApiSuccess();
                        mView.showAlert(localRepo.getTranslation(Status.SUCCESS.rawValue()));
                    } else
                        mView.showAlert(R.string.server_error);

                }

                @Override
                public void onFailure(@NotNull ApolloException e) {
                    mView.hideProgress();
                    mView.showAlert(R.string.error_occured);
                }
            }, uiHandler));
        } else if (screenType == ConfirmCodeFragment.editPhoneScreen) {
            ModifyUserPhoneMutation modifyUserPhoneMutation = ModifyUserPhoneMutation.builder()
                    .phone(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build()).build();

            ApolloClientBuilder.getApolloClient(true).mutate(modifyUserPhoneMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<ModifyUserPhoneMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<ModifyUserPhoneMutation.Data> response) {
                    mView.hideProgress();
                    if (response.data() != null) {
                        if (response.data().modifyUserPhone().equals(Status.SUCCESS)) {
                            mView.resendApiSuccess();
                            mView.showAlert(localRepo.getTranslation(Status.SUCCESS.rawValue()));
                        } else {
                            mView.showAlert(localRepo.getTranslation(response.data().modifyUserPhone().rawValue()));
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
        } else if (screenType == ConfirmCodeFragment.addPhoneScreen) {
                //todo resend code when add phone
            if (credential.substring(0, 1).equals("0"))
                credential = credential.substring(1);

            AddPhoneMutation addPhoneMutation = AddPhoneMutation.builder()
                    .number(IPhoneNumber.builder().prefix("+"+prefix).number(credential).build())
                    .language(LanguageType.safeValueOf(language)).build();
            ApolloClientBuilder.getApolloClient(true).mutate(addPhoneMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<AddPhoneMutation.Data>() {
                @Override
                public void onResponse(@NotNull Response<AddPhoneMutation.Data> response) {
                    mView.hideProgress();
                    if (response.data() != null) {
                        if (response.data().addPhone().equals(Status.SUCCESS)) {
                            mView.resendApiSuccess();
                            mView.showAlert(localRepo.getTranslation(Status.SUCCESS.rawValue()));
                        } else {
                            mView.showAlert(localRepo.getTranslation(response.data().addPhone().rawValue()));
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


    private ConfirmCodeContract.View DUMMY_VIEW = new ConfirmCodeContract.View() {
        @Override
        public void startTimer() {

        }

        @Override
        public void handleTextWatcher() {

        }

        @Override
        public void disableViews() {

        }

        @Override
        public void enableViews() {

        }

        @Override
        public void navigateToLogin() {

        }

        @Override
        public void showInvalidCredtials(String error) {

        }

        @Override
        public void resendApiSuccess() {

        }

        @Override
        public void verificationSuccess(String message) {

        }

        @Override
        public void setPresenter(ConfirmCodeContract.Presenter presenter) {

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
