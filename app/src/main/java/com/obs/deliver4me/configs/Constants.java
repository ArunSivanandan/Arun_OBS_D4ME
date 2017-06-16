package com.obs.deliver4me.configs;

import android.Manifest;

import okhttp3.MediaType;

/**
 * Created by Arun.S on 4/10/2017.
 */

public class Constants {

    public static final String BASE_URL = "http://192.237.253.117:9002/";
    public static final String APP_NAME = "Deliver4Me";
    public static final String LOGGER_TAG = "Error";
    public static final String LOGGER_ERROR = "Received an exception";
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /*Regex Password Validation*/
    public final static String PASSWORD_VALIDITY = "^(?=\\D*\\d)\\S{6,}$";

    //Different Font Styles used in the application
    public enum FontStyle {
        BOLD, REGULAR
    }

    public static final String[] EDIT_PROFILE = {"Take Photo", "Choose from Gallery"};
    public static final String FILE_NAME = "deliver4me_now";

    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
    };

    public static final String[] PERMISSIONS_LOCATION = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final int POST_REGISTER_USER = 10;
    public static final int POST_AUTH_LOGIN_TOKEN = 11;
    public static final int POST_FB_REGISTER = 12;
    public static final int POST_FB_LOGIN = 13;
    public static final int GET_FORGOT_PASSWORD = 14;
    public static final int POST_PROFILE_MEMBER = 15;
    public static final int POST_PROFILE_VEHICLE = 16;
    public static final int REQUEST_CODE_GALLERY = 34;
}
