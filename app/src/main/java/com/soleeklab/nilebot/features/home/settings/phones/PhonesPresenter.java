package com.soleeklab.nilebot.features.home.settings.phones;

import android.os.Handler;
import android.os.Looper;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.AddPhoneMutation;
import com.soleeklab.nilebot.GetPhonesQuery;
import com.soleeklab.nilebot.ModifyPhoneMutation;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.RemovePhoneMutation;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.type.IPhoneNumber;
import com.soleeklab.nilebot.type.LanguageType;
import com.soleeklab.nilebot.type.Status;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

public class PhonesPresenter implements PhonesContract.Presenter {

    Handler uiHandler = new Handler(Looper.getMainLooper());
    Handler uiHandlerAdd = new Handler(Looper.getMainLooper());
    Handler uiHandlerEdit = new Handler(Looper.getMainLooper());
    Handler uiHandlerRemove = new Handler(Looper.getMainLooper());
    private PhonesContract.View mView;

    @Inject
    LocalRepo localRepo;


    @Inject
    public PhonesPresenter() {
    }

    @Override
    public void registerView(PhonesContract.View view) {
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
    public void getPhones(boolean showProgress) {
        if (showProgress)
            mView.showProgress();
        GetPhonesQuery getPhonesQuery = GetPhonesQuery.builder().build();
        ApolloClientBuilder.getApolloClient(true).query(getPhonesQuery).enqueue(new ApolloCallback<>(new ApolloCall.Callback<GetPhonesQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetPhonesQuery.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().user() != null) {
                        mView.initPhones(response.data().user().phones());
                    } else {
                        mView.showAlert(R.string.server_error);
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
    public void addPhone(String prefix,String phone, LanguageType languageType) {
        mView.showProgress();

        if (phone.substring(0, 1).equals("0"))
            phone = phone.substring(1);


        AddPhoneMutation addPhoneMutation = AddPhoneMutation.builder()
                .number(IPhoneNumber.builder().prefix("+"+prefix).number(phone).build())
                .language(languageType).build();
        String finalPhone = phone;
        ApolloClientBuilder.getApolloClient(true).mutate(addPhoneMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<AddPhoneMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<AddPhoneMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().addPhone().equals(Status.SUCCESS)) {
                        mView.successNewPhone(prefix, finalPhone,languageType.rawValue());
                    } else {
                        mView.hideProgress();
                        mView.showAlert(localRepo.getTranslation(response.data().addPhone().rawValue()));
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
    public void editPhone(String prefix,String phone, LanguageType languageType) {
        mView.showProgress();
        ModifyPhoneMutation modifyPhoneMutation = ModifyPhoneMutation.builder()
                .number(IPhoneNumber.builder().prefix("+"+prefix).number(phone).build())
                .language(languageType).build();
        ApolloClientBuilder.getApolloClient(true).mutate(modifyPhoneMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<ModifyPhoneMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<ModifyPhoneMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().modifyPhone().equals(Status.SUCCESS)) {
                        mView.successEditPhone();
                    } else {
                        mView.hideProgress();
                        mView.showAlert(localRepo.getTranslation(response.data().modifyPhone().rawValue()));
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
        }, uiHandlerEdit));
    }

    @Override
    public void removePhone(String prefix ,String number) {
        mView.showProgress();

        RemovePhoneMutation removePhoneMutation = RemovePhoneMutation.builder()
                .number(IPhoneNumber.builder().prefix("+"+prefix).number(number).build())
                .build();
        ApolloClientBuilder.getApolloClient(true).mutate(removePhoneMutation).enqueue(new ApolloCallback<>(new ApolloCall.Callback<RemovePhoneMutation.Data>() {
            @Override
            public void onResponse(@NotNull Response<RemovePhoneMutation.Data> response) {
                mView.hideProgress();
                if (response.data() != null) {
                    if (response.data().removePhone().equals(Status.SUCCESS)) {
                        mView.successDeletePhone();
                    } else {
                        mView.hideProgress();
                        mView.showAlert(localRepo.getTranslation(response.data().removePhone().rawValue()));
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

    private PhonesContract.View DUMMY_VIEW = new PhonesContract.View() {
        @Override
        public void initPhones(List<GetPhonesQuery.Phone> phoneArrayList) {

        }

        @Override
        public void successEditPhone() {

        }

        @Override
        public void successDeletePhone() {

        }

        @Override
        public void successNewPhone(String prefix, String number, String language) {

        }


        @Override
        public void setPresenter(PhonesContract.Presenter presenter) {

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
