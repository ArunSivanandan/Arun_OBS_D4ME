package com.obs.deliver4me.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.obs.deliver4me.application.AppController;
import com.obs.deliver4me.configs.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;


/**
 * Created by Arun S on 14-Jun-17.
 */
public class WebServiceUtils {
    @Inject
    Gson gson;

    public WebServiceUtils() {
        AppController.getAppComponent().inject(this);
    }

    public int getRequestCodeFromUrl(String requestUrl, String method) {
        Log.e(" Method ", method + " Url : " + requestUrl);
        int requestCode = 0;
//        if (requestUrl.contains("?")) {
//            requestUrl = requestUrl.substring(Constants.BASE_URL.length(), requestUrl.indexOf("?"));
//        } else {
//            requestUrl = requestUrl.substring(Constants.BASE_URL.length());
//        }

        if (requestUrl.equals(Constants.BASE_URL + "user")) {
            requestCode = Constants.POST_REGISTER_USER;
        } else if (requestUrl.equals(Constants.BASE_URL + "auth/login")) {
            requestCode = Constants.POST_AUTH_LOGIN_TOKEN;
        } else if (requestUrl.equals(Constants.BASE_URL + "user/social-media/register")) {
            requestCode = Constants.POST_FB_REGISTER;
        } else if (requestUrl.equals(Constants.BASE_URL + "auth/social-media-login")) {
            requestCode = Constants.POST_FB_LOGIN;
        } else if (requestUrl.equals(Constants.BASE_URL + "user/edit-profile")) {
            requestCode = Constants.POST_PROFILE_MEMBER;
        } else if (requestUrl.contains(Constants.BASE_URL + "auth/forgot-password")) {
            requestCode = Constants.GET_FORGOT_PASSWORD;
        } else if (requestUrl.equals(Constants.BASE_URL + "user/edit-vehicle")) {
            requestCode = Constants.POST_PROFILE_VEHICLE;
        }

        return requestCode;
    }

    public Object getJsonObjectModel(String jsonString, Object object) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            if (jsonObject != null) {
                object = gson.fromJson(jsonObject.toString(), (Class<Object>) object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return object;
    }

    public Object getJsonValue(String jsonString, String key, Object object) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            if (jsonObject != null && jsonObject.has(key)) {
                object = jsonObject.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return object;
    }

    public Object getJsonArrayModel(String jsonString, Object object) {
        JSONArray jsonArray = null;
        Object[] objects = null;
        try {
            jsonArray = new JSONArray(jsonString);
            if (jsonArray != null && jsonArray.length() > 0) {
                objects = gson.fromJson(jsonArray.toString(), (Class<Object[]>) object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return new java.util.ArrayList(Arrays.asList(objects));
    }

    public Object getJsonObject(String jsonString, Object object) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            if (jsonObject != null) {
                object = gson.fromJson(jsonObject.toString(), (Class<Object>) object);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Object();
        }
        return object;
    }
}
