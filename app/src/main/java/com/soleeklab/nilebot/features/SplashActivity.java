package com.soleeklab.nilebot.features;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCallback;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.soleeklab.nilebot.BaseActivity;
import com.soleeklab.nilebot.CrashReportActivity;
import com.soleeklab.nilebot.GetTranslationLUTQuery;
import com.soleeklab.nilebot.HandleAppCrash;
import com.soleeklab.nilebot.R;
import com.soleeklab.nilebot.api.ApolloClientBuilder;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;
import com.soleeklab.nilebot.features.auth.login.LoginActivity;
import com.soleeklab.nilebot.features.home.HomeActivity;
import com.soleeklab.nilebot.type.LanguageType;
import com.soleeklab.nilebot.utils.LocalHelper;

import org.jetbrains.annotations.NotNull;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    LocalRepo localRepo;
    LocalHelper localHelper;
    Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        localRepo = new LocalRepoImpl(this);
        ButterKnife.bind(this);
        HandleAppCrash.deploy(SplashActivity.this, CrashReportActivity.class);
        uiHandler = new Handler(Looper.getMainLooper());
        localHelper = LocalHelper.getInstance(this);

        if (isInternetConnection())
            getTranslation();
        else
            navigateToNoInternet();
    }

    public void simulateCrash() {
        int x = 1 / 0;
    }

    private void getTranslation() {
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
                    if (response.data().translationLUT() != null) {
                        if (response.data().translationLUT().size() > 0) {
                            localRepo.saveTranslationLUT(response.data().translationLUT());
                        }
                    }
//
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (localRepo.isLogin()) {
                                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    }, 3000);


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

    public void navigateToNoInternet() {
        startActivity(new Intent(SplashActivity.this, SplashNoInternetConnectionActivity.class));
        finish();
    }


    @Override
    protected View getView() {
        return null;
    }
}
