package com.obs.deliver4me.interfaces;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Arun.S on 4/10/2017.
 */

public interface ApiService {

    ///// REGISTER /////
    @POST("user")
    Call<ResponseBody> registerUserApi(@Body RequestBody requestBody);

    ///// LOGIN /////
    @POST("auth/login")
    Call<ResponseBody> loginTokenApi(@Body RequestBody requestBody);

    @POST("user/social-media/register")
    Call<ResponseBody> fbRegisterApi(@Body RequestBody requestBody);

    @POST("auth/social-media-login")
    Call<ResponseBody> fbLoginApi(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST("user/edit-profile")
    Call<ResponseBody> createProfileMemberApi(@FieldMap Map<String, String> parameters, @FieldMap Map<String, File> params);

    @GET("auth/forgot-password")
    Call<ResponseBody> getForgotPwdApi(@Query("email") String postId);

    @POST("auth/change-password")
    Call<ResponseBody> postChangePwdApi(@Body RequestBody requestBody);

    @POST("auth/reset-password")
    Call<ResponseBody> postResetPwdApi(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST("user/edit-vehicle")
    Call<ResponseBody> createProfileVehicleApi(@FieldMap Map<String, String> parameters);
}
