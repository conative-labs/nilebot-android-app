package com.soleeklab.nilebot.data.repo;

import java.util.Map;

/**
 * helper module that gets the default headers from the language and token repo
 * Created by ahmed on 12/21/2016.
 */
public interface HeaderInjector {

    String KEY_CONTENT_TYPE_HEADER = "Content-Type";
    String KEY_AUTH_HEADER = "Authorization";
    String KEY_Bearer = "Bearer ";
    String KEY_LANG = "lang";
    String APPLICATION_JSON = "application/json";

    Map<String, String> getHeaders();


}
