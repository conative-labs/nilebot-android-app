package com.soleeklab.nilebot.data.repo;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soleeklab.nilebot.GetTranslationLUTQuery;
import com.soleeklab.nilebot.LoginMutation;
import com.soleeklab.nilebot.type.IPhoneNumber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by omaraboulfotoh on 12/13/17.
 */

public class LocalRepoImpl implements LocalRepo {
    private static final String PREFERENCE_NAME = "Preference";
    private static final String KEY_USER = "user";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_LAUNCH = "launch";
    private static final String KEY_PUSH_TOKEN = "push_token";
    private static final String KEY_ADS_POS = "ads_pos";
    private static final String KEY_USER_MOBILE = "user_mobile";
    private static final String KEY_USER_TOKEN = "user_token";
    private static final String KEY_CART = "key_cart";
    private static final String KEY_CREDIT_CARD = "key_credit_card";
    private static final String KEY_NOTIFICATION = "key_notification";
    private static final String KEY_Translation = "KEY_Translation";


    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Inject
    public LocalRepoImpl(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        gson = new Gson();
    }


    @Override
    public void saveMobile(String mobile) {
        sharedPreferences.edit().putString(KEY_USER_MOBILE, mobile).apply();
    }


    @Override
    public String getMobile() {
        return sharedPreferences.getString(KEY_USER_MOBILE, "");
    }


    @Override
    public void logout() {
        sharedPreferences.edit().remove(KEY_LOGIN).apply();
        sharedPreferences.edit().remove(KEY_USER).apply();
    }

    @Override
    public void setLoginFalse() {
        sharedPreferences.edit().remove(KEY_LOGIN).apply();
    }

    @Override
    public boolean isLogin() {
        return sharedPreferences.getBoolean(KEY_LOGIN, false);
    }

    @Override
    public void setFirstTimeLaunchFalse(boolean value) {
        sharedPreferences.edit().putBoolean(KEY_LAUNCH, value).apply();
    }

    @Override
    public boolean isFirstTimeLaunch() {
        return sharedPreferences.getBoolean(KEY_LAUNCH, false);
    }


    @Override
    public void saveUser(LoginMutation.User user) {
        sharedPreferences.edit().putString(KEY_USER, gson.toJson(user)).apply();
        sharedPreferences.edit().putBoolean(KEY_LOGIN, true).apply();
    }

    @Override
    public LoginMutation.User getUser() {
        return gson.fromJson(sharedPreferences.getString(KEY_USER, null), LoginMutation.User.class);
    }

    @Override
    public void saveUserPushToken(String token) {
        sharedPreferences.edit().putString(KEY_USER_TOKEN, token).apply();
    }

    @Override
    public String getUserPushToken() {
        return sharedPreferences.getString(KEY_USER_TOKEN, "");
    }

    @Override
    public void saveUserToken(String token) {
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply();
    }

    @Override
    public String getUserToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, "");
    }

    @Override
    public void setNewToken(boolean value) {
        sharedPreferences.edit().putBoolean(KEY_PUSH_TOKEN, value).apply();
    }

    @Override
    public boolean isNewToken() {
        return sharedPreferences.getBoolean(KEY_PUSH_TOKEN, false);
    }


    @Override
    public void saveTranslationLUT(List<GetTranslationLUTQuery.TranslationLUT> translationLUTList) {
        sharedPreferences.edit().putString(KEY_Translation, gson.toJson(translationLUTList)).apply();
    }

    @Override
    public String getTranslation(String key) {
        try {
            String translation = sharedPreferences.getString(KEY_Translation, "");
            List<GetTranslationLUTQuery.TranslationLUT> translationLUTS = gson.fromJson(translation, new TypeToken<List<GetTranslationLUTQuery.TranslationLUT>>() {
            }.getType());
            for (GetTranslationLUTQuery.TranslationLUT translationLUT : translationLUTS) {
                if (translationLUT.txt().equals(key)) return translationLUT.translation();
            }
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return key;
        }
    }

}
