package com.soleeklab.nilebot.data.repo;

import android.content.Context;


import com.soleeklab.nilebot.utils.LocalHelper;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class HeaderInjectorImpl implements HeaderInjector {

    private LocalRepo mLocalRepo;
    private LocalHelper localHelper;
    private int langType = 1;

    @Inject
    public HeaderInjectorImpl(Context context) {
        mLocalRepo = new LocalRepoImpl(context);
        localHelper = LocalHelper.getInstance(context);
    }

    @Override
    public Map<String, String> getHeaders() {

        if (localHelper.getLanguage().equalsIgnoreCase(LocalHelper.LANGUAGE_ARABIC))
            langType = 2;
        else
            langType = 1;
        HashMap<String, String> headers = new HashMap<>();

//        if (mLocalRepo.getUser().getToken() != null) {
//            headers.put(KEY_AUTH_HEADER, KEY_Bearer + mLocalRepo.getUser().getToken());
//        } else {
//            headers.put(KEY_AUTH_HEADER, KEY_Bearer + "");
//
//        }
        headers.put(KEY_LANG, langType + "");
        headers.put(KEY_CONTENT_TYPE_HEADER, APPLICATION_JSON);

        return headers;
    }

}
