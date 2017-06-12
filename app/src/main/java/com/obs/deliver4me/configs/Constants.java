package com.obs.deliver4me.configs;

import okhttp3.MediaType;

/**
 * Created by Arun.S on 4/10/2017.
 */

public class Constants {

    public static final String BASE_URL = "http://52.24.21.112:8080/soc/";
    public static final String APP_NAME = "SOC";
    public static final String LOGGER_TAG = "Error";
    public static final String LOGGER_ERROR = "Received an exception";
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    //Different Font Styles used in the application
    public enum FontStyle {
        BOLD, REGULAR
    }

    //Permission Code
    public static final int REQUEST_PERMISSION = 16;


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");




}
