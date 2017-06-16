package com.obs.deliver4me.dependencies.interceptors;

/**
 * Created by Arun.S on 4/10/2017.
 */

import android.util.Log;

import com.obs.deliver4me.configs.Constants;
import com.obs.deliver4me.configs.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthTokenInterceptor implements Interceptor {
    Request.Builder requestBuilder;
    private SessionManager sessionManager;

    public AuthTokenInterceptor(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            Request original = chain.request();

            if (sessionManager.getToken() != null) {
                // Request customization: add request headers
                requestBuilder = original.newBuilder()
                        .header("Authorization", "bearer " + sessionManager.getToken())
                        .method(original.method(), original.body());
            } else {
                // Request customization: add request headers
                requestBuilder = original.newBuilder()
                        .method(original.method(), original.body());
            }
        }catch (Exception e){
            Log.e(Constants.LOGGER_TAG, Constants.LOGGER_ERROR, e);
        }
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}

