package com.obs.deliver4me.interfaces;


import android.util.Log;

import com.obs.deliver4me.application.AppController;
import com.obs.deliver4me.datamodels.JsonResponse;
import com.obs.deliver4me.utils.WebServiceUtils;

import java.util.logging.LogManager;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Arun S on 14-Jun-17.
 */
public class RequestCallback implements Callback<ResponseBody> {

    protected RequestListener listener;

    @Inject
    JsonResponse jsonResp;
    @Inject
    WebServiceUtils webServiceUtils;

    public RequestCallback() {
        AppController.getAppComponent().inject(this);
    }

    public RequestCallback(RequestListener listener) {
        AppController.getAppComponent().inject(this);
        this.listener = listener;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        this.listener.onSuccess(getSuccessResponse(call, response));
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        this.listener.onFailure(getFailureResponse(call, t));
    }

    private JsonResponse getFailureResponse(Call<ResponseBody> call, Throwable t) {
        try {
            jsonResp.clearAll();
            if (call != null && call.request() != null) {
                jsonResp.setMethod(call.request().method().toString());
                jsonResp.setRequestCode(webServiceUtils.getRequestCodeFromUrl(call.request().url().toString(), call.request().method().toString()));
                jsonResp.setStrRequest(call.request().toString());
                jsonResp.setUrl(call.request().url().toString());
                Log.d("Json Response", call.request().toString());
            }
            jsonResp.setIsSuccess(false);
            jsonResp.setErrorMsg(t.getMessage().toString());
            Log.e("Json Response", t.getMessage().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResp;
    }

    private JsonResponse getSuccessResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        try {
            jsonResp.clearAll();
            if (call != null && call.request() != null) {
                jsonResp.setMethod(call.request().method().toString());
                jsonResp.setRequestCode(webServiceUtils.getRequestCodeFromUrl(call.request().url().toString(), call.request().method().toString()));
                jsonResp.setStrRequest(call.request().toString());
                jsonResp.setUrl(call.request().url().toString());
                Log.d("Json Response", call.request().toString());
            }
            if (response != null) {
                jsonResp.setResponseCode(response.code());
                Log.i("Json Response", "" + response.code());
                if (response.isSuccessful()) {
                    if(response != null && response.body() != null) {
                        String strJson = response.body().string();
                        jsonResp.setStrResponse(strJson);
                        Log.i("Json Response",strJson);
                    }
                    jsonResp.setIsSuccess(true);
                } else {
                    jsonResp.setIsSuccess(false);
                    jsonResp.setErrorMsg(response.errorBody().string());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResp;
    }
}
