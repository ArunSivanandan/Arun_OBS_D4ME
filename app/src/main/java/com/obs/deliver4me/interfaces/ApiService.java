package com.obs.deliver4me.interfaces;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Arun.S on 4/10/2017.
 */

public interface ApiService {

    ///// REGISTER /////
    @POST("register")
    Call<ResponseBody> register(@Body RequestBody params);


    ///// LOGIN /////
    @FormUrlEncoded
    @POST("oauth/token")
    Call<ResponseBody> login(@FieldMap Map<String, String> parameters);

}
