package com.soleeklab.nilebot.api;


import com.soleeklab.nilebot.data.api.BaseResponse;
import com.soleeklab.nilebot.data.api.RequestBug;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface    ApiInterface {

    @POST("http://hayahbook.net/api/api/reports")
    Call<BaseResponse> crashApi(@Body RequestBug requestBug);

    @Headers({"Content-Type: application/json",
            "Authorization: Bearer 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"})
    @POST("https://api.sendgrid.com/v3/mail/send")
    Call<ResponseBody> sendGridApi(@Body RequestBody body);

}

