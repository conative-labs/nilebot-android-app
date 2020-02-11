package com.soleeklab.nilebot.data.repo;


import com.soleeklab.nilebot.GetTranslationLUTQuery;
import com.soleeklab.nilebot.LoginMutation;

import java.util.List;

/**
 * Created by omaraboulfotoh on 12/13/17.
 */

public interface LocalRepo {

    void saveMobile(String mobile);

    String getMobile();

    void logout();

    void setLoginFalse();

    boolean isLogin();

    void setFirstTimeLaunchFalse(boolean value);

    boolean isFirstTimeLaunch();

    void saveUser(LoginMutation.User user);

    LoginMutation.User getUser();

    void saveUserPushToken(String token);

    String getUserPushToken();


    void saveUserToken(String token);

    String getUserToken();

    void setNewToken(boolean value);
    boolean isNewToken();


    void saveTranslationLUT(List<GetTranslationLUTQuery.TranslationLUT> translationLUTList);

    String getTranslation(String key);




}
