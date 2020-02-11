package com.soleeklab.nilebot.features;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.BaseActivity;
import com.soleeklab.nilebot.GetTranslationLUTQuery;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.api.RestClient;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.features.auth.login.LoginActivity;
import com.soleeklab.nilebot.features.home.HomeActivity;
import com.soleeklab.nilebot.type.LanguageType;
import com.soleeklab.nilebot.utils.LocalHelper;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class SplashNoInternetConnectionActivity extends BaseActivity {

    @BindView(R.id.image_connection)
    ImageView imageConnection;
    @BindView(R.id.txt_internet)
    TextView txtInternet;
    @BindView(R.id.btn_try_again)
    Button btnTryAgain;


    LocalHelper localHelper;
    Handler uiHandler;

    LocalRepo localRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_no_internet_connection);

        localRepo = new LocalRepoImpl(this);
        localHelper = LocalHelper.getInstance(this);

        ButterKnife.bind(this);
        uiHandler = new Handler(Looper.getMainLooper());

    }

    @Override
    protected View getView() {
        return null;
    }


    @OnClick(R.id.btn_try_again)
    public void onViewClicked() {
        btnTryAgain.setEnabled(false);
        if (isInternetConnection()) {
            getTranslation();
            btnTryAgain.setEnabled(true);
        } else {
            btnTryAgain.setEnabled(true);
        }


    }

    public void getTranslation() {
        showProgress();
        GetTranslationLUTQuery translationLUTQuery;

        if (localHelper.getLanguage().equalsIgnoreCase(LocalHelper.LANGUAGE_ARABIC))
            translationLUTQuery = GetTranslationLUTQuery.builder().lang(LanguageType.ARABIC).build();
        else
            translationLUTQuery = GetTranslationLUTQuery.builder().lang(LanguageType.ENGLISH).build();


        ApolloClientBuilder.getApolloClient(false).query(translationLUTQuery).enqueue(new ApolloCallback<>(new ApolloCall.Callback<GetTranslationLUTQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<GetTranslationLUTQuery.Data> response) {
                    hideProgress();
                    if (response.data() != null) {
                        //todo dun go
                        if (response.data().translationLUT().size() > 0) {
                            localRepo.saveTranslationLUT(response.data().translationLUT());
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (localRepo.isLogin()) {
                                    startActivity(new Intent(SplashNoInternetConnectionActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(SplashNoInternetConnectionActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        },20);


                    } else {
                        showAlert(R.string.server_error);
                    }

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                hideProgress();
                showAlert(R.string.error_occured);

            }
        }, uiHandler));

    }

    public void navigateToHome() {
        startActivity(new Intent(SplashNoInternetConnectionActivity.this, HomeActivity.class));
        finish();
    }
}
