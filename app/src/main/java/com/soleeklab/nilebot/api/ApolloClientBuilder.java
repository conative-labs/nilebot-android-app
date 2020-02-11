package com.soleeklab.nilebot.api;


import com.apollographql.apollo.ApolloClient;
import com.soleeklab.nilebot.HandleAppCrash;
import com.soleeklab.nilebot.data.repo.ApplicationContextInjector;
import com.soleeklab.nilebot.data.repo.LocalRepo;
import com.soleeklab.nilebot.data.repo.LocalRepoImpl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApolloClientBuilder {
    private static final String BASE_URL = "https://platformapi.nilebot.com/v1";

    private static ApolloClient apolloClient;
    public static LocalRepo mLocalRepo;

    public static ApolloClient getApolloClient(boolean withHeader) {
        mLocalRepo = new LocalRepoImpl(ApplicationContextInjector.getApplicationContext());
//        HttpLoggingInterceptor.Logger fileLogger = new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String s) {
//                if(s.contains("--> GET") || s.contains("--> POST")) HandleAppCrash.network.setLength(0);
//                HandleAppCrash.network.append(s).append('\n');
//
//            }
//        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor interceptor;

        if (withHeader) {
            interceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .addHeader("Authorization"
                                    , "Bearer " + mLocalRepo.getUserToken())
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            };
        } else {
            interceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            };
        }

        OkHttpClient okHttp = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();


        apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttp)
                .build();


        return apolloClient;
    }


}
